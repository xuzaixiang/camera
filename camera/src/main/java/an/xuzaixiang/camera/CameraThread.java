package an.xuzaixiang.camera;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Executor;

public class CameraThread implements Executor {
    private final Handler mHandler;
    private final HandlerThread mThread;

    public CameraThread() {
        mThread = new HandlerThread("CameraThread");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());
    }

    public void stop() {
        mThread.quitSafely();
        try {
            mThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    public HandlerThread getThread() {
        return mThread;
    }

    @Override
    public void execute(Runnable command) {
        mHandler.post(command);
    }
}
