package com.arduno.remotebt.activity;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.arduno.remotebt.MyApplication;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.databinding.ActivityConfigureLedBinding;
import com.arduno.remotebt.viewmodel.MyViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.lifecycle.ViewModelProvider;

public class ConfigureLed extends BaseActivity<ActivityConfigureLedBinding> {
    private static final String TAG = "HUUDIEN";
    boolean isReceive = true;

    private MyViewModel viewModel;
    ConnectedThread connectedThread;
    BluetoothSocket bluetoothSocket;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    @Override
    public void initView() {
        connectedThread = MyApplication.getApplication().getCurrentConnectedThread();
        bluetoothSocket = MyApplication.getApplication().getBluetoothSocket();

        mmInputStream = connectedThread.getMmInStream();
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding.bat.setOnClickListener(v -> {
            connectedThread.send("1");
        });
        binding.tat.setOnClickListener(v -> connectedThread.send("0"));

        binding.stop.toggle();
        binding.stop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stopWorker = !isChecked;
            if (!stopWorker) {
                beginListenForData();
            }
        });

        beginListenForData();

    }

    @Override
    protected ActivityConfigureLedBinding getBinding() {
        return ActivityConfigureLedBinding.inflate(getLayoutInflater());
    }


    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; // This is the ASCII code for a newline
        // character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                try {
                    int bytesAvailable = mmInputStream.available();
                    if (bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInputStream.read(packetBytes);
                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0,
                                        encodedBytes, 0,
                                        encodedBytes.length);
                                final String data = new String(
                                        encodedBytes, "US-ASCII");
                                readBufferPosition = 0;

                                handler.post(() -> binding.textView.setText(data));
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException ex) {
                    stopWorker = true;
                }
            }
        });

        workerThread.start();
    }
}