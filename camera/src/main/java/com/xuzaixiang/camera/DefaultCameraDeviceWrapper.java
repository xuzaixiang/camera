package com.xuzaixiang.camera;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DefaultCameraDeviceWrapper implements CameraDeviceWrapper {
    private final CameraDevice cameraDevice;

    public DefaultCameraDeviceWrapper(CameraDevice cameraDevice) {
        this.cameraDevice = cameraDevice;
    }

    @NonNull
    @Override
    public CaptureRequest.Builder createCaptureRequest(int templateType)
            throws CameraAccessException {
        return cameraDevice.createCaptureRequest(templateType);
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public void createCaptureSession(SessionConfiguration config) throws CameraAccessException {
        cameraDevice.createCaptureSession(config);
    }

    @Override
    public void createCaptureSession(
            @NonNull List<Surface> outputs,
            @NonNull CameraCaptureSession.StateCallback callback,
            @Nullable Handler handler)
            throws CameraAccessException {
        cameraDevice.createCaptureSession(outputs, callback, handler);
    }

    @Override
    public void close() {
        cameraDevice.close();
    }
}