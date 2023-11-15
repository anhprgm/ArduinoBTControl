package com.arduno.remotebt;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import com.arduno.remotebt.core.ConnectedThread;

public class MyApplication extends Application
{
    private static MyApplication sInstance;
    ConnectedThread connectedThread = null;
    BluetoothSocket bluetoothSocket = null;

    public static MyApplication getApplication() {
        return sInstance;
    }

    public  void setupConnectedThread() {
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public void setupConnectedThread(ConnectedThread connectedThread)
    {
        this.connectedThread=connectedThread;
    }

    public ConnectedThread getCurrentConnectedThread()
    {
        return connectedThread;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }
}