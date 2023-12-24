package com.arduno.remotebt.activity;

import com.arduno.remotebt.adpaters.EditRemoteAdapter;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.BaseModel;
import com.arduno.remotebt.databinding.ActivityEditRemoteBinding;
import com.arduno.remotebt.dialogs.DialogData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditRemoteActivity extends BaseActivity<ActivityEditRemoteBinding> {
    private int number_of_items = 0;
    private EditRemoteAdapter adapter;
    private List<BaseModel> list = new ArrayList<>();
    private DialogData dialogData;
    private Boolean isAddItem = false;
    private BaseModel currItem;

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

        adapter = new EditRemoteAdapter(this, list);
        binding.rcvItems.setAdapter(adapter);
        binding.rcvItems.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        dialogData =
                (DialogData) new DialogData.ExtendBuilder(this).setCancelable(true).setCanOntouchOutside(false).build();
        viewModel.message.observe(this, m -> {
            if (isAddItem) {
                dialogData.addItemAdapter(m);
            }
        });

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
        adapter.setOnItemClickListener(position -> {
            isAddItem = true;
            currItem = list.get(position);
            dialogData.mShow(this);
        });
        dialogData.setOnDismissListener(b -> isAddItem = false);
        dialogData.setOnItemClickListener(s -> {
            currItem.setValue(s);
        });
    }
}