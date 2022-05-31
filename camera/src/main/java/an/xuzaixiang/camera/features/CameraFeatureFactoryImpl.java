package an.xuzaixiang.camera.features;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.autofocus.AutoFocusFeature;
import an.xuzaixiang.camera.features.exposurelock.ExposureLockFeature;
import an.xuzaixiang.camera.features.exposureoffset.ExposureOffsetFeature;
import an.xuzaixiang.camera.features.flash.FlashFeature;
import an.xuzaixiang.camera.features.fpsrange.FpsRangeFeature;
import an.xuzaixiang.camera.features.noisereduction.NoiseReductionFeature;
import an.xuzaixiang.camera.features.resolution.ResolutionFeature;
import an.xuzaixiang.camera.features.resolution.ResolutionPreset;
import an.xuzaixiang.camera.features.zoomlevel.ZoomLevelFeature;


public class CameraFeatureFactoryImpl implements CameraFeatureFactory {

  @Override
  public AutoFocusFeature createAutoFocusFeature(
          @NonNull CameraProperties cameraProperties, boolean recordingVideo) {
    return new AutoFocusFeature(cameraProperties, recordingVideo);
  }

  @Override
  public ExposureLockFeature createExposureLockFeature(@NonNull CameraProperties cameraProperties) {
    return new ExposureLockFeature(cameraProperties);
  }

  @Override
  public ExposureOffsetFeature createExposureOffsetFeature(
      @NonNull CameraProperties cameraProperties) {
    return new ExposureOffsetFeature(cameraProperties);
  }

  @Override
  public FlashFeature createFlashFeature(@NonNull CameraProperties cameraProperties) {
    return new FlashFeature(cameraProperties);
  }

  @Override
  public ResolutionFeature createResolutionFeature(
      @NonNull CameraProperties cameraProperties,
      ResolutionPreset initialSetting,
      String cameraName) {
    return new ResolutionFeature(cameraProperties, initialSetting, cameraName);
  }
//
//  @Override
//  public FocusPointFeature createFocusPointFeature(
//      @NonNull CameraProperties cameraProperties,
//      @NonNull SensorOrientationFeature sensorOrientationFeature) {
//    return new FocusPointFeature(cameraProperties, sensorOrientationFeature);
//  }
//
  @Override
  public FpsRangeFeature createFpsRangeFeature(@NonNull CameraProperties cameraProperties) {
    return new FpsRangeFeature(cameraProperties);
  }
//
//  @Override
//  public SensorOrientationFeature createSensorOrientationFeature(
//      @NonNull CameraProperties cameraProperties,
//      @NonNull Activity activity,
//      @NonNull DartMessenger dartMessenger) {
//    return new SensorOrientationFeature(cameraProperties, activity, dartMessenger);
//  }
//
  @Override
  public ZoomLevelFeature createZoomLevelFeature(@NonNull CameraProperties cameraProperties) {
    return new ZoomLevelFeature(cameraProperties);
  }
//
//  @Override
//  public ExposurePointFeature createExposurePointFeature(
//      @NonNull CameraProperties cameraProperties,
//      @NonNull SensorOrientationFeature sensorOrientationFeature) {
//    return new ExposurePointFeature(cameraProperties, sensorOrientationFeature);
//  }
//
  @Override
  public NoiseReductionFeature createNoiseReductionFeature(
      @NonNull CameraProperties cameraProperties) {
    return new NoiseReductionFeature(cameraProperties);
  }
}
