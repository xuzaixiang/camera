package com.xuzaixiang.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuzaixiang.camera.features.CameraFeature;
import com.xuzaixiang.camera.features.CameraFeatures;
import com.xuzaixiang.camera.features.resolution.ResolutionFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@FunctionalInterface
interface ErrorCallback {
    void onError(String errorCode, String errorMessage);
}

public class CameraSession {
    private static final String TAG = "CameraSession";

    private final CameraFeatures cameraFeatures;
    private final CameraDeviceWrapper cameraDevice;
    private final Camera.Callback mSurfaceProvider;
    private final Handler handler;
    private final CameraCallback callback;

    private ImageReader pictureImageReader;

    private CameraCaptureSession session;
    private CaptureRequest.Builder previewRequestBuilder;

    public CameraSession(
            Camera.Callback provider,
            Handler handler,
            CameraFeatures cameraFeatures,
            CameraDeviceWrapper cameraDevice,
            CameraCallback callback
    ) {
        this.callback = callback;
        this.handler = handler;
        this.cameraDevice = cameraDevice;
        this.cameraFeatures = cameraFeatures;
        this.mSurfaceProvider = provider;
        final ResolutionFeature resolutionFeature = cameraFeatures.getResolution();
//        pictureImageReader =
//                ImageReader.newInstance(
//                        resolutionFeature.getCaptureSize().getWidth(),
//                        resolutionFeature.getCaptureSize().getHeight(),
//                        ImageFormat.JPEG,
//                        1);
    }

    private void refreshPreviewCaptureSession(
            @Nullable Runnable onSuccessCallback, @NonNull ErrorCallback onErrorCallback) {
        Log.i(TAG, "refreshPreviewCaptureSession");

        if (session == null) {
            Log.i(
                    TAG,
                    "refreshPreviewCaptureSession: captureSession not yet initialized, "
                            + "skipping preview capture session refresh.");
            return;
        }

        try {
//            if (!pausedPreview) {
            session.setRepeatingRequest(
                    previewRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                            super.onCaptureStarted(session, request, timestamp, frameNumber);
                        }

                        @Override
                        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                            super.onCaptureProgressed(session, request, partialResult);
                        }

                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                        }

                        @Override
                        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                            super.onCaptureFailed(session, request, failure);
                        }

                        @Override
                        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                        }

                        @Override
                        public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                            super.onCaptureSequenceAborted(session, sequenceId);
                        }

                        @Override
                        public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                            super.onCaptureBufferLost(session, request, target, frameNumber);
                        }
                    }, handler);
//            }

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

        } catch (IllegalStateException e) {
            onErrorCallback.onError("cameraAccess", "Camera is closed: " + e.getMessage());
        } catch (CameraAccessException e) {
            onErrorCallback.onError("cameraAccess", e.getMessage());
        }
    }

    private void createCaptureSession(
            int templateType, Runnable onSuccessCallback, Surface... surfaces)
            throws CameraAccessException {
        closeCaptureSession();
        previewRequestBuilder = cameraDevice.createCaptureRequest(templateType);

        ResolutionFeature resolutionFeature = cameraFeatures.getResolution();
        int width = resolutionFeature.getPreviewSize().getWidth();
        int height = resolutionFeature.getPreviewSize().getHeight();
        SurfaceRequest request = new SurfaceRequest(new Size(width,height));
        mSurfaceProvider.onSurfaceRequest(request);

        Surface surface = request.getSurface().get(0);
        previewRequestBuilder.addTarget(surface);

        List<Surface> remainingSurfaces = Arrays.asList(surfaces);
        if (templateType != CameraDevice.TEMPLATE_PREVIEW) {
            // If it is not preview mode, add all surfaces as targets.
            for (Surface s : remainingSurfaces) {
                previewRequestBuilder.addTarget(s);
            }
        }

        CameraCaptureSession.StateCallback callback =
                new CameraCaptureSession.StateCallback() {
                    boolean captureSessionClosed = false;

                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        Log.i(TAG, "CameraCaptureSession onConfigured");
                        if (captureSessionClosed) {
                            CameraSession.this.callback.onError("The camera was closed during configuration.");
                            return;
                        }
                        CameraSession.this.session = session;

                        Log.i(TAG, "Updating builder settings");
                        for (CameraFeature feature : cameraFeatures.getAllFeatures()) {
                            Log.d(TAG, "Updating builder with feature: " + feature.getDebugName());
                            feature.updateBuilder(previewRequestBuilder);
                        }

                        refreshPreviewCaptureSession(
                                onSuccessCallback, (code, message) ->
                                        CameraSession.this.callback.onError(message)
                        );
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        Log.i(TAG, "CameraCaptureSession onConfigureFailed");
                        CameraSession.this.callback.onError("Failed to configure camera session.");
                    }

                    @Override
                    public void onClosed(@NonNull CameraCaptureSession session) {
                        Log.i(TAG, "CameraCaptureSession onClosed");
                        captureSessionClosed = true;
                    }
                };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Collect all surfaces to render to.
            List<OutputConfiguration> configs = new ArrayList<>();
            configs.add(new OutputConfiguration(surface));
            for (Surface s : remainingSurfaces) {
                configs.add(new OutputConfiguration(s));
            }
            cameraDevice.createCaptureSession(
                    new SessionConfiguration(
                            SessionConfiguration.SESSION_REGULAR,
                            configs,
                            Executors.newSingleThreadExecutor(),
                            callback));
        } else {
            // Collect all surfaces to render to.
            List<Surface> surfaceList = new ArrayList<>();
            surfaceList.add(surface);
            surfaceList.addAll(remainingSurfaces);
            cameraDevice.createCaptureSession(surfaceList, callback, handler);
        }
    }

    private void createCaptureSession(int templateType, Surface... surfaces)
            throws CameraAccessException {
        createCaptureSession(templateType, null, surfaces);
    }

    public void open() throws CameraAccessException {
//        if (pictureImageReader == null || pictureImageReader.getSurface() == null) return;
//        Log.i(TAG, "startPreview");
//        createCaptureSession(CameraDevice.TEMPLATE_PREVIEW, pictureImageReader.getSurface());
        createCaptureSession(CameraDevice.TEMPLATE_PREVIEW);
    }

    private void closeCaptureSession() {
        if (session != null) {
            Log.i(TAG, "stopPreview");
            session.close();
            session = null;
        }
    }

    public void close() {
        closeCaptureSession();
        cameraDevice.close();
//        if (pictureImageReader != null) {
//            pictureImageReader.close();
//            pictureImageReader = null;
//        }
//        if (mediaRecorder != null) {
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            mediaRecorder = null;
//        }
    }
}
