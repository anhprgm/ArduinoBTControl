package com.arduno.remotebt.activity;

import android.content.Intent;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityAddRemoteBinding;

import java.util.HashMap;
import java.util.Map;

public class AddRemoteActivity extends BaseActivity<ActivityAddRemoteBinding> {

    private RemoteControl remoteControl;

    private Map<Integer, String> temp = new HashMap<>();
    private Map<String, String> mode = new HashMap<>();
    private Map<String, String> power = new HashMap<>();

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

        binding.monitor.setOnClickListener(v -> {
            startActivity(new Intent(this, EditRemoteActivity.class));
        });

    }

    @Override
    protected ActivityAddRemoteBinding getBinding() {
        return ActivityAddRemoteBinding.inflate(getLayoutInflater());
    }
}