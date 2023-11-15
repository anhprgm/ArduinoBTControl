package com.arduno.remotebt.activity;

import android.os.Handler;
import android.util.Log;

import com.arduno.remotebt.MyApplication;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.databinding.ActivityConfigureLedBinding;
import com.arduno.remotebt.viewmodel.MyViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.ViewModelProvider;

public class ConfigureLed extends BaseActivity<ActivityConfigureLedBinding> {
    private static final String TAG = "HUUDIEN";
    boolean isReceive = true;

    private MyViewModel viewModel;
    ConnectedThread connectedThread;
    @Override
    public void initView() {
        connectedThread = MyApplication.getApplication().getCurrentConnectedThread();

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding.bat.setOnClickListener(v -> {
            connectedThread.send("1");
        });
        binding.tat.setOnClickListener(v -> connectedThread.send("0"));

        binding.stop.setOnClickListener(v -> {
            if (isReceive) {
                binding.stop.setText("start");
                isReceive = false;
            } else {
                binding.stop.setText("stop");
                isReceive = true;
                receiverMessage();
            }
        });

        receiverMessage();
        viewModel.message.observe(this, s -> {
            binding.textView.setText(s);
            receiverMessage();
        });

    }

    @Override
    protected ActivityConfigureLedBinding getBinding() {
        return ActivityConfigureLedBinding.inflate(getLayoutInflater());
    }

    void receiverMessage() {
        if (!isReceive) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ExecutorService service = Executors.newSingleThreadExecutor();

            service.submit(() -> {
                connectedThread.run(s -> {
                    Log.d("TAG", "TAH");
                    viewModel.message.postValue(s);
                    service.shutdown();
                });
            });
        }, 100);

    }
}