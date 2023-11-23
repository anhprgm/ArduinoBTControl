package com.arduno.remotebt.adpaters;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.arduno.remotebt.R;
import com.arduno.remotebt.databinding.ItemDeviceBinding;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class DevicesAdapter extends BaseRecyclerAdapter<BluetoothDevice,
        DevicesAdapter.ViewHolder> {
    private Context context;
    private List<BluetoothDevice> list;

    public DevicesAdapter(Context context, List<BluetoothDevice> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(DevicesAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position), position);
    }

    @Override
    public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_device, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDeviceBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemDeviceBinding.bind(itemView);
        }

        @SuppressLint("MissingPermission")
        public void bindData(BluetoothDevice device, int position) {
            binding.tvNameDevice.setText(device.getName());
            binding.tvMacAddress.setText(device.getAddress());

            binding.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
