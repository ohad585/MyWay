package com.example.myway;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.myway.Model.BluetoothRep;
import com.example.myway.Model.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressLint("MissingPermission")
public class Bluetooth {
    private boolean scanning;
    private Handler handler ;
    BluetoothAdapter bluetoothAdapter;
    private List<String> devicesToBeFound;
    private HashMap<String, BluetoothRep> devicesFound;
    private ReentrantLock devicesFoundLock;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 2000;
    private static final long SCAN_INTERVAL = 3000;
    private static final long RESET_INTERVAL = 20000;


    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            String addr = bluetoothDevice.getName();
            if(addr != null){
                if(devicesToBeFound.contains(addr)){
                    try {
                        devicesFoundLock.lock();
                        Log.d("TAG", "onLeScan: "+bluetoothDevice.getName());
                        devicesFound.put(addr,new BluetoothRep(bluetoothDevice,i));
                    }finally {
                        devicesFoundLock.unlock();
                    }

                }
            }
        }

    };


    public Bluetooth(BluetoothAdapter btAdapter) {
        devicesFoundLock = new ReentrantLock();
        bluetoothAdapter = btAdapter;
        devicesFound = new LinkedHashMap<>();
        if (bluetoothAdapter == null) {
            Log.d("TAG", "Bluetooth: Device Dosent support Bluetooth");
            // Device doesn't support Bluetooth
        }
        handler = new Handler();
        Model.instance.getBluetoothDevices(new Model.getBluetoothDevicesListener() {
            @Override
            public void onComplete(List<String> devices) {
                devicesToBeFound = devices;
            }
        });

    }

    //Will run scan every SCAN_INTERVAL
    public void runScan(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanLeDevice();

                runScan();
            }
        },SCAN_INTERVAL);
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
    public interface getDevicesFoundListener {
        void onComplete(HashMap<String, BluetoothRep> devices);
    }
    public void getDevicesFound(getDevicesFoundListener listener){
        try {
            devicesFoundLock.lock();
            listener.onComplete(devicesFound);
        }finally {
            devicesFoundLock.unlock();
        }

    }

    public void resetBeaconsFound(){
        handler.postDelayed(new Runnable() {
            public void run() {
                try{
                    devicesFoundLock.lock();
                    devicesFound = new LinkedHashMap<>();
                }finally {
                    devicesFoundLock.unlock();
                }
                handler.postDelayed(this, RESET_INTERVAL);
            }
        }, RESET_INTERVAL);
    }
}
