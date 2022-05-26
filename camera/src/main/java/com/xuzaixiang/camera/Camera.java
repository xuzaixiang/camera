package com.xuzaixiang.camera;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xuzaixiang.camera.features.CameraFeatureFactory;
import com.xuzaixiang.camera.features.CameraFeatureFactoryImpl;
import com.xuzaixiang.camera.features.CameraFeatures;
import com.xuzaixiang.camera.features.CameraSession;
import com.xuzaixiang.camera.features.HandlerFactory;
import com.xuzaixiang.camera.features.HandlerThreadFactory;
import com.xuzaixiang.camera.features.resolution.ResolutionFeature;
import com.xuzaixiang.camera.features.resolution.ResolutionPreset;


public class Camera {
    private final Context context;
    private final boolean enableAudio;
    private final ResolutionPreset resolutionPreset;
    private final CameraDescription description;
    private final CameraProperties cameraProperty;
    private final CameraFeatureFactory cameraFeatureFactory;
    private final CameraFeatures cameraFeatures;
    private final CameraCallback callback;
    private final SurfaceTexture surfaceTexture;

    private HandlerThread backgroundHandlerThread;
    private Handler backgroundHandler;
    private CameraSession cameraSession;


    private static final String TAG = "Camera";


    public Camera(
            Context context,
            SurfaceTexture surfaceTexture,
            CameraDescription description,
            CameraCallback callback,
            boolean enableAudio,
            ResolutionPreset resolutionPreset
    ) throws CameraAccessException {
        this(context, surfaceTexture, description, callback, enableAudio, resolutionPreset, new CameraFeatureFactoryImpl());
    }

    public Camera(
            Context context,
            SurfaceTexture surfaceTexture,
            CameraDescription description,
            CameraCallback callback,
            boolean enableAudio,
            ResolutionPreset resolutionPreset,
            CameraFeatureFactory cameraFeatureFactory
    ) throws CameraAccessException {
        this.context = context;
        this.enableAudio = enableAudio;
        this.description = description;
        this.resolutionPreset = resolutionPreset;
        this.cameraProperty = new CameraPropertiesImpl(description.getName(), CameraUtil.getCameraManager(context));
        this.cameraFeatureFactory = cameraFeatureFactory;
        this.cameraFeatures = CameraFeatures.init(cameraFeatureFactory, cameraProperty, context, resolutionPreset);
        this.surfaceTexture = surfaceTexture;

        this.callback = callback;

        backgroundHandlerThread = HandlerThreadFactory.create("CameraBackground");
        try {
            backgroundHandlerThread.start();
        } catch (IllegalThreadStateException e) {
            // Ignore exception in case the thread has already started.
        }
        backgroundHandler = HandlerFactory.create(backgroundHandlerThread.getLooper());
    }

    @SuppressLint("MissingPermission")
    public void open(ImageFormatGroup imageFormat) throws CameraAccessException {
        final ResolutionFeature resolutionFeature = cameraFeatures.getResolution();
        if (!resolutionFeature.checkIsSupported()) {
            callback.onError("Camera with name \""
                    + cameraProperty.getCameraName()
                    + "\" is not supported.");
            return;
        }
        CameraManager cameraManager = CameraUtil.getCameraManager(context);
        cameraManager.openCamera(description.getName(), new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                cameraSession = new CameraSession(
                        imageFormat,
                        surfaceTexture,
                        backgroundHandler,
                        cameraFeatures,
                        new DefaultCameraDeviceWrapper(camera),
                        callback
                );
                try {
                    cameraSession.open();
                    callback.onInitialized();
                } catch (CameraAccessException e) {
                    callback.onClosing();
                    close();
                }
            }

            @Override
            public void onClosed(@NonNull CameraDevice camera) {
                Log.i(TAG, "open | onClosed");
                cameraSession.close();
                cameraSession = null;
                callback.onClosing();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                Log.i(TAG, "open | onDisconnected");
                close();
                callback.onError("The camera was disconnected.");
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                Log.i(TAG, "open | onError");
                close();
                String errorDescription;
                switch (error) {
                    case ERROR_CAMERA_IN_USE:
                        errorDescription = "The camera device is in use already.";
                        break;
                    case ERROR_MAX_CAMERAS_IN_USE:
                        errorDescription = "Max cameras in use";
                        break;
                    case ERROR_CAMERA_DISABLED:
                        errorDescription = "The camera device could not be opened due to a device policy.";
                        break;
                    case ERROR_CAMERA_DEVICE:
                        errorDescription = "The camera device has encountered a fatal error";
                        break;
                    case ERROR_CAMERA_SERVICE:
                        errorDescription = "The camera service has encountered a fatal error.";
                        break;
                    default:
                        errorDescription = "Unknown camera error";
                }
                callback.onError(errorDescription);
            }
        }, backgroundHandler);
    }

    public void close() {
        if (cameraSession != null) {
            cameraSession.close();
        }
        if (backgroundHandlerThread != null) {
            backgroundHandlerThread.quitSafely();
            try {
                backgroundHandlerThread.join();
            } catch (InterruptedException e) {
                callback.onError(e.getMessage());
            }
        }
        backgroundHandlerThread = null;
        backgroundHandler = null;
    }
}
