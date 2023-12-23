package com.arduno.remotebt.activity;

import com.arduno.remotebt.adpaters.EditRemoteAdapter;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.BaseModel;
import com.arduno.remotebt.databinding.ActivityEditRemoteBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditRemoteActivity extends BaseActivity<ActivityEditRemoteBinding> {
    private int number_of_items = 0;
    private EditRemoteAdapter adapter;

    private List<BaseModel> list = new ArrayList<>();

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();
        adapter = new EditRemoteAdapter(this, list);
        binding.rcvItems.setAdapter(adapter);
        binding.rcvItems.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        initData();
    }

    @Override
    protected ActivityEditRemoteBinding getBinding() {
        return ActivityEditRemoteBinding.inflate(getLayoutInflater());
    }

    private void initData() {
        binding.tvNumberOfItems.setOnClickListener(v -> {
            number_of_items = Objects.requireNonNull(binding.numberOfItems.getText()).toString().trim().isEmpty() ? 0 : Integer.parseInt(binding.numberOfItems.getText().toString().trim());
            if (number_of_items > 0) {
                list.clear();
                for (int i = 0; i < number_of_items; i++) {
                    list.add(new BaseModel("Key: " + i, "Value: " + i));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}