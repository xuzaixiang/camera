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

public interface CameraFeatureFactory {
    AutoFocusFeature createAutoFocusFeature(
            @NonNull CameraProperties cameraProperties, boolean recordingVideo);

    ExposureLockFeature createExposureLockFeature(@NonNull CameraProperties cameraProperties);

    ExposureOffsetFeature createExposureOffsetFeature(@NonNull CameraProperties cameraProperties);

    FlashFeature createFlashFeature(@NonNull CameraProperties cameraProperties);

    ResolutionFeature createResolutionFeature(
            @NonNull CameraProperties cameraProperties,
            ResolutionPreset initialSetting,
            String cameraName);
//
//    FocusPointFeature createFocusPointFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull SensorOrientationFeature sensorOrientationFeature);
//
    FpsRangeFeature createFpsRangeFeature(@NonNull CameraProperties cameraProperties);
//
//    SensorOrientationFeature createSensorOrientationFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull Activity activity,
//            @NonNull DartMessenger dartMessenger);
//
    ZoomLevelFeature createZoomLevelFeature(@NonNull CameraProperties cameraProperties);

//    ExposurePointFeature createExposurePointFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull SensorOrientationFeature sensorOrientationFeature);
//
    NoiseReductionFeature createNoiseReductionFeature(@NonNull CameraProperties cameraProperties);
}
