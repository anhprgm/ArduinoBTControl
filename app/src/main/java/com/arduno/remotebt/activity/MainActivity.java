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

import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectThread;
import com.arduno.remotebt.core.ConnectedClass;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.core.DataListeningService;
import com.arduno.remotebt.databinding.ActivityMainBinding;
import com.arduno.remotebt.dialogs.DialogDevices;
import com.arduno.remotebt.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arduno.remotebt.ultils.Constants.HMR;
import static com.arduno.remotebt.ultils.Constants.THG;
import static com.arduno.remotebt.ultils.Constants.TPR;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final String TAG = "HUUDIEN";
    private static final int REQUEST_ENABLE_BT = 1;
    public static Handler handler;
    private final static int ERROR_READ = 0;
    BluetoothDevice arduinoBTModule = null;
    UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ConnectedThread connectedThread;

    private DialogDevices dialogDevices;

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void initView() {
        singleton.setViewModel(new MyViewModel(this.getApplication()));
        viewModel = singleton.getViewModel();
        dialogDevices = (DialogDevices) new DialogDevices.ExtendBuilder(this)
                .setCancelable(true)
                .setCanOntouchOutside(false)
                .build();

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == ERROR_READ) {
                    String arduinoMsg = msg.obj.toString(); // Read message from Arduino

                    Toast.makeText(MainActivity.this, arduinoMsg,
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        final Observable<ConnectedClass> connectToBTObservable = Observable.create(emitter -> {
            ConnectThread connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
            connectThread.run();
            if (connectThread.getMmSocket().isConnected()) {
                connectedThread = new ConnectedThread(connectThread.getMmSocket());
                singleton.setBluetoothSocket(connectThread.getMmSocket());
                if(connectedThread.getMmInStream() != null && connectedThread!= null) {
                    ConnectedClass connected = new ConnectedClass();
                    connected.setConnected(true);
                    emitter.onNext(connected);
                }
            }
            emitter.onComplete();
        });

        binding.seachDevices.setOnClickListener(view -> {
            if (bluetoothAdapter == null) {
                Log.d(TAG, "Device doesn't support Bluetooth");
            } else {
                Log.d(TAG, "Device support Bluetooth");
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
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                List<BluetoothDevice> listDevices = new ArrayList<>(pairedDevices);
                viewModel.listDevices.postValue(pairedDevices);
                dialogDevices.mShow(listDevices);

            }
        });
        dialogDevices.setOnItemClickListener(device -> {
            Log.d(TAG, "Device Name Clicked: " + device.getName());
            arduinoUUID = device.getUuids()[0].getUuid();
            arduinoBTModule = device;

            connectToBTObservable.
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    subscribe(connectedToBTDevice -> {
                        //valueRead returned by the onNext() from the Observable
                        if (connectedToBTDevice.isConnected()) {
                            Log.d(TAG, "initView: " + "Connected");
                            singleton.setMmInputStream(connectedThread.getMmInStream());
                            singleton.setViewModel(viewModel);

                            Intent intent = new Intent(this, DataListeningService.class);
                            startService(intent);
                        }
                    });
        });

        viewModel.message.observe(this, s -> {
            if (s.length() < 3) return;
            Log.d(TAG, "initView: " + s);
            String firstThree = s.substring(0, 3);
            switch (firstThree) {
                case TPR:
                    binding.tvTemperature.setText("Temperature: " + s.substring(5) + "°C");
                    break;
                case HMR:
                    binding.tvHumidity.setText("Humidity: " + s.substring(5) + "%");
                    break;
            }
        });
        binding.ivAddRemote.setOnClickListener(v -> {
            singleton.setConnectedThread(connectedThread);
            startActivity(new Intent(MainActivity.this, AddRemoteActivity.class));
        });
        binding.llTemperature.setOnClickListener(v -> connectedThread.send(THG + "\n"));
        binding.ivRemote.setOnClickListener(v -> {
            singleton.setConnectedThread(connectedThread);
            startActivity(new Intent(MainActivity.this, ListRemoteActivity.class));
        });
    }


    private void disConnectedDevice() {
        singleton.setConnectedThread(null);
        arduinoBTModule = null;
        connectedThread.cancel();
        Intent intent = new Intent(this, DataListeningService.class);
        stopService(intent);
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}