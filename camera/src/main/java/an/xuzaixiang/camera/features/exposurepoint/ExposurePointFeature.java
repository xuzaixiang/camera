package an.xuzaixiang.camera.features.exposurepoint;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.util.Size;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;
import an.xuzaixiang.camera.features.Point;
//import com.xuzaixiang.camera.features.sensororientation.SensorOrientationFeature;

public class ExposurePointFeature extends CameraFeature<Point> {

    private Size cameraBoundaries;
    private Point exposurePoint;
    private MeteringRectangle exposureRectangle;

    //  private final SensorOrientationFeature sensorOrientationFeature;
    public ExposurePointFeature(
            CameraProperties cameraProperties) {
        super(cameraProperties);
    }
//  public ExposurePointFeature(
//          CameraProperties cameraProperties, SensorOrientationFeature sensorOrientationFeature) {
//    super(cameraProperties);
//    this.sensorOrientationFeature = sensorOrientationFeature;
//  }

    public void setCameraBoundaries(@NonNull Size cameraBoundaries) {
        this.cameraBoundaries = cameraBoundaries;
        this.buildExposureRectangle();
    }

    @Override
    public String getDebugName() {
        return "ExposurePointFeature";
    }

    @Override
    public Point getValue() {
        return exposurePoint;
    }

    @Override
    public void setValue(Point value) {
        this.exposurePoint = (value == null || value.x == null || value.y == null) ? null : value;
        this.buildExposureRectangle();
    }

    @Override
    public boolean checkIsSupported() {
        Integer supportedRegions = cameraProperties.getControlMaxRegionsAutoExposure();
        return supportedRegions != null && supportedRegions > 0;
    }

    @Override
    public void updateBuilder(CaptureRequest.Builder requestBuilder) {
        if (!checkIsSupported()) {
            return;
        }
        requestBuilder.set(
                CaptureRequest.CONTROL_AE_REGIONS,
                exposureRectangle == null ? null : new MeteringRectangle[]{exposureRectangle});
    }

    private void buildExposureRectangle() {
        if (this.cameraBoundaries == null) {
            throw new AssertionError(
                    "The cameraBoundaries should be set (using `ExposurePointFeature.setCameraBoundaries(Size)`) before updating the exposure point.");
        }
        if (this.exposurePoint == null) {
            this.exposureRectangle = null;
        } else {
            // todo
//      PlatformChannel.DeviceOrientation orientation =
//          this.sensorOrientationFeature.getLockedCaptureOrientation();
//      if (orientation == null) {
//        orientation =
//            this.sensorOrientationFeature.getDeviceOrientationManager().getLastUIOrientation();
//      }
//      this.exposureRectangle =
//          CameraRegionUtils.convertPointToMeteringRectangle(
//              this.cameraBoundaries, this.exposurePoint.x, this.exposurePoint.y, orientation);
        }
    }
}
