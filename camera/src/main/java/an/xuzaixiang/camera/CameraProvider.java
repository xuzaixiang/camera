package an.xuzaixiang.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CameraProvider {

    private final ArrayList<CameraDescription> mCameraDescription = new ArrayList<>();
    private final Object mLock = new Object();

    private Context mContext;
    private CameraManager mCameraManager;
    private ListenableFutureTask<CameraProvider> mTask;

    @SuppressLint("StaticFieldLeak")
    private static final CameraProvider sProvider = new CameraProvider();

    public List<CameraDescription> getCameraDescription() {
        return mCameraDescription;
    }

    @Nullable
    public Camera newCamera(@NonNull String cameraName) {
        if (mCameraDescription.isEmpty()) {
            return null;
        }
        for (CameraDescription description : mCameraDescription) {
            if (Objects.equals(description.getName(), cameraName)) {
                return newCamera(description);
            }
        }
        return null;
    }

    @Nullable
    public Camera newCamera(@NonNull CameraLensDirection direction) {
        if (mCameraDescription.isEmpty()) {
            return null;
        }
        for (CameraDescription description : mCameraDescription) {
            if (description.getLensDirection() == direction) {
                return newCamera(description);
            }
        }
        return null;
    }

    @Nullable
    public Camera newCamera(@NonNull CameraDescription description) {
        Camera camera;
        try {
            camera = new Camera(mContext, mCameraManager, description);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            camera = null;
        }
        return camera;
    }

    @NonNull
    public static ListenableFutureTask<CameraProvider> getInstance(@NonNull Context context) {
        return sProvider.getProvider(context.getApplicationContext());
    }

    ListenableFutureTask<CameraProvider> getProvider(Context context) {
        mContext = context;
        synchronized (mLock) {
            if (mTask != null) {
                return mTask;
            }
            if (mCameraManager == null) {
                mCameraManager = CameraUtil.getCameraManager(context);
            }
            mTask = new ListenableFutureTask<>(() -> {
                try {
                    List<CameraDescription> descriptions = CameraUtil.getAvailableCameras(mCameraManager);
                    if (descriptions.isEmpty()) {
                        return null;
                    }
                    sProvider.setCameraDescription(descriptions);
                    return sProvider;
                } catch (Exception e) {
                    return null;
                }
            });
            return mTask;
        }
    }

    void setCameraDescription(List<CameraDescription> descriptions) {
        mCameraDescription.clear();
        mCameraDescription.addAll(descriptions);
    }
}
