package com.arduno.remotebt.activity;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.databinding.ActivityAddRemoteBinding;

public class AddRemoteActivity extends BaseActivity<ActivityAddRemoteBinding> {

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

    }

    @Override
    protected ActivityAddRemoteBinding getBinding() {
        return ActivityAddRemoteBinding.inflate(getLayoutInflater());
    }
}