package ng.apmis.apmismobile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import co.paystack.android.PaystackSdk;
import ng.apmis.apmismobile.utilities.NotificationUtils;

public class APMISAPP extends Application{

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static APMISAPP sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private RequestQueue queue;
    private static Context mContext;

    private String TAG = "APMIS";

    public APMISAPP(){
        this.diskIO = null;
        this.networkIO = null;
        this.mainThread = null;
    }

    private APMISAPP(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        PaystackSdk.initialize(getApplicationContext());
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new APMISAPP(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationUtils.createNotificationChannel(this);
            }
        }
    }

    public static APMISAPP getInstance(){
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(mContext);
        }

        return queue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (queue != null) {
            queue.cancelAll(tag);
        }
    }

    public Executor diskIO(){
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
