package com.arduno.remotebt.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.arduno.remotebt.TimerService;
import com.arduno.remotebt.base.BaseActivity;
import com.arduno.remotebt.core.ConnectedThread;
import com.arduno.remotebt.database.RemoteControl;
import com.arduno.remotebt.databinding.ActivityRemoteBinding;
import com.arduno.remotebt.dialogs.DialogData;
import com.arduno.remotebt.ultils.Constants;

public class RemoteActivity extends BaseActivity<ActivityRemoteBinding> {
    private RemoteControl rc;
    private int currItemTemp = 0;
    private int currItemMode = 0;
    private int currItemFan = 0;
    private int currItemSleep = 0;
    private int currItemPower = 0;
    ConnectedThread connectedThread;
    private static String TAG = "SEND";
    private DialogData dialogData;
    private long delayMillis;

    @Override
    public void initView() {
        viewModel = singleton.getViewModel();
        connectedThread = singleton.getConnectedThread();
        rc = viewModel.remoteControl;

        dialogData =
                (DialogData) new DialogData.ExtendBuilder(this)
                        .setCancelable(true)
                        .setCanOntouchOutside(false)
                        .build();

        binding.cancel.setOnClickListener(v -> {
            finish();
        });
        binding.mode.setOnClickListener(v -> {
            if (rc.getMode().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }
            Log.d(TAG, Constants.IR_Send + rc.getMode().get(currItemMode).getValue() + "\n");
            binding.tvMode.setText(rc.getMode().get(currItemMode).getKey());
            connectedThread.send(Constants.IR_Send + rc.getMode().get(currItemMode).getValue() + "\n");
            sendVisible();
            currItemMode++;
            if (currItemMode == rc.getMode().size()) {
                currItemMode = 0;
            }
        });
        binding.fan.setOnClickListener(v -> {
            if (rc.getFan().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }

            Log.d(TAG, Constants.IR_Send + rc.getFan().get(currItemFan).getValue() + "\n");
            binding.ivFan.setText(rc.getFan().get(currItemFan).getKey());
            connectedThread.send(Constants.IR_Send + rc.getFan().get(currItemFan).getValue() + "\n");
            sendVisible();
            currItemFan++;
            if (currItemFan == rc.getFan().size()) {
                currItemFan = 0;
            }
        });
        binding.sleep.setOnClickListener(v -> {
            if (rc.getSleep().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }
            binding.ivSleep.setText(rc.getSleep().get(currItemSleep).getKey());
            Log.d(TAG, Constants.IR_Send + rc.getSleep().get(currItemSleep).getValue() + "\n");
            connectedThread.send(Constants.IR_Send + rc.getSleep().get(currItemSleep).getValue() + "\n");
            sendVisible();
            currItemSleep++;
            if (currItemSleep == rc.getSleep().size()) {
                currItemSleep = 0;
            }
        });
        binding.power.setOnClickListener(v -> {
            if (rc.getPower().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }
            Log.d(TAG, Constants.IR_Send + rc.getPower().get(currItemPower).getValue() + "\n");
            connectedThread.send(Constants.IR_Send + rc.getPower().get(currItemPower).getValue() + "\n");
            sendVisible();
            currItemPower++;
            if (currItemPower == rc.getPower().size()) {
                currItemPower = 0;
            }
        });
        binding.up.setOnClickListener(v -> {
            if (rc.getTemp().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }
            Log.d(TAG, Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
            connectedThread.send(Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
            sendVisible();
            binding.monitor.setText(rc.getTemp().get(currItemTemp).getKey() + "°C");
            if (currItemTemp < rc.getTemp().size() - 1) {
                currItemTemp++;
            }
        });
        binding.down.setOnClickListener(v -> {
            if (rc.getTemp().isEmpty()) {
                toast("Chưa có chế độ nào được cài đặt");
                return;
            }
            Log.d(TAG, Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
            binding.monitor.setText(rc.getTemp().get(currItemTemp).getKey() + "°C");
            connectedThread.send(Constants.IR_Send + rc.getTemp().get(currItemTemp).getValue() + "\n");
            sendVisible();
            if (currItemTemp > 1) {
                currItemTemp--;
            }
        });
        binding.timer.setOnClickListener(v -> {
            showTimePickerDialog();
        });
        dialogData.setOnItemClickListener(s -> {
            Intent serviceIntent = new Intent(this, TimerService.class);
            serviceIntent.putExtra(TimerService.DELAY_MILLIS, delayMillis);
            serviceIntent.putExtra(TimerService.VALUE, s);
            startService(serviceIntent);
        });
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                    Log.d(TAG,
                            "initView: " + formatRemainingTime(calendar.getTimeInMillis() - System.currentTimeMillis()));
                    if (calendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
                        toast("Thời gian đã trôi qua");
                        return;
                    }
                    dialogData.mShow(this);
                    for (int i = 0; i < rc.getPower().size(); i++) {
                        dialogData.addItemAdapter(rc.getPower().get(i).getKey() + " " + rc.getPower().get(i).getValue() + "\n");
                    }
                    delayMillis = calendar.getTimeInMillis() - System.currentTimeMillis();
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private String formatRemainingTime(long remainingTimeInMillis) {
        long hours = remainingTimeInMillis / (1000 * 60 * 60);
        long minutes = (remainingTimeInMillis % (1000 * 60 * 60)) / (1000 * 60);

        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    protected ActivityRemoteBinding getBinding() {
        return ActivityRemoteBinding.inflate(getLayoutInflater());
    }

    void sendVisible() {
        if (binding.send.getVisibility() != View.VISIBLE) {
            Handler handler = new Handler();
            handler.postDelayed(() -> binding.send.setVisibility(View.GONE), 1000);
        }
        binding.send.setVisibility(View.VISIBLE);
    }
}