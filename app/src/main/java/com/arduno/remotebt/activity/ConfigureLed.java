package com.arduno.remotebt.activity;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.databinding.ActivityConfigureLedBinding;

public class ConfigureLed extends BaseActivity<ActivityConfigureLedBinding> {
    private static final String TAG = "HUUDIEN";
    boolean isReceive = true;
    ConnectedThread connectedThread;
    BluetoothSocket bluetoothSocket;


    @Override
    public void initView() {
        viewModel = singleton.getViewModel();
        connectedThread = singleton.getConnectedThread();
        bluetoothSocket = singleton.getBluetoothSocket();



        binding.bat.setOnClickListener(v -> {
            connectedThread.send("1");
        });
        binding.tat.setOnClickListener(v -> connectedThread.send("0"));

        binding.stop.toggle();
        binding.stop.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        viewModel.message.observe(this, s -> {
            Log.d(TAG, "initView: " + s);
            if (s.length() < 3) return;
            String firstThree = s.substring(0, 3);
            switch (firstThree) {
                case "IRR":
                    binding.textView.setText(s.substring(5));
                    break;
            }
        });

    }

    @Override
    protected ActivityConfigureLedBinding getBinding() {
        return ActivityConfigureLedBinding.inflate(getLayoutInflater());
    }



}