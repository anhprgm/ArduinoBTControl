package com.arduno.remotebt.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.arduno.remotebt.R;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ItemRemoteControlBinding;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RemoteControlAdapter extends BaseRecyclerAdapter<RemoteControl,
        RemoteControlAdapter.ViewHolder> {
   private Context context;


   public RemoteControlAdapter(Context context, List<RemoteControl> list) {
      super(context, list);
      this.context = context;
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      holder.bindData(list.get(position), position);
   }

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(layoutInflater.inflate(R.layout.item_remote_control, parent, false));
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      private ItemRemoteControlBinding binding;

      public ViewHolder(View itemView) {
         super(itemView);
         this.binding = ItemRemoteControlBinding.bind(itemView);
      }

      public void bindData(RemoteControl remoteControl, int position) {
         binding.delete.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position, "delete");
         });
         binding.edit.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position, "edit");
         });
         binding.title.setText(remoteControl.getNameRemoteControl());
         binding.getRoot().setOnClickListener(v -> {
            onItemClickListener.onItemClick(position, "root");
         });
      }
   }
}
