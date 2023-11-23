package com.arduno.remotebt.viewmodel;


import android.bluetooth.BluetoothDevice;

import java.util.Set;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    // Declare LiveData
    public MutableLiveData<String> message = new MutableLiveData<>();


    public MutableLiveData<Set<BluetoothDevice>> listDevices = new MutableLiveData<>();

    // Rest of your ViewModel code...
}

