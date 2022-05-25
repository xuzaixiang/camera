package com.xuzaixiang.camera;

import android.graphics.Rect;
import android.os.Build;
import android.util.Range;
import android.util.Size;

import androidx.annotation.RequiresApi;

public interface CameraProperties {
    String getCameraName();

    Range<Integer>[] getControlAutoExposureAvailableTargetFpsRanges();

    Range<Integer> getControlAutoExposureCompensationRange();

    double getControlAutoExposureCompensationStep();

    int[] getControlAutoFocusAvailableModes();

    Integer getControlMaxRegionsAutoExposure();

    Integer getControlMaxRegionsAutoFocus();

    @RequiresApi(api = Build.VERSION_CODES.P)
    int[] getDistortionCorrectionAvailableModes();

    Boolean getFlashInfoAvailable();

    int getLensFacing();

    Float getLensInfoMinimumFocusDistance();

    Float getScalerAvailableMaxDigitalZoom();

    Rect getSensorInfoActiveArraySize();

    Size getSensorInfoPixelArraySize();

    @RequiresApi(api = Build.VERSION_CODES.M)
    Rect getSensorInfoPreCorrectionActiveArraySize();

    int getSensorOrientation();

    int getHardwareLevel();

    int[] getAvailableNoiseReductionModes();
}
