package an.xuzaixiang.camera.features;

import android.os.Handler;
import android.os.Looper;


public class HandlerFactory {
    public static Handler create(Looper looper) {
        return new Handler(looper);
    }
}