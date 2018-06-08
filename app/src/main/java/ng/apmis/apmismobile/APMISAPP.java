package ng.apmis.apmismobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class APMISAPP {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static APMISAPP sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private RequestQueue queue;

    private APMISAPP(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static APMISAPP getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new APMISAPP(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }


    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }



}
