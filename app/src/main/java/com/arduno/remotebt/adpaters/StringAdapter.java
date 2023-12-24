package com.arduno.remotebt.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.arduno.remotebt.R;
import com.arduno.remotebt.databinding.ItemEditRemoteBinding;
import com.arduno.remotebt.dialogs.OnItemAddDeviceClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StringAdapter extends BaseRecyclerAdapter<String, StringAdapter.ViewHolder> {
   private Context context;
   private List<String> list;

   private OnItemAddDeviceClickListener onItemAddDeviceClickListener;

   public StringAdapter(Context context, List<String> list) {
      super(context, list);
      this.context = context;
      this.list = list;
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      holder.bindData(list.get(position), position);
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(layoutInflater.inflate(R.layout.item_edit_remote, parent, false));
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      private ItemEditRemoteBinding binding;

      public ViewHolder(View item) {
         super(item);
         this.binding = ItemEditRemoteBinding.bind(item);
      }

      public void bindData(String s, int pos) {
         binding.tvNameDevice.setText(s);

         binding.getRoot().setOnClickListener(v -> {
            onItemAddDeviceClickListener.onItemClick(s);
         });
      }
   }

   public void setOnItemAddDeviceClickListener(OnItemAddDeviceClickListener listener) {
      this.onItemAddDeviceClickListener = listener;
   }
}
