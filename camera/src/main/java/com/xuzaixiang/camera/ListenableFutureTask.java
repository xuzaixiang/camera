package com.xuzaixiang.camera;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V> {

    public interface ITask<V> {
        void get(V v);
    }

    public ListenableFutureTask(Callable<V> callable) {
        super(callable);
    }

    public ListenableFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    private final Object mLock = new Object();
    private final HashMap<Executor, ITask<V>> mCallable = new HashMap<>();
    private boolean mExecuted = false;

    @Override
    protected void done() {
        super.done();
        synchronized (mLock) {
            Iterator<Map.Entry<Executor, ITask<V>>> iterator = mCallable.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Executor, ITask<V>> entry = iterator.next();
                entry.getKey().execute(() -> executeTask(entry.getValue()));
                iterator.remove();
            }
        }
    }

    void executeTask(ITask<V> task) {
        V value;
        try {
            value = get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            value = null;
        }
        task.get(value);
    }

    public void addListener(@NonNull Executor executor, @NonNull ITask<V> task) {
        if (isDone()) {
            executor.execute(() -> executeTask(task));
            return;
        }
        synchronized (mLock) {
            mCallable.put(executor, task);
            if (!mExecuted) {
                new Thread(this).start();
                mExecuted = true;
            }
        }
    }
}
