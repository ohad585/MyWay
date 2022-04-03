package com.example.myway.Model;


import android.bluetooth.BluetoothDevice;

public class BluetoothRep {
    private BluetoothDevice device;
    private double distance;

    //Range 2-4 depends on signal strentgh
    public static int ENVIROMENTAL_FACTOR = 2;
    //Factory based value
    public static int RSSI_AT_ONE_METER = -3;


    public BluetoothRep(BluetoothDevice bDevice, int rssi){

        device = bDevice;
        distance = calcDistance(rssi);
    }

    private double calcDistance(int rssi) {
        double d ;
        if (rssi==0){
            return Integer.MAX_VALUE;
        }
        float temp = (RSSI_AT_ONE_METER - rssi)/(10*ENVIROMENTAL_FACTOR);
        d=Math.pow(10,temp);
        return d;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "BluetoothRep{" +
                "device=" + device +
                ", distance=" + distance +
                '}';
    }
}
