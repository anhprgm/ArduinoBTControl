package com.arduno.remotebt.dialogs;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;

import com.arduno.remotebt.adpaters.DevicesAdapter;
import com.arduno.remotebt.databinding.DialogDevicesBinding;

import java.util.List;

public class DialogDevices extends BaseDialog<DialogDevicesBinding, DialogDevices.ExtendBuilder> {

    private DevicesAdapter devicesAdapter;
    private OnItemClickListener onItemClickListener;

    public DialogDevices(Context context, ExtendBuilder builder) {
        super(context, builder);
    }

    @Override
    public DialogDevicesBinding getViewBinding() {
        return DialogDevicesBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    public void initListener() {
    }

    public void mShow(List<BluetoothDevice> list) {
        devicesAdapter = new DevicesAdapter(getContext(), list);
        binding.rcvDevices.setAdapter(devicesAdapter);
        devicesAdapter.setOnItemClickListener(position -> {
            dismiss();
            onItemClickListener.onItemClick(list.get(position));
        });
        show();
    }

    public static class ExtendBuilder extends BuilderDialog {
        public ExtendBuilder(Context context) {
            super(context);
        }

        @Override
        public BaseDialog build() {
            return new DialogDevices(context, this);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
