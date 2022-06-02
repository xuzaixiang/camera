package an.xuzaixiang.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.features.CameraFeatureFactory;
import an.xuzaixiang.camera.features.CameraFeatureFactoryImpl;
import an.xuzaixiang.camera.features.CameraFeatures;
import an.xuzaixiang.camera.features.resolution.ResolutionPreset;

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

    // permission : camera
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
                        callback.onOpened();
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
                    callback.onError("openCamera : onDisconnected", "The camera was disconnected.");
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    close();
                    callback.onError("openCamera : onError", CameraUtil.getCameraErrorDescription(error));
                }
            }, mHandler.getHandler());
        } catch (CameraAccessException e) {
            callback.onError("CameraAccessException on openCamera : " +
                    "the camera is disabled by device policy, has been disconnected, " +
                    "is being used by a higher-priority camera API client, " +
                    "or the device has reached its maximal resource and cannot open this camera device", e.getMessage());
        }
    }

    public boolean isClosed() {
        return mIsClosed.get();
    }

    public boolean isClosing() {
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
