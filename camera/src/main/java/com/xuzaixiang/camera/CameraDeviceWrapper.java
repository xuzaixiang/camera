package com.xuzaixiang.camera;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface CameraDeviceWrapper {
    @NonNull
    CaptureRequest.Builder createCaptureRequest(int templateType) throws CameraAccessException;

    @TargetApi(Build.VERSION_CODES.P)
    void createCaptureSession(SessionConfiguration config) throws CameraAccessException;

    void createCaptureSession(
            @NonNull List<Surface> outputs,
            @NonNull CameraCaptureSession.StateCallback callback,
            @Nullable Handler handler)
            throws CameraAccessException;

    void close();
}