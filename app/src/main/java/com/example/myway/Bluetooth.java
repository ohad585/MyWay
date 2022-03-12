package com.example.myway;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class Bluetooth {
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler ;
    BluetoothAdapter bluetoothAdapter;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("TAGOhad", "onScanResult: "+result.getDevice().toString());
        }

    };


    public Bluetooth(BluetoothManager btManager, BluetoothAdapter btAdapter, BluetoothLeScanner btScanner) {
        bluetoothAdapter = btAdapter;
        if (bluetoothAdapter == null) {
            Log.d("TAG", "Bluetooth: Device Dosent support Bluetooth");
            // Device doesn't support Bluetooth
        }
        handler = new Handler();

        bluetoothLeScanner = btScanner;
        Log.d("TAGOHAD", "Bluetooth: Calling ScanLeDevice");
        scanLeDevice();
    }

    private void scanLeDevice() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            Log.d("TAGOHAD", "scanLeDevice: Starting to scan");
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }
}
