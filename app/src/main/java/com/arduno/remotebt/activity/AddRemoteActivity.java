package com.arduno.remotebt.activity;

import android.content.Intent;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.DataModel;
import com.arduno.remotebt.databinding.ActivityAddRemoteBinding;

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
    }

    @Override
    protected ActivityAddRemoteBinding getBinding() {
        return ActivityAddRemoteBinding.inflate(getLayoutInflater());
    }
}

