package com.arduno.remotebt.viewmodel;


import android.app.Application;
import android.bluetooth.BluetoothDevice;

import com.arduno.remotebt.activity.Mode;
import com.arduno.remotebt.database.DataModel;
import com.arduno.remotebt.database.Database;
import com.arduno.remotebt.database.DatabaseManager;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.database.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    public MutableLiveData<String> message = new MutableLiveData<>();

    public MutableLiveData<Set<BluetoothDevice>> listDevices = new MutableLiveData<>();

    public MutableLiveData<List<RemoteControl>> listRemoteControl = new MutableLiveData<>();

    private Repository repository;

    public Map<Mode, List<DataModel>> modeRemoteControlMap = new HashMap<>();
    public RemoteControl remoteControl = new RemoteControl();
    public MutableLiveData<Mode> modeEdited = new MutableLiveData<>();

    public MyViewModel(Application application) {
        this.repository = new DatabaseManager(Database.Companion.getInstance(application));
    }

    public void getListRemoteControl() {
        repository.getLiveData().observeForever(remoteControls -> listRemoteControl.postValue(remoteControls));
    }

    public void deleteRemoteControl(RemoteControl remoteControl) {
        repository.delete(remoteControl.getId());
    }

    public void insertRemoteControl(RemoteControl remoteControl) {
        repository.insert(remoteControl);
    }
    public RemoteControl getRemoteControlById(int id) {
        return repository.getById(id);
    }
}

