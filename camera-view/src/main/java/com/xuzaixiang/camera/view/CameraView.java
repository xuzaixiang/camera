package com.xuzaixiang.camera.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.xuzaixiang.camera.Camera;
import com.xuzaixiang.camera.CameraCallback;
import com.xuzaixiang.camera.CameraDescription;
import com.xuzaixiang.camera.CameraLensDirection;
import com.xuzaixiang.camera.CameraProvider;
import com.xuzaixiang.camera.features.resolution.ResolutionPreset;
import com.xuzaixiang.camera.SurfaceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CameraView extends TextureView implements CameraCallback, TextureView.SurfaceTextureListener {

    private static final String TAG = "Camera";

    public CameraView(@NonNull Context context) {
        super(context);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Camera mCamera;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSurfaceTextureListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        Executor executor = ContextCompat.getMainExecutor(getContext());
        CameraProvider.getInstance(getContext()).addListener(executor, provider -> {
            if (provider == null) {
                return;
            }
            mCamera = provider.newCamera(CameraLensDirection.back);
            assert mCamera != null;
            mCamera.open(ResolutionPreset.high, new Camera.Callback() {
                @Override
                public void onSurfaceRequest(SurfaceRequest request) {
                    Surface surface = new Surface(surfaceTexture);
                    List<Surface> surfaces = new ArrayList<>();
                    surfaces.add(surface);
                    int width = request.getResolution().getWidth();
                    int height = request.getResolution().getHeight();
                    surfaceTexture.setDefaultBufferSize(width, height);
                    request.provideSurface(surfaces, executor, () -> {
                        surfaceTexture.release();
                        surface.release();
                    });
                }

                @Override
                public void onError(String errCode, String errDescription) {
                    Log.e(TAG, "onError: -- ");
                }

                @Override
                public void onOpened() {
                    Log.e(TAG, "onOpened: -- ");
                }

                @Override
                public void onClosed() {
                    Log.e(TAG, "onClosed: -- ");
                }
            });

        });
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (mCamera != null && !mCamera.isClosed() && !mCamera.isClosing()) {
            mCamera.close();
            mCamera = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    @Override
    public void onInitialized() {

    }

    @Override
    public void onClosing() {

    }

    @Override
    public void onError(String description) {
        Log.e(TAG, "onError: " + description);
    }


}
