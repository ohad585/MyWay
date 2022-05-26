package com.example.myway.Model;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Room {
    static final String TOP_LEFT_X="TOP_LEFT_X";
    static final String TOP_LEFT_Y="TOP_LEFT_Y";
    static final String TOP_RIGHT_X="TOP_RIGHT_X";
    static final String TOP_RIGHT_Y="TOP_RIGHT_Y";
    static final String BOT_LEFT_X="BOT_LEFT_X";
    static final String BOT_LEFT_Y="BOT_LEFT_Y";
    static final String BOT_RIGHT_X="BOT_RIGHT_X";
    static final String BOT_RIGHT_Y="BOT_RIGHT_Y";
    static final String DETAILS = "DETAILS";
    static final String FILL_COLOR="FILL_COLOR";
    static final String FLOOR="FLOOR";
    static final String EXTRA_DETAILS = "EXTRA_DETAILS";

    private LatLng topLeft;
    private LatLng topRight;
    private LatLng botRight;
    private LatLng botLeft;
    private int fillColor = 0xffd62d20;
    private String details;
    private boolean isDeleted;
    private Long floor;
    private String extraDetails;

    public Room(){
    }

    public Room(Polygon poly){
        List<LatLng> points = poly.getPoints();
        topLeft = points.get(0);
        topRight = points.get(1);
        botRight = points.get(2);
        botLeft = points.get(3);
        details = poly.getTag().toString();

    }

    public Room(LatLng tl,LatLng tr,LatLng br,LatLng bl,String det){
        topLeft=tl;
        topRight=tr;
        botRight=br;
        botLeft=bl;
        details=det;
        isDeleted=false;
    }

    public int getFillColor() {
        return fillColor;
    }
    public LatLng getBotLeft() {
        return botLeft;
    }
    public LatLng getBotRight() {
        return botRight;
    }
    public LatLng getTopLeft() {
        return topLeft;
    }
    public LatLng getTopRight() {
        return topRight;
    }
    public String getDetails() {
        return details;
    }
    public void setBotLeft(LatLng botLeft) {
        this.botLeft = botLeft;
    }
    public void setBotRight(LatLng botRight) {
        this.botRight = botRight;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
    public void setTopLeft(LatLng topLeft) {
        this.topLeft = topLeft;
    }
    public void setTopRight(LatLng topRight) {
        this.topRight = topRight;
    }
    public long getFloor() {
        return floor;
    }
    public void setFloor(long floor) {
        this.floor = floor;
    }

    public String getExtraDetails() {
        return this.extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public PolygonOptions retPolygonOptions(){
        return new PolygonOptions().add(topLeft,topRight,botRight,botLeft)
                .strokeColor(Color.BLACK)
                .fillColor(fillColor);
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(TOP_LEFT_X, topLeft.latitude);
        json.put(TOP_LEFT_Y, topLeft.longitude);
        json.put(TOP_RIGHT_X, topRight.latitude);
        json.put(TOP_RIGHT_Y, topRight.longitude);
        json.put(BOT_RIGHT_X, botRight.latitude);
        json.put(BOT_RIGHT_Y, botRight.longitude);
        json.put(BOT_LEFT_X, botLeft.latitude);
        json.put(BOT_LEFT_Y, botLeft.longitude);
        json.put(DETAILS,details);
        json.put(FLOOR,floor);
        json.put(EXTRA_DETAILS,extraDetails);
        //json.put(FILL_COLOR,fillColor);
        return json;
    }

    static Room fromJson(Map<String,Object> json){
        String det = (String)json.get(DETAILS);
        if (det == null){
            return null;
        }
        Long floor = (Long)json.get(FLOOR);
        LatLng tl = new LatLng((double)json.get(TOP_LEFT_X),(double) json.get(TOP_LEFT_Y));
        LatLng tr = new LatLng((double)json.get(TOP_RIGHT_X),(double) json.get(TOP_RIGHT_Y));
        LatLng br = new LatLng((double)json.get(BOT_RIGHT_X),(double) json.get(BOT_RIGHT_Y));
        LatLng bl = new LatLng((double)json.get(BOT_LEFT_X),(double) json.get(BOT_LEFT_Y));
        String extra = (String)json.get(EXTRA_DETAILS);
        //int fc = (int)json.get(FILL_COLOR);
        Room r = new Room(tl,tr,br,bl,det);
        r.setFloor(floor);
        r.setExtraDetails(extra);
        return r;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}
