package com.xuzaixiang.camera.view;

import android.content.Context;
import android.graphics.Matrix;
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
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

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
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
        mSurface = new Surface(surface);
        Executor executor = ContextCompat.getMainExecutor(getContext());
        CameraProvider.getInstance(getContext()).addListener(executor, provider -> {
            if (provider == null) {
                return;
            }
            List<CameraDescription> descriptions = provider.getCameraDescription();
            mCamera = provider.newCamera(descriptions.get(0));
            assert mCamera != null;
            mCamera.open(ResolutionPreset.high, request -> {
                List<Surface> surfaces = new ArrayList<>();
                surfaces.add(mSurface);
                request.setTransformListener(executor);
                request.provideSurface(surfaces, new SurfaceRequest.Callback() {
                    @Override
                    public void onTransform(int width, int height) {
                        mSurfaceTexture.setDefaultBufferSize(width, height);
                        Log.e(TAG, "onTransform: width :" + width + "height : " + height);
                    }

                    @Override
                    public void onReleased() {
                        mSurfaceTexture.release();
                        mSurface.release();
                    }
                });
            }, new Camera.OpenCallback() {
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
        if (mCamera != null) {
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
