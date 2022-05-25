package com.xuzaixiang.camera;


import android.content.Context;
import android.hardware.camera2.CameraAccessException;

import com.xuzaixiang.camera.features.resolution.ResolutionPreset;

public class Camera {
    private final CameraProperties mCameraProperty;
    private final Context mContext;
    private final boolean mEnableAudio;
    private final CameraDescription mDescription;
    private final ResolutionPreset mResolutionPreset;

    public Camera(
            Context context,
            CameraDescription description
    ) throws CameraAccessException {
        this(context, description, true, ResolutionPreset.high);
    }

    public Camera(
            Context context,
            CameraDescription description,
            boolean enableAudio,
            ResolutionPreset resolutionPreset
    ) throws CameraAccessException {
        mContext = context;
        mEnableAudio = enableAudio;
        mDescription = description;
        mResolutionPreset = resolutionPreset;
        mCameraProperty = new CameraPropertiesImpl(description.getName(), CameraUtil.getCameraManager(context));
    }

    public void close() {

    }
}
