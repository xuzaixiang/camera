package com.xuzaixiang.camera.features;

import android.content.Context;

import com.xuzaixiang.camera.CameraProperties;
import com.xuzaixiang.camera.features.autofocus.AutoFocusFeature;
import com.xuzaixiang.camera.features.exposurelock.ExposureLockFeature;
import com.xuzaixiang.camera.features.exposureoffset.ExposureOffsetFeature;
import com.xuzaixiang.camera.features.exposurepoint.ExposurePointFeature;
import com.xuzaixiang.camera.features.flash.FlashFeature;
import com.xuzaixiang.camera.features.focuspoint.FocusPointFeature;
import com.xuzaixiang.camera.features.fpsrange.FpsRangeFeature;
import com.xuzaixiang.camera.features.noisereduction.NoiseReductionFeature;
import com.xuzaixiang.camera.features.resolution.ResolutionFeature;
import com.xuzaixiang.camera.features.resolution.ResolutionPreset;
import com.xuzaixiang.camera.features.zoomlevel.ZoomLevelFeature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CameraFeatures {
    private static final String AUTO_FOCUS = "AUTO_FOCUS";
    private static final String EXPOSURE_LOCK = "EXPOSURE_LOCK";
    private static final String EXPOSURE_OFFSET = "EXPOSURE_OFFSET";
    private static final String EXPOSURE_POINT = "EXPOSURE_POINT";
    private static final String FLASH = "FLASH";
    private static final String FOCUS_POINT = "FOCUS_POINT";
    private static final String FPS_RANGE = "FPS_RANGE";
    private static final String NOISE_REDUCTION = "NOISE_REDUCTION";
    private static final String REGION_BOUNDARIES = "REGION_BOUNDARIES";
    private static final String RESOLUTION = "RESOLUTION";
    private static final String SENSOR_ORIENTATION = "SENSOR_ORIENTATION";
    private static final String ZOOM_LEVEL = "ZOOM_LEVEL";

    public static CameraFeatures init(
            CameraFeatureFactory cameraFeatureFactory,
            CameraProperties cameraProperties,
            Context context,
//      DartMessenger dartMessenger,
            ResolutionPreset resolutionPreset) {
        CameraFeatures cameraFeatures = new CameraFeatures();
        cameraFeatures.setAutoFocus(
                cameraFeatureFactory.createAutoFocusFeature(cameraProperties, false));
        cameraFeatures.setExposureLock(
                cameraFeatureFactory.createExposureLockFeature(cameraProperties));
        cameraFeatures.setExposureOffset(
                cameraFeatureFactory.createExposureOffsetFeature(cameraProperties));
//    SensorOrientationFeature sensorOrientationFeature =
//        cameraFeatureFactory.createSensorOrientationFeature(
//            cameraProperties, activity, dartMessenger);
//    cameraFeatures.setSensorOrientation(sensorOrientationFeature);
//    cameraFeatures.setExposurePoint(
//        cameraFeatureFactory.createExposurePointFeature(
//            cameraProperties, sensorOrientationFeature));
        cameraFeatures.setFlash(cameraFeatureFactory.createFlashFeature(cameraProperties));
//    cameraFeatures.setFocusPoint(
//        cameraFeatureFactory.createFocusPointFeature(cameraProperties, sensorOrientationFeature));
        cameraFeatures.setFpsRange(cameraFeatureFactory.createFpsRangeFeature(cameraProperties));
        cameraFeatures.setNoiseReduction(
                cameraFeatureFactory.createNoiseReductionFeature(cameraProperties));
        cameraFeatures.setResolution(
                cameraFeatureFactory.createResolutionFeature(
                        cameraProperties, resolutionPreset, cameraProperties.getCameraName()));
        cameraFeatures.setZoomLevel(cameraFeatureFactory.createZoomLevelFeature(cameraProperties));
        return cameraFeatures;
    }

    private Map<String, CameraFeature> featureMap = new HashMap<>();

    public Collection<CameraFeature> getAllFeatures() {
        return this.featureMap.values();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AutoFocusFeature getAutoFocus() {
        return (AutoFocusFeature) featureMap.get(AUTO_FOCUS);
    }

    public void setAutoFocus(AutoFocusFeature autoFocus) {
        this.featureMap.put(AUTO_FOCUS, autoFocus);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ExposureLockFeature getExposureLock() {
        return (ExposureLockFeature) featureMap.get(EXPOSURE_LOCK);
    }

    public void setExposureLock(ExposureLockFeature exposureLock) {
        this.featureMap.put(EXPOSURE_LOCK, exposureLock);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ExposureOffsetFeature getExposureOffset() {
        return (ExposureOffsetFeature) featureMap.get(EXPOSURE_OFFSET);
    }

    public void setExposureOffset(ExposureOffsetFeature exposureOffset) {
        this.featureMap.put(EXPOSURE_OFFSET, exposureOffset);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ExposurePointFeature getExposurePoint() {
        return (ExposurePointFeature) featureMap.get(EXPOSURE_POINT);
    }

    public void setExposurePoint(ExposurePointFeature exposurePoint) {
        this.featureMap.put(EXPOSURE_POINT, exposurePoint);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public FlashFeature getFlash() {
        return (FlashFeature) featureMap.get(FLASH);
    }

    public void setFlash(FlashFeature flash) {
        this.featureMap.put(FLASH, flash);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public FocusPointFeature getFocusPoint() {
        return (FocusPointFeature) featureMap.get(FOCUS_POINT);
    }

    public void setFocusPoint(FocusPointFeature focusPoint) {
        this.featureMap.put(FOCUS_POINT, focusPoint);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public FpsRangeFeature getFpsRange() {
        return (FpsRangeFeature) featureMap.get(FPS_RANGE);
    }

    public void setFpsRange(FpsRangeFeature fpsRange) {
        this.featureMap.put(FPS_RANGE, fpsRange);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public NoiseReductionFeature getNoiseReduction() {
        return (NoiseReductionFeature) featureMap.get(NOISE_REDUCTION);
    }

    public void setNoiseReduction(NoiseReductionFeature noiseReduction) {
        this.featureMap.put(NOISE_REDUCTION, noiseReduction);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ResolutionFeature getResolution() {
        return (ResolutionFeature) featureMap.get(RESOLUTION);
    }

    public void setResolution(ResolutionFeature resolution) {
        featureMap.put(RESOLUTION, resolution);
    }

//  public SensorOrientationFeature getSensorOrientation() {
//    return (SensorOrientationFeature) featureMap.get(SENSOR_ORIENTATION);
//  }

//  public void setSensorOrientation(SensorOrientationFeature sensorOrientation) {
//    this.featureMap.put(SENSOR_ORIENTATION, sensorOrientation);
//  }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ZoomLevelFeature getZoomLevel() {
        return (ZoomLevelFeature) featureMap.get(ZOOM_LEVEL);
    }

    public void setZoomLevel(ZoomLevelFeature zoomLevel) {
        this.featureMap.put(ZOOM_LEVEL, zoomLevel);
    }
}
