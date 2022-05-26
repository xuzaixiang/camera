package com.xuzaixiang.camera;

import android.graphics.ImageFormat;

public enum ImageFormatGroup {
    yuv_420,
    jpeg;

    public int value() {
        if (this == ImageFormatGroup.jpeg) {
            return ImageFormat.JPEG;
        }
        return ImageFormat.YUV_420_888;
    }
}
