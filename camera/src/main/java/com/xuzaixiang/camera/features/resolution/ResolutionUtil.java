package com.xuzaixiang.camera.features.resolution;

import android.annotation.TargetApi;
import android.media.CamcorderProfile;
import android.media.EncoderProfiles;
import android.os.Build;

public class ResolutionUtil {

    public static CamcorderProfile getBestAvailableCamcorderProfileForResolutionPresetLegacy(
            int cameraId, ResolutionPreset preset) {
        if (cameraId < 0) {
            throw new AssertionError(
                    "getBestAvailableCamcorderProfileForResolutionPreset can only be used with valid (>=0) camera identifiers.");
        }

        switch (preset) {
            case max:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_HIGH)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
                }
            case ultraHigh:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_2160P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_2160P);
                }
            case veryHigh:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P);
                }
            case high:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P);
                }
            case medium:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P);
                }
            case low:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA);
                }
            default:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
                } else {
                    throw new IllegalArgumentException(
                            "No capture session available for current capture session.");
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.S)
    public static EncoderProfiles getBestAvailableCamcorderProfileForResolutionPreset(
            int cameraId, ResolutionPreset preset) {
        if (cameraId < 0) {
            throw new AssertionError(
                    "getBestAvailableCamcorderProfileForResolutionPreset can only be used with valid (>=0) camera identifiers.");
        }
        String cameraIdString = Integer.toString(cameraId);

        switch (preset) {
            case max:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_HIGH)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_HIGH);
                }
            case ultraHigh:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_2160P)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_2160P);
                }
            case veryHigh:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_1080P);
                }
            case high:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_720P);
                }
            case medium:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_480P);
                }
            case low:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_QVGA);
                }
            default:
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                    return CamcorderProfile.getAll(cameraIdString, CamcorderProfile.QUALITY_LOW);
                }
                throw new IllegalArgumentException(
                        "No capture session available for current capture session.");
        }
    }
}
