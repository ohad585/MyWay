package com.example.myway;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class Bluetooth {
    private boolean scanning;
    private Handler handler ;
    BluetoothAdapter bluetoothAdapter;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            String name = bluetoothDevice.getName();
            if(name != null){
                Log.d("TAG", "onLeScan: "+bluetoothDevice.toString());
            }
        }
    };


    public Bluetooth(BluetoothAdapter btAdapter) {
        bluetoothAdapter = btAdapter;
        if (bluetoothAdapter == null) {
            Log.d("TAG", "Bluetooth: Device Dosent support Bluetooth");
            // Device doesn't support Bluetooth
        }
        handler = new Handler();


        scanLeDevice();
    }

    private void scanLeDevice() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            Log.d("TAGOHAD", "scanLeDevice: Starting to scan");
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }
}
