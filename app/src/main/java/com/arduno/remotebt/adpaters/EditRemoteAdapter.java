package com.arduno.remotebt.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.arduno.remotebt.R;
import com.arduno.remotebt.database.DataModel;
import com.arduno.remotebt.databinding.ItemListBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditRemoteAdapter extends BaseRecyclerAdapter<DataModel,
        EditRemoteAdapter.ViewHolder> {

   private Context context;
   private List<DataModel> list;

   public EditRemoteAdapter(Context context, List<DataModel> list) {
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
      View view = layoutInflater.inflate(R.layout.item_list, parent, false);
      return new ViewHolder(view);
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      private ItemListBinding binding;

      public ViewHolder(View itemView) {
         super(itemView);
         this.binding = ItemListBinding.bind(itemView);
      }

      public void bindData(DataModel dataModel, int position) {
         binding.key.setText(dataModel.getKey());
         binding.value.setText(dataModel.getValue());
         binding.stt.setText("STT: " + (position + 1));

         binding.value.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position);
         });
      }
   }
}
