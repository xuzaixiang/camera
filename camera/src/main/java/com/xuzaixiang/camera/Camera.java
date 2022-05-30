package com.xuzaixiang.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;

import com.xuzaixiang.camera.features.CameraFeatureFactory;
import com.xuzaixiang.camera.features.CameraFeatureFactoryImpl;
import com.xuzaixiang.camera.features.CameraFeatures;
import com.xuzaixiang.camera.features.resolution.ResolutionPreset;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class Camera {

    public interface Callback {
        void onOpened();

        void onClosed();

        void onError(String errCode, String errDescription);

        void onSurfaceRequest(SurfaceRequest request);
    }

    private final Context mContext;
    private final CameraManager mCameraManager;
    private final CameraDescription mDescription;
    private final CameraProperties mCameraProperty;
    private final CameraThread mHandler;
    private final CameraFeatureFactory mCameraFeatureFactory;
    private final AtomicBoolean mIsClosed = new AtomicBoolean(false);
    private final AtomicBoolean mIsClosing = new AtomicBoolean(false);

    private CameraFeatures mCameraFeatures;

    private CameraCallback callback;

    private CameraSession cameraSession;

    private static final String TAG = "Camera";

    public Camera(
            Context context,
            CameraManager cameraManager,
            CameraDescription description
    ) throws CameraAccessException {
        mContext = context;
        mCameraManager = cameraManager;
        mDescription = description;
        mCameraFeatureFactory = new CameraFeatureFactoryImpl();
        mCameraProperty = new CameraPropertiesImpl(description.getName(), cameraManager);
        mHandler = new CameraThread();
    }

    @SuppressLint("MissingPermission")
    public void open(ResolutionPreset resolutionPreset, Callback callback) {
        if (mIsClosed.get()) {
            throw new RuntimeException("Camera had closed.");
        }
        if (mIsClosing.get()) {
            throw new RuntimeException("Camera is closing.");
        }
        assert mHandler != null;
        mCameraFeatures = CameraFeatures.init(mCameraFeatureFactory, mCameraProperty, mContext, resolutionPreset);
        try {
            mCameraManager.openCamera(mDescription.getName(), new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraSession = new CameraSession(
                            callback,
                            mHandler.getHandler(),
                            mCameraFeatures,
                            new DefaultCameraDeviceWrapper(camera),
                            Camera.this.callback
                    );
                    try {
                        cameraSession.open();
                    } catch (CameraAccessException e) {
                        close();
                        callback.onError("CameraAccess", e.getMessage());
                    }
                }

                @Override
                public void onClosed(@NonNull CameraDevice camera) {
                    super.onClosed(camera);
                    mIsClosed.set(true);
                    mIsClosing.set(false);
                    callback.onClosed();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    close();
                    callback.onError("openCamera", "The camera was disconnected.");
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
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
                    callback.onError("openCamera", errorDescription);
                }
            }, mHandler.getHandler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
            callback.onError("CameraAccess", e.getMessage());
        }
    }

    public boolean isClosed() {
        return mIsClosed.get();
    }

    public boolean isClosing(){
        return mIsClosing.get();
    }

    public void close() {
        if (mIsClosed.get()) {
            throw new RuntimeException("Camera had closed.");
        }
        if (mIsClosing.get()) {
            throw new RuntimeException("Camera is closing.");
        }
        mIsClosing.set(true);
        if (cameraSession != null) {
            cameraSession.close();
            cameraSession = null;
        }
        mHandler.stop();
    }
}
