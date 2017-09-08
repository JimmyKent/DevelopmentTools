package com.jimmy.development.tools;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class ThreadUtils {
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());

    public static boolean isMainThread() {
        return Thread.currentThread().equals(Looper.getMainLooper().getThread());
    }

    public static void doInBackground(Runnable runnable) {
        doInBackground(runnable, 0);
    }

    public static void doInBackground(final Runnable runnable, final long delayMillis) {
        Runnable jobTask = new Runnable() {
            @Override
            public void run() {
                if (delayMillis > 0) {
                    try {
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException e) {
                        Log.w("ThreadUtils", e);
                    }
                }

                runnable.run();
            }
        };

        if (isMainThread()) {
            new Thread(jobTask).start();
            return;
        }

        jobTask.run();
    }

    public static void doOnMainThread(Runnable runnable) {
        doOnMainThread(runnable, 0);
    }

    public static void doOnMainThread(Runnable runnable, long delayMillis) {
        if (isMainThread() && delayMillis <= 0) {
            runnable.run();
            return;
        }

        sUiHandler.postDelayed(runnable, delayMillis);
    }
}
