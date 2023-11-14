package com.arduno.remotebt;

import android.os.Bundle;
import android.os.Handler;

import com.arduno.remotebt.databinding.ActivityConfigureLedBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ConfigureLed extends AppCompatActivity {
    private static final String TAG = "HUUDIEN";

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
            connectedThread.write("1");
        });
        binding.tat.setOnClickListener(v -> connectedThread.write("0"));

        binding.textView.setOnClickListener(v -> {
            connectedThread.run(message -> {
                viewModel.message.postValue(message);
            });
        });
        viewModel.message.observe(this, message -> {
            binding.textView.setText(message);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                connectedThread.run(message1 -> {
                    viewModel.message.postValue(message1);
                });
            }, 100);

        });
        binding.stop.setOnClickListener(v -> {

        });
    }

}