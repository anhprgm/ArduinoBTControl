package com.arduno.remotebt;

import android.bluetooth.BluetoothSocket;

import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.viewmodel.MyViewModel;

import java.io.InputStream;

public class MySingleton {

    private static final MySingleton ourInstance = new MySingleton();

    private InputStream mmInputStream;
    private MyViewModel viewModel;

    private ConnectedThread connectedThread;
    private BluetoothSocket bluetoothSocket;

    public static MySingleton getInstance() {
        return ourInstance;
    }

    private MySingleton() {
    }
    public InputStream getMmInputStream() {
        return mmInputStream;
    }

    public void setMmInputStream(InputStream mmInputStream) {
        this.mmInputStream = mmInputStream;
    }

    public MyViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ConnectedThread getConnectedThread() {
        return connectedThread;
    }

    public void setConnectedThread(ConnectedThread connectedThread) {
        this.connectedThread = connectedThread;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }
}
