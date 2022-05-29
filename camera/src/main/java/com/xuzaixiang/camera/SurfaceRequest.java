package com.xuzaixiang.camera;

import android.view.Surface;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

public class SurfaceRequest {

    public interface Callback {
        void onTransform(int width, int height);

        // 发生错误不会触发
        void onReleased();
    }

    private Callback mCallback;
    private List<Surface> mSurface;

    public SurfaceRequest() {

    }

    public void provideSurface(@NonNull List<Surface> surface, Callback callback) {
        mSurface = surface;
        mCallback = callback;
    }

    public void setTransformListener(@NonNull Executor executor){

    }

    List<Surface> getSurface() {
        return mSurface;
    }

    void transform(int width, int height) {
        if (mCallback != null) {
            mCallback.onTransform(width, height);
        }
    }
}
