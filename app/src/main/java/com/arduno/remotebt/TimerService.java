package com.arduno.remotebt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.arduno.remotebt.activity.MainActivity;
import com.arduno.remotebt.core.ConnectedThread;

public class TimerService extends Service {

    private Handler handler = new Handler();
    private Runnable runnable;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "timer_channel";
    public static final String DELAY_MILLIS = "delay_millis";
    public static final String VALUE = "value";

    private ConnectedThread connectedThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       connectedThread = MySingleton.getInstance().getConnectedThread();
        createNotificationChannel();

        if (intent != null) {
            long delayMillis = intent.getLongExtra(DELAY_MILLIS, 5000);
            String value = intent.getStringExtra(VALUE);
            startForeground(NOTIFICATION_ID, buildNotification());

            runnable = () -> {
                Log.d("TimerService", "runnable: " + delayMillis);
                if (connectedThread == null) {
                    Log.d("TimerService", "runnable: connectedThread null");
                    return;
                }
                if (value == null) {
                    Log.d("TimerService", "runnable: value null");
                    return;
                }
                connectedThread.send(value);
                //remove notification
                stopForeground(true);
                stopSelf();
            };

            handler.postDelayed(runnable, delayMillis);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Remote Control")
                    .setContentText("Running...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        return new Notification.Builder(this)
                .setContentTitle("Remote Control")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }

}