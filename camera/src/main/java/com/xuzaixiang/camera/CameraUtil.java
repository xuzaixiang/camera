package com.xuzaixiang.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;

import java.util.ArrayList;
import java.util.List;

public class CameraUtil {

    static CameraManager getCameraManager(Context context) {
        return (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public static List<CameraDescription> getAvailableCameras(Activity activity)
            throws CameraAccessException {
        CameraManager cameraManager = getCameraManager(activity);
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
