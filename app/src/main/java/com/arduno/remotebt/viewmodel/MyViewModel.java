package com.arduno.remotebt.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    // Declare LiveData
    public MutableLiveData<String> message = new MutableLiveData<>();

    // Rest of your ViewModel code...
}

