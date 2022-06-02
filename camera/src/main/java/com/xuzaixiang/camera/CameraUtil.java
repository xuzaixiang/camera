package com.xuzaixiang.camera;

import static android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DEVICE;
import static android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DISABLED;
import static android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_IN_USE;
import static android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_SERVICE;
import static android.hardware.camera2.CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE;


import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class CameraUtil {
    static String getCameraErrorDescription(int error) {
        String errorDescription;
        switch (error) {
            case ERROR_CAMERA_IN_USE:
                errorDescription = "The camera device is in use already.";
                break;
            case ERROR_MAX_CAMERAS_IN_USE:
                errorDescription = "Max cameras in use";
                break;
            case ERROR_CAMERA_DISABLED:
                errorDescription = "The camera device could not be opened due to a device policy.";
                break;
            case ERROR_CAMERA_DEVICE:
                errorDescription = "The camera device has encountered a fatal error";
                break;
            case ERROR_CAMERA_SERVICE:
                errorDescription = "The camera service has encountered a fatal error.";
                break;
            default:
                errorDescription = "Unknown camera error";
        }
        return errorDescription;
    }

    static CameraManager getCameraManager(@NonNull Context context) {
        return (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    @NonNull
    static List<CameraDescription> getAvailableCameras(Context context) throws CameraAccessException {
        return getAvailableCameras(getCameraManager(context));
    }

    @NonNull
    static List<CameraDescription> getAvailableCameras(@NonNull CameraManager cameraManager)
            throws CameraAccessException {
        String[] cameraNames = cameraManager.getCameraIdList();
        List<CameraDescription> cameras = new ArrayList<>();
        for (String cameraName : cameraNames) {
            int cameraId;
            try {
                cameraId = Integer.parseInt(cameraName, 10);
            } catch (NumberFormatException e) {
                cameraId = -1;
            }
            if (cameraId < 0)
                continue;
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraName);
            int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            int lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
            CameraLensDirection direction = CameraLensDirection.external;
            if (lensFacing == CameraMetadata.LENS_FACING_FRONT) {
                direction = CameraLensDirection.front;
            } else if (lensFacing == CameraMetadata.LENS_FACING_BACK) {
                direction = CameraLensDirection.back;
            }
            cameras.add(new CameraDescription(cameraName, sensorOrientation, direction));
        }
        return cameras;
    }
}
