package an.xuzaixiang.camera.features;

import android.hardware.camera2.CaptureRequest;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.CameraProperties;


public abstract class CameraFeature<T> {

    protected final CameraProperties cameraProperties;

    protected CameraFeature(@NonNull CameraProperties cameraProperties) {
        this.cameraProperties = cameraProperties;
    }

    public abstract String getDebugName();

    public abstract T getValue();

    public abstract void setValue(T value);

    public abstract boolean checkIsSupported();

    public abstract void updateBuilder(CaptureRequest.Builder requestBuilder);
}
