package com.arduno.remotebt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.arduno.remotebt.adpaters.EditRemoteAdapter;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.database.DataModel;
import com.arduno.remotebt.databinding.ActivityEditRemoteBinding;
import com.arduno.remotebt.dialogs.DialogData;
import com.arduno.remotebt.dialogs.DialogEditKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditRemoteActivity extends BaseActivity<ActivityEditRemoteBinding> {
    private int number_of_items = 0;
    private EditRemoteAdapter adapter;
    private List<DataModel> list = new ArrayList<>();
    private DialogData dialogData;
    private DialogEditKey dialogEditKey;
    private Boolean isAddItem = false;
    private DataModel currItem;
    private int currPosition = 0;

    private Mode mode;

    public static void startActivity(Context context, Mode mode) {
        Intent intent = new Intent(context, EditRemoteActivity.class);
        intent.putExtra(AddRemoteActivity.TYPE, mode);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();

        adapter = new EditRemoteAdapter(this, list);
        binding.rcvItems.setAdapter(adapter);
        binding.rcvItems.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mode = (Mode) bundle.getSerializable(AddRemoteActivity.TYPE);
            if (viewModel.modeRemoteControlMap.get(mode) != null) {
                list.addAll(Objects.requireNonNull(viewModel.modeRemoteControlMap.get(mode)));
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        }

        dialogData =
                (DialogData) new DialogData.ExtendBuilder(this)
                        .setCancelable(true)
                        .setCanOntouchOutside(false)
                        .build();
        dialogEditKey =
                (DialogEditKey) new DialogEditKey.ExtendBuilder(this)
                        .setCancelable(true)
                        .setCanOntouchOutside(false)
                        .build();
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
            //hide keyboard
            binding.numberOfItems.clearFocus();
            hideKeyBoard(binding.numberOfItems);
            number_of_items = Objects.requireNonNull(binding.numberOfItems.getText()).toString().trim().isEmpty() ? 0 : Integer.parseInt(binding.numberOfItems.getText().toString().trim());
            if (number_of_items > 0) {
                list.clear();
                for (int i = 0; i < number_of_items; i++) {
                    list.add(new DataModel(String.valueOf(i), String.valueOf(i)));
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnItemClickListener((position, type) -> {
            currItem = list.get(position);
            currPosition = position;
            if (Objects.equals(type, "value")) {
                isAddItem = true;
                dialogData.mShow(this);
            } else {
                dialogEditKey.show();
            }
        });
        dialogData.setOnDismissListener(b -> isAddItem = false);
        dialogData.setOnCancelListener(b -> isAddItem = false);
        dialogData.setOnItemClickListener(s -> {
            String value = s.substring(5);
            currItem.setValue(value);
            adapter.setData(currPosition, currItem);
        });
        dialogEditKey.onClickKey = s -> {
            currItem.setKey(s);
            adapter.setData(currPosition, currItem);
        };

        binding.save.setOnClickListener(v -> {
            viewModel.modeRemoteControlMap.put(mode, list);
            viewModel.modeEdited.postValue(mode);
            finish();
        });
        binding.cancel.setOnClickListener(v -> {
            viewModel.modeEdited.postValue(Mode.NONE);
            finish();
        });
    }

    private void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}