package com.arduno.remotebt.viewmodel;


import android.app.Application;
import android.bluetooth.BluetoothDevice;

import com.arduno.remotebt.database.Database;
import com.arduno.remotebt.database.DatabaseManager;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.database.Repository;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    // Declare LiveData
    public MutableLiveData<String> message = new MutableLiveData<>();


    public MutableLiveData<Set<BluetoothDevice>> listDevices = new MutableLiveData<>();

    public MutableLiveData<List<RemoteControl>> listDevice = new MutableLiveData<>();

    private Repository repository;

    public MyViewModel(Application application) {
        this.repository = new DatabaseManager(Database.Companion.getInstance(application));
    }
    public void getListRemoteControl() {
      repository.getLiveData().observeForever(remoteControls -> listDevice.postValue(remoteControls));
    }
    public void insertRemoteControl(RemoteControl remoteControl) {
        repository.insert(remoteControl);
    }
    public RemoteControl getRemoteControlById(int id) {
        return repository.getById(id);
    }
}

