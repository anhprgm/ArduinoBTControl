package com.arduno.remotebt.activity;

import android.content.Intent;

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
            this.list = list;
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        });
        viewModel.getListRemoteControl();

        adapter.setOnItemClickListener((pos, type) -> {
            RemoteControl rc = list.get(pos);
            if (type.equals("delete")) {
                viewModel.deleteRemoteControl(rc);
            } else if (type.equals("edit")) {
                viewModel.modeRemoteControlMap.put(Mode.TEMP, rc.getTemp());
                viewModel.modeRemoteControlMap.put(Mode.MODE, rc.getMode());
                viewModel.modeRemoteControlMap.put(Mode.FAN, rc.getFan());
                viewModel.modeRemoteControlMap.put(Mode.SLEEP, rc.getSleep());
                viewModel.modeRemoteControlMap.put(Mode.POWER, rc.getPower());
                viewModel.remoteControl = rc;
                startActivity(new Intent(this, AddRemoteActivity.class).putExtra(AddRemoteActivity.EDIT, "EDIT"));
            } else if (type.equals("root")) {
                viewModel.remoteControl = rc;
                startActivity(new Intent(this, RemoteActivity.class));
            }
        });
    }

    @Override
    protected ActivityListRemoteBinding getBinding() {
        return ActivityListRemoteBinding.inflate(getLayoutInflater());
    }
}