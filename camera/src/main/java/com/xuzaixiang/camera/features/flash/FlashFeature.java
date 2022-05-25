package com.xuzaixiang.camera.features.flash;

import android.hardware.camera2.CaptureRequest;

import com.xuzaixiang.camera.CameraProperties;
import com.xuzaixiang.camera.features.CameraFeature;

public class FlashFeature extends CameraFeature<FlashMode> {
  private FlashMode currentSetting = FlashMode.auto;

  public FlashFeature(CameraProperties cameraProperties) {
    super(cameraProperties);
  }

  @Override
  public String getDebugName() {
    return "FlashFeature";
  }

  @Override
  public FlashMode getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(FlashMode value) {
    this.currentSetting = value;
  }

  @Override
  public boolean checkIsSupported() {
    Boolean available = cameraProperties.getFlashInfoAvailable();
    return available != null && available;
  }

  @Override
  public void updateBuilder(CaptureRequest.Builder requestBuilder) {
    if (!checkIsSupported()) {
      return;
    }

    switch (currentSetting) {
      case off:
        requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
        requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        break;

      case always:
        requestBuilder.set(
            CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
        requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        break;

      case torch:
        requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
        requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
        break;

      case auto:
        requestBuilder.set(
            CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        break;
    }
  }
}
