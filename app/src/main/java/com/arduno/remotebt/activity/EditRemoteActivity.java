package com.arduno.remotebt.activity;

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.databinding.ActivityEditRemoteBinding;

public class EditRemoteActivity extends BaseActivity<ActivityEditRemoteBinding> {

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();
    }

    @Override
    protected ActivityEditRemoteBinding getBinding() {
        return ActivityEditRemoteBinding.inflate(getLayoutInflater());
    }
}