package an.xuzaixiang.camera;

public interface CameraCallback {
    void onError(String description);
    void onClosing();
    void onInitialized();
}
