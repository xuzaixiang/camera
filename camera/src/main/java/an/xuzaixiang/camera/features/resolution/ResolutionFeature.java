package an.xuzaixiang.camera.features.resolution;

import android.hardware.camera2.CaptureRequest;
import android.media.CamcorderProfile;
import android.media.EncoderProfiles;
import android.os.Build;
import android.util.Size;

import androidx.annotation.NonNull;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;

import java.util.List;

public class ResolutionFeature extends CameraFeature<ResolutionPreset> {
    private Size captureSize;
    private Size previewSize;
    private CamcorderProfile recordingProfileLegacy;
    private EncoderProfiles recordingProfile;

    private ResolutionPreset currentSetting;
    private int cameraId;

    public ResolutionFeature(
            CameraProperties cameraProperties, ResolutionPreset resolutionPreset, String cameraName) {
        super(cameraProperties);
        currentSetting = resolutionPreset;
        try {
            cameraId = Integer.parseInt(cameraName, 10);
        } catch (NumberFormatException e) {
            cameraId = -1;
            return;
        }
        configureResolution(resolutionPreset, cameraId);
    }

    public CamcorderProfile getRecordingProfileLegacy() {
        return this.recordingProfileLegacy;
    }

    public EncoderProfiles getRecordingProfile() {
        return this.recordingProfile;
    }

    public Size getPreviewSize() {
        return this.previewSize;
    }

    public Size getCaptureSize() {
        return this.captureSize;
    }

    @Override
    public String getDebugName() {
        return "ResolutionFeature";
    }

    @Override
    public ResolutionPreset getValue() {
        return this.currentSetting;
    }

    @Override
    public void setValue(ResolutionPreset value) {
        this.currentSetting = value;
        configureResolution(currentSetting, cameraId);
    }

    @Override
    public boolean checkIsSupported() {
        return cameraId >= 0;
    }

    @Override
    public void updateBuilder(CaptureRequest.Builder requestBuilder) {
        // No-op: when setting a resolution there is no need to update the request builder.
    }

    private void configureResolution(ResolutionPreset resolutionPreset, int cameraId)
            throws IndexOutOfBoundsException {
        if (!checkIsSupported()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 31) {
            recordingProfile = ResolutionUtil.getBestAvailableCamcorderProfileForResolutionPreset(cameraId, resolutionPreset);
            List<EncoderProfiles.VideoProfile> videoProfiles = recordingProfile.getVideoProfiles();
            EncoderProfiles.VideoProfile defaultVideoProfile = videoProfiles.get(0);
            captureSize = new Size(defaultVideoProfile.getWidth(), defaultVideoProfile.getHeight());
        } else {
            recordingProfileLegacy = ResolutionUtil.getBestAvailableCamcorderProfileForResolutionPresetLegacy(cameraId, resolutionPreset);
            captureSize = new Size(recordingProfileLegacy.videoFrameWidth, recordingProfileLegacy.videoFrameHeight);
        }
        previewSize = computeBestPreviewSize(cameraId, resolutionPreset);
    }

    @NonNull
    static Size computeBestPreviewSize(int cameraId, @NonNull ResolutionPreset preset)
            throws IndexOutOfBoundsException {
        if (preset.ordinal() > ResolutionPreset.high.ordinal()) {
            preset = ResolutionPreset.high;
        }
        if (Build.VERSION.SDK_INT >= 31) {
            EncoderProfiles profile = ResolutionUtil.getBestAvailableCamcorderProfileForResolutionPreset(cameraId, preset);
            List<EncoderProfiles.VideoProfile> videoProfiles = profile.getVideoProfiles();
            EncoderProfiles.VideoProfile defaultVideoProfile = videoProfiles.get(0);
            return new Size(defaultVideoProfile.getWidth(), defaultVideoProfile.getHeight());
        } else {
            CamcorderProfile profile = ResolutionUtil.getBestAvailableCamcorderProfileForResolutionPresetLegacy(cameraId, preset);
            return new Size(profile.videoFrameWidth, profile.videoFrameHeight);
        }
    }
}
