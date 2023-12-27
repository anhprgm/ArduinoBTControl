package com.arduno.remotebt.activity;

import android.content.Intent;

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
            Intent intent = new Intent(this, EditRemoteActivity.class);
            intent.putExtra(TYPE, Mode.TEMP);
            startActivity(intent);
        });
        binding.power.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditRemoteActivity.class);
            intent.putExtra(TYPE, Mode.POWER);
            startActivity(intent);
        });

        viewModel.modeEdited.observe(this, m -> {
            switch (m) {
                case TEMP -> toast("Temp");
                case POWER -> toast("Power");
                case MODE -> toast("Mode");
                default -> toast("Cancel");
            }
        });
        binding.save.setOnClickListener(v -> {
            List<DataModel> temp = viewModel.modeRemoteControlMap.get(Mode.TEMP);
            List<DataModel> power = viewModel.modeRemoteControlMap.get(Mode.POWER);
            List<DataModel> mode = viewModel.modeRemoteControlMap.get(Mode.MODE);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            if (power == null) {
                power = new ArrayList<>();
            }
            if (mode == null) {
                mode = new ArrayList<>();
            }
            viewModel.insertRemoteControl(new RemoteControl(0,"" ,temp, power, mode));
            viewModel.modeRemoteControlMap = new HashMap<>();
            finish();
        });
    }

    @Override
    protected ActivityAddRemoteBinding getBinding() {
        return ActivityAddRemoteBinding.inflate(getLayoutInflater());
    }
}

