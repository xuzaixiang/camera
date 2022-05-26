package com.xuzaixiang.camera.features;

import android.os.HandlerThread;

import androidx.annotation.VisibleForTesting;

public class HandlerThreadFactory {
    public static HandlerThread create(String name) {
        return new HandlerThread(name);
    }
}

