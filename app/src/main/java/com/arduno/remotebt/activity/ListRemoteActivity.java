package com.arduno.remotebt.activity;

import android.content.Intent;
import android.util.Log;

import com.arduno.remotebt.adpaters.RemoteControlAdapter;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityListRemoteBinding;

import java.util.ArrayList;
import java.util.List;

public class ListRemoteActivity extends BaseActivity<ActivityListRemoteBinding> {
    private RemoteControlAdapter adapter;
    private List<RemoteControl> list = new ArrayList<>();

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

        adapter = new RemoteControlAdapter(this, list);
        binding.listRemote.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        binding.cancel.setOnClickListener(v -> {
            finish();
        });

        viewModel.listRemoteControl.observe(this, list -> {
            Log.d("TAG", "initView: " + list.toString());
            this.list = list;
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        });
        viewModel.getListRemoteControl();

        adapter.setOnItemClickListener((pos, type) -> {
            if (type.equals("delete")) {
                viewModel.deleteRemoteControl(list.get(pos));
            } else if (type.equals("edit")) {

                startActivity(new Intent(this, AddRemoteActivity.class));
            } else if (type.equals("root")) {
//                RemoteControlActivity.startActivity(this, list.get(pos));
            }
        });
    }

    @Override
    protected ActivityListRemoteBinding getBinding() {
        return ActivityListRemoteBinding.inflate(getLayoutInflater());
    }
}