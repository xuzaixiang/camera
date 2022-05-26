package com.xuzaixiang.camera;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V> {

    public ListenableFutureTask(Callable<V> callable) {
        super(callable);
    }

    public ListenableFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    private Callable<V> mCallable;

    @Override
    protected void done() {
        super.done();
        if (mCallable != null) {
            try {
                mCallable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
