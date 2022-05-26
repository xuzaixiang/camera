package com.xuzaixiang.camera;

public class CameraDescription {
    private final String mName;
    private final int mSensorOrientation;
    private final CameraLensDirection mLensDirection;

    CameraDescription(String name, int sensorOrientation, CameraLensDirection lensDirection) {
        mName = name;
        mSensorOrientation = sensorOrientation;
        mLensDirection = lensDirection;
    }

    public String getName() {
        return mName;
    }

    public int getSensorOrientation() {
        return mSensorOrientation;
    }

    public CameraLensDirection getLensDirection() {
        return mLensDirection;
    }
}
