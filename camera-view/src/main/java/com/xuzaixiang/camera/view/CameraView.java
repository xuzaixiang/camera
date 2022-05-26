package com.xuzaixiang.camera.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuzaixiang.camera.Camera;
import com.xuzaixiang.camera.CameraCallback;
import com.xuzaixiang.camera.CameraDescription;
import com.xuzaixiang.camera.CameraProvider;
import com.xuzaixiang.camera.CameraUtil;
import com.xuzaixiang.camera.ImageFormatGroup;
import com.xuzaixiang.camera.features.resolution.ResolutionPreset;

import java.util.ArrayList;
import java.util.List;

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
        setSurfaceTextureListener(null);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        List<CameraDescription> descriptions;
        try {
            descriptions = CameraUtil.getAvailableCameras(getContext());
        } catch (CameraAccessException e) {
            e.printStackTrace();
            descriptions = new ArrayList<>();
        }
        if (descriptions.isEmpty()) {
            return;
        }
        try {
            CameraProvider.getInstance();
            mCamera = new Camera(getContext(), surface, descriptions.get(0), this, true, ResolutionPreset.high);
            mCamera.open(ImageFormatGroup.yuv_420);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        mCamera.close();
        return true;
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
