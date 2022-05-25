package com.xuzaixiang.camera.features;

import androidx.annotation.NonNull;

import com.xuzaixiang.camera.CameraProperties;
import com.xuzaixiang.camera.features.autofocus.AutoFocusFeature;
import com.xuzaixiang.camera.features.exposurelock.ExposureLockFeature;
import com.xuzaixiang.camera.features.exposureoffset.ExposureOffsetFeature;
import com.xuzaixiang.camera.features.flash.FlashFeature;
import com.xuzaixiang.camera.features.resolution.ResolutionFeature;

public interface CameraFeatureFactory {
    AutoFocusFeature createAutoFocusFeature(
            @NonNull CameraProperties cameraProperties, boolean recordingVideo);

    ExposureLockFeature createExposureLockFeature(@NonNull CameraProperties cameraProperties);

    ExposureOffsetFeature createExposureOffsetFeature(@NonNull CameraProperties cameraProperties);

    FlashFeature createFlashFeature(@NonNull CameraProperties cameraProperties);
//
//    ResolutionFeature createResolutionFeature(
//            @NonNull CameraProperties cameraProperties,
//            ResolutionPreset initialSetting,
//            String cameraName);
//
//    FocusPointFeature createFocusPointFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull SensorOrientationFeature sensorOrientationFeature);
//
//    FpsRangeFeature createFpsRangeFeature(@NonNull CameraProperties cameraProperties);
//
//    SensorOrientationFeature createSensorOrientationFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull Activity activity,
//            @NonNull DartMessenger dartMessenger);
//
//    ZoomLevelFeature createZoomLevelFeature(@NonNull CameraProperties cameraProperties);
//
//    ExposurePointFeature createExposurePointFeature(
//            @NonNull CameraProperties cameraProperties,
//            @NonNull SensorOrientationFeature sensorOrientationFeature);
//
//    NoiseReductionFeature createNoiseReductionFeature(@NonNull CameraProperties cameraProperties);
}
