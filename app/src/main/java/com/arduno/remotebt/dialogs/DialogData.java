package com.arduno.remotebt.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import com.arduno.remotebt.adpaters.StringAdapter;
import com.arduno.remotebt.databinding.DialogEditRemoteBinding;

import java.util.ArrayList;

public class DialogData extends BaseDialog<DialogEditRemoteBinding, DialogData.ExtendBuilder> {

    public StringAdapter stringAdapter;
    private OnItemAddDeviceClickListener onItemClickListener;

    private Context context;

    public DialogData(Context context, ExtendBuilder builder) {
        super(context, builder);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected DialogEditRemoteBinding getViewBinding() {
        return DialogEditRemoteBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected void initListener() {
        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    public static class ExtendBuilder extends BuilderDialog {
        public ExtendBuilder(Context context) {
            super(context);
        }

        @Override
        public BaseDialog build() {
            return new DialogData(context, this);
        }

    }

    public void addItemAdapter(String s) {
        stringAdapter.add(s);
        binding.rcvIrr.smoothScrollToPosition(stringAdapter.getItemCount() - 1);
    }

    public void mShow(Context context) {
        stringAdapter = new StringAdapter(context, new ArrayList<>());
        binding.rcvIrr.setAdapter(stringAdapter);
        stringAdapter.notifyDataSetChanged();

        stringAdapter.setOnItemAddDeviceClickListener(s -> {
            onItemClickListener.onItemClick(s);
            dismiss();
        });
        show();
    }

    public void setOnItemClickListener(OnItemAddDeviceClickListener onItemAddDeviceClickListener) {
        this.onItemClickListener = onItemAddDeviceClickListener;
    }
}

