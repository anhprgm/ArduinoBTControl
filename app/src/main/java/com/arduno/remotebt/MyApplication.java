package com.arduno.remotebt;

import android.app.Application;

public class MyApplication extends Application
{
    private static MyApplication sInstance;

    public static MyApplication getApplication() {
        return sInstance;
    }

    public  void setupConnectedThread() {
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}