package com.arduno.remotebt.activity;

import android.content.Intent;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityAddRemoteBinding;

public class AddRemoteActivity extends BaseActivity<ActivityAddRemoteBinding> {

    private RemoteControl remoteControl;

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