package an.xuzaixiang.camera.features.exposurelock;

import android.hardware.camera2.CaptureRequest;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;

public class ExposureLockFeature extends CameraFeature<ExposureMode> {

  private ExposureMode currentSetting = ExposureMode.auto;
  public ExposureLockFeature(CameraProperties cameraProperties) {
    super(cameraProperties);
  }

  @Override
  public String getDebugName() {
    return "ExposureLockFeature";
  }

  @Override
  public ExposureMode getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(ExposureMode value) {
    this.currentSetting = value;
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

    requestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, currentSetting == ExposureMode.locked);
  }
}
