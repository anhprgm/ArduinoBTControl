package com.arduno.remotebt.core;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.arduno.remotebt.base.Listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private static final String TAG = "FrugalLogs";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private String valueRead;


    public InputStream getMmInStream() {
        return mmInStream;
    }

    public OutputStream getMmOutStream() {
        return mmOutStream;
    }

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mmSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public String getValueRead() {
        return valueRead;
    }


    public void run(Listener onDataReceived) {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        int numberOfReadings = 0;
        while (numberOfReadings < 1) {
            try {

                buffer[bytes] = (byte) mmInStream.read();
                String readMessage;
                if (buffer[bytes] == '\n') {
                    readMessage = new String(buffer, 0, bytes);
                    valueRead = readMessage;
                    bytes = 0;
                    numberOfReadings++;
                } else {
                    bytes++;
                }
                onDataReceived.onMessageReceived(valueRead);
            } catch (Exception e) {
                break;
            }

        }
    }

    public void send(String input) {
        byte[] bytes = input.getBytes();
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("Send Error", "Unable to send message", e);
        }
    }

    public void send(int v) {
        try {
            mmOutStream.write(v);
        } catch (IOException e) {
            Log.e("Send Error", "Unable to send message", e);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("Cancel Error", "Could not close the connect socket", e);
        }
    }
}