package com.binding.model.cycle;

import android.os.Handler;
import android.os.Looper;

public class MainLooper extends Handler {
    private static MainLooper instance = new MainLooper(Looper.getMainLooper());

    protected MainLooper(Looper looper) {
        super(looper);
    }

    public static MainLooper getInstance() {
        return instance;
    }

    public static void runOnUiThread(Runnable runnable) {
        if (isUiThread()) {
            runnable.run();
        } else {
            instance.post(runnable);
        }
    }

    public static boolean isUiThread() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }
}