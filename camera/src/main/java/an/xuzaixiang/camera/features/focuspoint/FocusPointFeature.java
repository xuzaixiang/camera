// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package an.xuzaixiang.camera.features.focuspoint;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.util.Size;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;
import an.xuzaixiang.camera.features.Point;
//import com.xuzaixiang.camera.features.sensororientation.SensorOrientationFeature;

public class FocusPointFeature extends CameraFeature<Point> {

    private Size cameraBoundaries;
    private Point focusPoint;
    private MeteringRectangle focusRectangle;

    //  private final SensorOrientationFeature sensorOrientationFeature;
    public FocusPointFeature(
            CameraProperties cameraProperties) {
        super(cameraProperties);
    }

//  public FocusPointFeature(
//          CameraProperties cameraProperties, SensorOrientationFeature sensorOrientationFeature) {
//    super(cameraProperties);
//    this.sensorOrientationFeature = sensorOrientationFeature;
//  }

    public void setCameraBoundaries(@NonNull Size cameraBoundaries) {
        this.cameraBoundaries = cameraBoundaries;
        this.buildFocusRectangle();
    }

    @Override
    public String getDebugName() {
        return "FocusPointFeature";
    }

    @Override
    public Point getValue() {
        return focusPoint;
    }

    @Override
    public void setValue(Point value) {
        this.focusPoint = value == null || value.x == null || value.y == null ? null : value;
        this.buildFocusRectangle();
    }

    @Override
    public boolean checkIsSupported() {
        Integer supportedRegions = cameraProperties.getControlMaxRegionsAutoFocus();
        return supportedRegions != null && supportedRegions > 0;
    }

    @Override
    public void updateBuilder(CaptureRequest.Builder requestBuilder) {
        if (!checkIsSupported()) {
            return;
        }
        requestBuilder.set(
                CaptureRequest.CONTROL_AF_REGIONS,
                focusRectangle == null ? null : new MeteringRectangle[]{focusRectangle});
    }

    private void buildFocusRectangle() {
        if (this.cameraBoundaries == null) {
            throw new AssertionError(
                    "The cameraBoundaries should be set (using `FocusPointFeature.setCameraBoundaries(Size)`) before updating the focus point.");
        }
        if (this.focusPoint == null) {
            this.focusRectangle = null;
        } else {
            // todo
//      PlatformChannel.DeviceOrientation orientation =
//          this.sensorOrientationFeature.getLockedCaptureOrientation();
//      if (orientation == null) {
//        orientation =
//            this.sensorOrientationFeature.getDeviceOrientationManager().getLastUIOrientation();
//      }
//      this.focusRectangle =
//          CameraRegionUtils.convertPointToMeteringRectangle(
//              this.cameraBoundaries, this.focusPoint.x, this.focusPoint.y, orientation);
        }
    }
}
