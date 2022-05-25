package com.xuzaixiang.camera.features.exposureoffset;

import android.hardware.camera2.CaptureRequest;
import android.util.Range;

import androidx.annotation.NonNull;

import com.xuzaixiang.camera.CameraProperties;
import com.xuzaixiang.camera.features.CameraFeature;

public class ExposureOffsetFeature extends CameraFeature<Double> {

  private double currentSetting = 0;

  public ExposureOffsetFeature(CameraProperties cameraProperties) {
    super(cameraProperties);
  }

  @Override
  public String getDebugName() {
    return "ExposureOffsetFeature";
  }

  @Override
  public Double getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(@NonNull Double value) {
    double stepSize = getExposureOffsetStepSize();
    this.currentSetting = value / stepSize;
  }

  @Override
  public boolean checkIsSupported() {
    return true;
  }

  @Override
  public void updateBuilder(CaptureRequest.Builder requestBuilder) {
    if (!checkIsSupported()) {
      return;
    }

    requestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, (int) currentSetting);
  }

  public double getMinExposureOffset() {
    Range<Integer> range = cameraProperties.getControlAutoExposureCompensationRange();
    double minStepped = range == null ? 0 : range.getLower();
    double stepSize = getExposureOffsetStepSize();
    return minStepped * stepSize;
  }
  public double getMaxExposureOffset() {
    Range<Integer> range = cameraProperties.getControlAutoExposureCompensationRange();
    double maxStepped = range == null ? 0 : range.getUpper();
    double stepSize = getExposureOffsetStepSize();
    return maxStepped * stepSize;
  }

  public double getExposureOffsetStepSize() {
    return cameraProperties.getControlAutoExposureCompensationStep();
  }
}
