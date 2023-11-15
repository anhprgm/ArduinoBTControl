package com.arduno.remotebt.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

   protected VB binding;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (binding == null) {
         binding = getBinding();
         setContentView(binding.getRoot());
      }
      initView();
   }

   public abstract void initView();
   protected abstract VB getBinding();
}