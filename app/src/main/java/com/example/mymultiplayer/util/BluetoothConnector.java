package com.example.mymultiplayer.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BluetoothConnector {
    private String TAG = getClass().getName();
    private BluetoothSocketWrapper bluetoothSocket;
    private BluetoothDevice device;
    private boolean secure;
    private BluetoothAdapter adapter;
    private List<UUID> uuidCandidates;
    private int candidate;


    /**
     * @param device the device
     * @param secure if connection should be done via a secure socket
     * @param adapter the Android BT adapter
     * @param uuidCandidates a list of UUIDs. if null or empty, the Serial PP id is used
     */
    public BluetoothConnector(BluetoothDevice device, boolean secure, BluetoothAdapter adapter,
                              List<UUID> uuidCandidates) {
        this.device = device;
        this.secure = secure;
        this.adapter = adapter;
        this.uuidCandidates = uuidCandidates;

        if (this.uuidCandidates == null || this.uuidCandidates.isEmpty()) {
            this.uuidCandidates = new ArrayList<UUID>();
            this.uuidCandidates.add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        }
    }

    @SuppressLint("MissingPermission")
    public BluetoothSocketWrapper connect() throws IOException {
        final boolean[] success = {false};
        while (selectSocket()) {
            adapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                success[0] = true;
                break;
            } catch (IOException e) {
                Log.e(TAG, "connect e:", e);
                try {
                    bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                    //Thread.sleep(500);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                bluetoothSocket.connect();
                                success[0] = true;
                            } catch (Exception ex) {
                                Log.e(TAG, "ex: " + ex);
                            }

                        }
                    };
                    Handler handler = new Handler();
                    handler.postDelayed(runnable, 500);

                    break;
                } catch (FallbackException e1) {
                    Log.e(TAG, "Could not initialize FallbackBluetoothSocket classes.", e);
                }
            }
        }

        if (!success[0]) {
            throw new IOException("Could not connect to device: "+ device.getAddress());
        }

        return bluetoothSocket;
    }

    @SuppressLint("MissingPermission")
    private boolean selectSocket() throws IOException {
        if (candidate >= uuidCandidates.size()) {
            Log.d(TAG, "selectSocket false");
            return false;
        }

        BluetoothSocket tmp;
        UUID uuid = uuidCandidates.get(candidate++);

        Log.i(TAG, "Attempting to connect to Protocol: "+ uuid);
        if (secure) {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } else {
            tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
        }
        bluetoothSocket = new NativeBluetoothSocket(tmp);
        Log.d(TAG, "selectSocket true");
        return true;
    }

    public static interface BluetoothSocketWrapper {

        InputStream getInputStream() throws IOException;

        OutputStream getOutputStream() throws IOException;

        String getRemoteDeviceName();

        void connect() throws IOException;

        String getRemoteDeviceAddress();

        void close() throws IOException;

        BluetoothSocket getUnderlyingSocket();

    }


    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {

        private BluetoothSocket socket;

        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @SuppressLint("MissingPermission")
        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }

    }

    public class FallbackBluetoothSocket extends NativeBluetoothSocket {

        private BluetoothSocket fallbackSocket;

        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try
            {
                Class<?> clazz = tmp.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[] {Integer.valueOf(1)};
                fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
            }
            catch (Exception e)
            {
                throw new FallbackException(e);
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }


        @SuppressLint("MissingPermission")
        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }


        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }

    }

    public static class FallbackException extends Exception {
        private static final long serialVersionUID = 1L;

        public FallbackException(Exception e) {
            super(e);
        }

    }
}