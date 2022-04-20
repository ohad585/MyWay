package com.example.myway.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class IBeacon {
    private final static String UID = "UID";
    private final static String NAME = "NAME";
    private final static String LTD = "LATITUDE";
    private final static String LON = "LONGITUDE";

    private String uid;
    private String name;
    private double latitude;
    private double longitude;

    public IBeacon(String id,String name,double lat,double longit){
        this.uid = id;
        this.name = name;
        this.latitude = lat;
        this.longitude = longit;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(UID, this.uid);
        json.put(NAME, this.name);
        json.put(LON, this.longitude);
        json.put(LTD, this.latitude);
        return json;
    }

    static IBeacon fromJson(Map<String,Object> json){
        String uid = (String)json.get(UID);
        if (uid == null){
            return null;
        }
        String name = (String) json.get(NAME);
        double longitude = (double) json.get(LON);
        double latitude = (double) json.get(LTD);
        IBeacon b = new IBeacon(uid,name,latitude,longitude);
        return b;
    }

}
