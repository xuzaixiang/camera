package an.xuzaixiang.camera;

public interface ResultCallback {
    void onSuccess();

    void onError(String errCode, String errDescription);
}
