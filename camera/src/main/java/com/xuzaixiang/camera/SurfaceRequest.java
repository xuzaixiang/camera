package com.xuzaixiang.camera;

import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

public class SurfaceRequest {

    public interface Callback {
        // 发生错误不会触发
        void onRelease();
    }

    private final Size mSize;

    private Callback mCallback;
    private List<Surface> mSurface;
    private Executor mExecutor;

    public SurfaceRequest(Size size) {
        mSize = size;
    }

    public void provideSurface(@NonNull List<Surface> surface, Executor executor, Callback callback) {
        mSurface = surface;
        mCallback = callback;
        mExecutor = executor;
    }

    void release() {
        if (mExecutor != null && mCallback != null) {
            mExecutor.execute(() -> mCallback.onRelease());
        }
    }

    List<Surface> getSurface() {
        return mSurface;
    }

    @NonNull
    public Size getResolution() {
        return mSize;
    }
}
