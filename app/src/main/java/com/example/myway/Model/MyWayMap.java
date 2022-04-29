package com.example.myway.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class MyWayMap {
    private final static String NAME = "NAME";
    private final static String LTD = "LATITUDE";
    private final static String LON = "LONGITUDE";

    private String name;
    private LatLng latLng;

    public MyWayMap(String n,LatLng l){
        name = n;
        latLng = l;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(NAME, this.name);
        json.put(LON, this.latLng.longitude);
        json.put(LTD, this.latLng.latitude);
        return json;
    }

    static MyWayMap fromJson(Map<String,Object> json){
        String name = (String)json.get(NAME);
        if (name == null){
            return null;
        }
        double longitude = (double) json.get(LON);
        double latitude = (double) json.get(LTD);
        MyWayMap m = new MyWayMap(name,new LatLng(latitude,longitude));
        return m;
    }
}
