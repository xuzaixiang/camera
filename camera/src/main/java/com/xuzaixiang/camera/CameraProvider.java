package com.xuzaixiang.camera;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CameraProvider {
    private static final CameraProvider sProvider = new CameraProvider();
    private final List<CameraDescription> mCameraDescription = new ArrayList<>();
    private ListenableFutureTask<CameraProvider> mTask;
    private CameraThread mCameraThread;

    public static ListenableFutureTask<CameraProvider> getInstance(Context context) {
        synchronized (sProvider) {
            if (!sProvider.mCameraDescription.isEmpty())
                return new ListenableFutureTask<>(() -> sProvider);
            if (sProvider.mCameraThread == null)
                sProvider.mCameraThread = new CameraThread();
            ListenableFutureTask<CameraProvider> task = new ListenableFutureTask<>(() -> {
                synchronized (sProvider) {
                    sProvider.mCameraDescription.addAll(CameraUtil.getAvailableCameras(context));
                    return sProvider;
                }
            });
            sProvider.mCameraThread.execute(task);
            return task;
        }
    }


}
