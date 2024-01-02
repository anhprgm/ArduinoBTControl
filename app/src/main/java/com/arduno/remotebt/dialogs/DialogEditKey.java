package com.arduno.remotebt.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import com.arduno.remotebt.databinding.DialogEditKeyBinding;

public class DialogEditKey extends BaseDialog<DialogEditKeyBinding, DialogEditKey.ExtendBuilder> {
   public OnClickKey onClickKey;

   public DialogEditKey(Context context, ExtendBuilder builder) {
      super(context, builder);
   }

   @Override
   protected DialogEditKeyBinding getViewBinding() {
      return DialogEditKeyBinding.inflate(LayoutInflater.from(getContext()));
   }

   @Override
   protected void initListener() {
      binding.btnAdd.setOnClickListener(v -> {
         onClickKey.onClickKey(binding.edtKeyName.getText().toString());
         binding.edtKeyName.setText("");
         dismiss();
      });
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
         return new DialogEditKey(context, this);
      }
   }
}
