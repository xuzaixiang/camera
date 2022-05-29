package com.xuzaixiang.camera;

import com.xuzaixiang.camera.SurfaceRequest;

public interface SurfaceProvider {
    void onSurfaceRequested(SurfaceRequest request);
}
