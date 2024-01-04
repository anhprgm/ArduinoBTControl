package com.arduno.remotebt.activity;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityRemoteBinding;
import com.arduno.remotebt.ultils.Constants;

public class RemoteActivity extends BaseActivity<ActivityRemoteBinding> {
    private RemoteControl rc;
    private int currItemTemp = 0;
    private int currItemMode = 0;
    private int currItemFan = 0;
    private int currItemSleep = 0;
    private int currItemPower = 0;
    ConnectedThread connectedThread;

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();
        connectedThread = singleton.getConnectedThread();
        rc = viewModel.remoteControl;
        binding.cancel.setOnClickListener(v -> {
            finish();
        });
        binding.mode.setOnClickListener(v -> {
            currItemMode++;
            if (currItemMode > rc.getMode().size()) {
                currItemMode = 0;
            }
            connectedThread.send(Constants.IR_Send + rc.getMode().get(currItemMode).getValue() + "\n");
        });
        binding.fan.setOnClickListener(v -> {
            currItemFan++;
            if (currItemFan > rc.getFan().size()) {
                currItemFan = 0;
            }
            connectedThread.send(Constants.IR_Send + rc.getFan().get(currItemFan).getValue() + "\n");
        });
        binding.sleep.setOnClickListener(v -> {
            currItemSleep++;
            if (currItemSleep > rc.getSleep().size()) {
                currItemSleep = 0;
            }
            connectedThread.send(Constants.IR_Send + rc.getSleep().get(currItemSleep).getValue() + "\n");
        });
        binding.power.setOnClickListener(v -> {
            currItemPower++;
            if (currItemPower > rc.getPower().size()) {
                currItemPower = 0;
            }
            connectedThread.send(Constants.IR_Send + rc.getPower().get(currItemPower).getValue() + "\n");
        });
        binding.up.setOnClickListener(v -> {
            if (currItemTemp < rc.getTemp().size()) {
                currItemTemp++;
            }
            connectedThread.send(Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
            binding.monitor.setText(rc.getTemp().get(currItemTemp).getKey());
        });
        binding.down.setOnClickListener(v -> {
            if (currItemTemp > 0) {
                currItemTemp--;
            }
            binding.monitor.setText(rc.getTemp().get(currItemTemp).getKey());
            connectedThread.send(Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
        });
    }

    @Override
    protected ActivityRemoteBinding getBinding() {
        return ActivityRemoteBinding.inflate(getLayoutInflater());
    }
}