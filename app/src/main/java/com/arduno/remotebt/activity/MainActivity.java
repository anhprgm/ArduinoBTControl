package com.arduno.remotebt.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.arduno.remotebt.MyApplication;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectThread;
import com.arduno.remotebt.core.ConnectedClass;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.databinding.ActivityMainBinding;

import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final String TAG = "HUUDIEN";
    private static final int REQUEST_ENABLE_BT = 1;
    public static Handler handler;
    private final static int ERROR_READ = 0;
    BluetoothDevice arduinoBTModule = null;
    UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ConnectedThread connectedThread;

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void initView() {
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {

                    case ERROR_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino

                        Toast.makeText(MainActivity.this, arduinoMsg,
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        final Observable<ConnectedClass> connectToBTObservable = Observable.create(emitter -> {
            ConnectThread connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
            connectThread.run();
            if (connectThread.getMmSocket().isConnected()) {
                connectedThread = new ConnectedThread(connectThread.getMmSocket());
                if(connectedThread.getMmInStream() != null && connectedThread!= null) {
                    ConnectedClass connected = new ConnectedClass();
                    connected.setConnected(true);
                    emitter.onNext(connected);
                }
            }
            emitter.onComplete();
        });

        //Display all the linked BT Devices
        binding.seachDevices.setOnClickListener(view -> {
            //Check if the phone supports BT
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
                Log.d(TAG, "Device doesn't support Bluetooth");
            } else {
                Log.d(TAG, "Device support Bluetooth");
                //Check BT enabled. If disabled, we ask the user to enable BT
                if (!bluetoothAdapter.isEnabled()) {
                    Log.d(TAG, "Bluetooth is disabled");
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "We don't BT Permissions");
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        Log.d(TAG, "Bluetooth is enabled now");
                    } else {
                        Log.d(TAG, "We have BT Permissions");
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        Log.d(TAG, "Bluetooth is enabled now");
                    }

                } else {
                    Log.d(TAG, "Bluetooth is enabled");
                }
                String btDevicesString="";
                Set< BluetoothDevice > pairedDevices = bluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device: pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        Log.d(TAG, "deviceName:" + deviceName);
                        Log.d(TAG, "deviceHardwareAddress:" + deviceHardwareAddress);
                        btDevicesString=btDevicesString+deviceName+" || "+deviceHardwareAddress+"\n";
                        if (deviceName.equals("HC-05")) {
                            Log.d(TAG, "HC-05 found");
                            arduinoUUID = device.getUuids()[0].getUuid();
                            arduinoBTModule = device;
                            binding.connectToDevice.setEnabled(true);
                        }
                        binding.btDevices.setText(btDevicesString);
                    }
                }
            }
            Log.d(TAG, "Button Pressed");
        });

        binding.connectToDevice.setOnClickListener(view -> {
            if (arduinoBTModule != null) {
                connectToBTObservable.
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribeOn(Schedulers.io()).
                        subscribe(connectedToBTDevice -> {
                            //valueRead returned by the onNext() from the Observable
                            if(connectedToBTDevice.isConnected()){
                                binding.nextActivity.setEnabled(true);
                            }
                            //We just scratched the surface with RxAndroid
                        });

            }
        });

        binding.nextActivity.setOnClickListener(v -> {
            MyApplication.getApplication().setupConnectedThread(connectedThread);
            Intent intent = new Intent(MainActivity.this, ConfigureLed.class);
            startActivity(intent);
        });
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}