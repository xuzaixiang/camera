// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package an.xuzaixiang.camera.features.noisereduction;

import android.hardware.camera2.CaptureRequest;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import an.xuzaixiang.camera.CameraProperties;
import an.xuzaixiang.camera.features.CameraFeature;

import java.util.HashMap;

public class NoiseReductionFeature extends CameraFeature<NoiseReductionMode> {
  private NoiseReductionMode currentSetting = NoiseReductionMode.fast;

  private final HashMap<NoiseReductionMode, Integer> NOISE_REDUCTION_MODES = new HashMap<>();

  public NoiseReductionFeature(CameraProperties cameraProperties) {
    super(cameraProperties);
    NOISE_REDUCTION_MODES.put(NoiseReductionMode.off, CaptureRequest.NOISE_REDUCTION_MODE_OFF);
    NOISE_REDUCTION_MODES.put(NoiseReductionMode.fast, CaptureRequest.NOISE_REDUCTION_MODE_FAST);
    NOISE_REDUCTION_MODES.put(
        NoiseReductionMode.highQuality, CaptureRequest.NOISE_REDUCTION_MODE_HIGH_QUALITY);
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      NOISE_REDUCTION_MODES.put(
          NoiseReductionMode.minimal, CaptureRequest.NOISE_REDUCTION_MODE_MINIMAL);
      NOISE_REDUCTION_MODES.put(
          NoiseReductionMode.zeroShutterLag, CaptureRequest.NOISE_REDUCTION_MODE_ZERO_SHUTTER_LAG);
    }
  }

  @Override
  public String getDebugName() {
    return "NoiseReductionFeature";
  }

  @Override
  public NoiseReductionMode getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(NoiseReductionMode value) {
    this.currentSetting = value;
  }

  @Override
  public boolean checkIsSupported() {
    int[] modes = cameraProperties.getAvailableNoiseReductionModes();

    return modes != null && modes.length > 0;
  }

  @Override
  public void updateBuilder(CaptureRequest.Builder requestBuilder) {
    if (!checkIsSupported()) {
      return;
    }

    Log.i("Camera", "updateNoiseReduction | currentSetting: " + currentSetting);

    requestBuilder.set(
        CaptureRequest.NOISE_REDUCTION_MODE, NOISE_REDUCTION_MODES.get(currentSetting));
  }
}
