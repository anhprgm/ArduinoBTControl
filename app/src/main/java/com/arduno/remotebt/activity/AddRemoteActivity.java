package com.arduno.remotebt.activity;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.DataModel;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityAddRemoteBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRemoteActivity extends BaseActivity<ActivityAddRemoteBinding> {

    public static final String TYPE = "type";

    private Map<Mode, List<DataModel>> remoteControl;

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

        binding.monitor.setOnClickListener(v -> {
            EditRemoteActivity.startActivity(this, Mode.TEMP);
        });
        binding.power.setOnClickListener(v -> {
            EditRemoteActivity.startActivity(this, Mode.POWER);
        });
        binding.mode.setOnClickListener(v -> {
            EditRemoteActivity.startActivity(this, Mode.MODE);
        });
        binding.fan.setOnClickListener(v -> {
            EditRemoteActivity.startActivity(this, Mode.FAN);
        });
        binding.sleep.setOnClickListener(v -> {
            EditRemoteActivity.startActivity(this, Mode.SLEEP);
        });

        viewModel.modeEdited.observe(this, m -> {
            switch (m) {
                case TEMP -> toast("Temp");
                case POWER -> toast("Power");
                case MODE -> toast("Mode");
                case FAN -> toast("Fan");
                case SLEEP -> toast("Sleep");
                default -> toast("Cancel");
            }
        });
        binding.save.setOnClickListener(v -> {
            List<DataModel> temp = viewModel.modeRemoteControlMap.get(Mode.TEMP);
            List<DataModel> power = viewModel.modeRemoteControlMap.get(Mode.POWER);
            List<DataModel> mode = viewModel.modeRemoteControlMap.get(Mode.MODE);
            List<DataModel> fan = viewModel.modeRemoteControlMap.get(Mode.FAN);
            List<DataModel> sleep = viewModel.modeRemoteControlMap.get(Mode.SLEEP);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            if (power == null) {
                power = new ArrayList<>();
            }
            if (mode == null) {
                mode = new ArrayList<>();
            }
            if (fan == null) {
                fan = new ArrayList<>();
            }
            if (sleep == null) {
                sleep = new ArrayList<>();
            }
            viewModel.insertRemoteControl(new RemoteControl(0, "", temp, power, mode, fan, sleep));
            viewModel.modeRemoteControlMap = new HashMap<>();
            finish();
        });
    }

    @Override
    protected ActivityAddRemoteBinding getBinding() {
        return ActivityAddRemoteBinding.inflate(getLayoutInflater());
    }
}

