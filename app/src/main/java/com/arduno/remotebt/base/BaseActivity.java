package com.arduno.remotebt.base;

import android.os.Bundle;
import android.widget.Toast;

import com.arduno.remotebt.MySingleton;
import com.arduno.remotebt.viewmodel.MyViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

   protected VB binding;
   public MyViewModel viewModel;
   public MySingleton singleton;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (binding == null) {
         binding = getBinding();
         setContentView(binding.getRoot());
      }
      singleton = MySingleton.getInstance();
      initView();
   }

   public abstract void initView();

   protected abstract VB getBinding();

   public void toast(String message) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
   }
}