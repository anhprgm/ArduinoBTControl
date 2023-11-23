package com.arduno.remotebt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.arduno.remotebt.viewmodel.MyViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.Nullable;

public class DataListeningService extends Service {
   private boolean stopWorker = false;
   private int readBufferPosition = 0;
   private byte[] readBuffer;
   private InputStream mmInputStream;
   private Thread workerThread;
   private MyViewModel viewModel;
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      mmInputStream = MySingleton.getInstance().getMmInputStream();
      viewModel = MySingleton.getInstance().getViewModel();
      beginListenForData();
      return START_STICKY;
   }
   public void beginListenForData() {
      final Handler handler = new Handler();
      final byte delimiter = 10; // ASCII for newline

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
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data = new String(encodedBytes, StandardCharsets.US_ASCII);
                        readBufferPosition = 0;

                        handler.post(() -> viewModel.message.postValue(data));
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
   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
   }
}
