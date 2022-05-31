package an.xuzaixiang.camera.features.autofocus;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;


public class AutoFocusFeature extends CameraFeature<FocusMode> {
  private FocusMode currentSetting = FocusMode.auto;

  private final boolean recordingVideo;

  public AutoFocusFeature(CameraProperties cameraProperties, boolean recordingVideo) {
    super(cameraProperties);
    this.recordingVideo = recordingVideo;
  }

  @Override
  public String getDebugName() {
    return "AutoFocusFeature";
  }

  @Override
  public FocusMode getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(FocusMode value) {
    this.currentSetting = value;
  }

  @Override
  public boolean checkIsSupported() {
    int[] modes = cameraProperties.getControlAutoFocusAvailableModes();

    final Float minFocus = cameraProperties.getLensInfoMinimumFocusDistance();

    boolean isFixedLength = minFocus == null || minFocus == 0;

    return !isFixedLength
        && !(modes.length == 0
            || (modes.length == 1 && modes[0] == CameraCharacteristics.CONTROL_AF_MODE_OFF));
  }

  @Override
  public void updateBuilder(CaptureRequest.Builder requestBuilder) {
    if (!checkIsSupported()) {
      return;
    }

    switch (currentSetting) {
      case locked:
        requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
        break;
      case auto:
        requestBuilder.set(
            CaptureRequest.CONTROL_AF_MODE,
            recordingVideo
                ? CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO
                : CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
      default:
        break;
    }
  }
}
