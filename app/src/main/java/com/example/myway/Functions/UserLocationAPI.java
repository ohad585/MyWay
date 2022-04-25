package com.example.myway.Functions;

import static java.lang.Math.pow;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.myway.Bluetooth;
import com.example.myway.Model.BluetoothRep;
import com.example.myway.Model.IBeacon;
import com.example.myway.Model.Model;
import com.example.myway.Model.Room;
import com.example.myway.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserLocationAPI {
    private LatLng userLocation;
    private List<Room> allRooms;
    private HashMap<String,BluetoothRep> iBeaconsInRange;
    private List<IBeacon> allBeacons;
    private GoogleMap googleMap;
    private Bluetooth bleAPI;
    private Resources appRes;
    private Marker userMarker;

    private final LatLng SAMISHAMOON = new LatLng(31.80693, 34.65828);
    private final int ICON_SIZE = 72;


    public UserLocationAPI(GoogleMap gm, Bluetooth ble, Resources resources){
        appRes = resources;
        googleMap=gm;
        bleAPI = ble;
        userLocation = SAMISHAMOON;
        setUserIcon();
        Model.instance.getAllRooms(new Model.GetAllRoomsListener() {
            @Override
            public void onComplete(List<Room> roomList) {
                allRooms=roomList;
                Model.instance.getAllBeacons(new Model.GetAllBeaconsListener() {
                    @Override
                    public void onComplete(List<IBeacon> beacons) {
                        allBeacons = beacons;
                        init();
                    }
                });
            }
        });

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setUserIcon(){

        Drawable circleDrawable = appRes.getDrawable(R.drawable.user_location_icon);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        userMarker = googleMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("user")
                .icon(markerIcon)
                .anchor(0, 0)
        );
    }

    private void updateUserIconLocation(){
        userMarker.setPosition(userLocation);
    }

    public void init(){
        bleAPI.getDevicesFound(new Bluetooth.getDevicesFoundListener() {
            @Override
            public void onComplete(HashMap<String, BluetoothRep> devices) {
                iBeaconsInRange = devices;
                calcUserLocationNearestBeacon();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void calcUserLocationNearestBeacon(){
        if(iBeaconsInRange == null){
            Log.d("TAG", "calcUserLocation: iBeaconsInRange is null ");
            return;
        }
        List<String> keys = new LinkedList<>();
        for(String key:iBeaconsInRange.keySet()){
            keys.add(key);
        }

        if(iBeaconsInRange.size()==1){
            for(IBeacon beacon:allBeacons){
                if(beacon.getUid().matches((iBeaconsInRange.get(keys.get(0))).getDevice().getName())){
                    double x = beacon.getLatitude();
                    double y = beacon.getLongitude();
                    userLocation = new LatLng(y,x);
                    updateUserIconLocation();
                    return;
                }
            }
        }else {
            double x,y,r;
            double tempx,tempy,tempr;
            tempr=1000;
            tempx=1;
            tempy=1;
            x=1;
            y=1;
            r=Integer.MAX_VALUE;
            for(int i = 0;i<iBeaconsInRange.size();i++){
                for(IBeacon iBeacon : allBeacons){
                    if(iBeacon.getUid().matches(iBeaconsInRange.get(keys.get(i)).getDevice().getName())){
                        tempx=iBeacon.getLatitude();
                        tempy =iBeacon.getLongitude();
                        tempr = iBeaconsInRange.get(keys.get(i)).getDistance();
                        break;
                    }
                }
                if(tempr<r){
                    x=tempx;
                    y=tempy;
                    r=tempr;
                }
            }
            if(x!=1 && y!=1){
                userLocation = new LatLng(y,x);
                updateUserIconLocation();
            }else Log.d("TAG", "calcUserLocationNearestBeacon: X,Y didnt changed");
        }
    }

    @SuppressLint("MissingPermission")
    private void calcUserExactLocation() {

        if(iBeaconsInRange == null){
            Log.d("TAG", "calcUserLocation: iBeaconsInRange is null ");
            return;
        }
        if(iBeaconsInRange.size()<2){
            userLocation = SAMISHAMOON;
            setUserIcon();
        }else {
            if(iBeaconsInRange.size()==2){


            }else {
                //iBeacons size is 3 or more, we will only use first 3 .
                List<String> keys = new LinkedList<>();
                for(String key:iBeaconsInRange.keySet()){
                    keys.add(key);
                }

                Log.d("TAG", "calcUserLocation: "+keys.toString());
                double xa,ya,ra,xb,yb,rb,xc,yc,rc;
                xa =1 ;
                ya =1 ;
                ra =2 ;
                xb =1 ;
                yb =1 ;
                rb =2 ;
                xc =1 ;
                yc =1 ;
                rc =2 ;

                for(IBeacon iBeacon : allBeacons){
                    if(iBeacon.getUid().matches((iBeaconsInRange.get(keys.get(0))).getDevice().getName())){
                        xa = iBeacon.getLatitude();
                        ya = iBeacon.getLongitude();
                        ra = (iBeaconsInRange.get(keys.get(0))).getDistance();
                        continue;
                    }
                    if(iBeacon.getUid().matches((iBeaconsInRange.get(keys.get(1))).getDevice().getName())){
                        xb = iBeacon.getLatitude();
                        yb = iBeacon.getLongitude();
                        rb = (iBeaconsInRange.get(keys.get(1))).getDistance();
                        continue;
                    }
                    if(iBeacon.getUid().matches((iBeaconsInRange.get(keys.get(2))).getDevice().getName())){
                        xc = iBeacon.getLatitude();
                        yc = iBeacon.getLongitude();
                        rc = (iBeaconsInRange.get(keys.get(2))).getDistance();
                    }
                }

                double S = (pow(xc, 2.) - pow(xb, 2.) + pow(yc, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(rc, 2.)) / 2.0;
                double T = (pow(xa, 2.) - pow(xb, 2.) + pow(ya, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(ra, 2.)) / 2.0;
                double y = ((T * (xb - xc)) - (S * (xb - xa))) / (((ya - yb) * (xb - xc)) - ((yc - yb) * (xb - xa)));
                double x = ((y * (ya - yb)) - T) / (xb - xa);

                userLocation = new LatLng(y*10,x*10);
                updateUserIconLocation();
            }

        }

    }


}