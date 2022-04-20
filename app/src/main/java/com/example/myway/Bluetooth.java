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
@SuppressLint("MissingPermission")
public class Bluetooth {
    private boolean scanning;
    private Handler handler ;
    BluetoothAdapter bluetoothAdapter;
    private List<String> devicesToBeFound;
    private HashMap<String, BluetoothRep> devicesFound;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final long SCAN_INTERVAL = 6000;
    private static final long RESET_INTERVAL = 6000;


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            String addr = bluetoothDevice.getAddress();
            if(addr != null){
                Log.d("TAG", "onLeScan: "+bluetoothDevice.getName());
                if(devicesToBeFound.contains(addr)){
                    devicesFound.put(addr,new BluetoothRep(bluetoothDevice,i));
                }
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
        devicesToBeFound = Model.instance.getBluetoothDevices();

    }

    //Will run scan every SCAN_INTERVAL
    private void runScan(){
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

    public HashMap<String, BluetoothRep> getDevicesFound(){
        return devicesFound;
    }

    public void resetBeaconsFound(){
        handler.postDelayed(new Runnable() {
            public void run() {
                //System.out.println("myHandler: here!"); // Do your work here
                devicesFound = new LinkedHashMap<>();
                handler.postDelayed(this, RESET_INTERVAL);
            }
        }, RESET_INTERVAL);
    }
}
