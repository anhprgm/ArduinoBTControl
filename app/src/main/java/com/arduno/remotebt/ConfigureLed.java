package com.arduno.remotebt;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.arduno.remotebt.databinding.ActivityConfigureLedBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ConfigureLed extends AppCompatActivity {
    private static final String TAG = "HUUDIEN";
    boolean isReceive = true;

    private MyViewModel viewModel;
    private ActivityConfigureLedBinding binding;
    ConnectedThread connectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigureLedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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