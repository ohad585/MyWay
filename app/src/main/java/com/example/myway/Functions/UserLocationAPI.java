package com.example.myway.Functions;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.myway.Bluetooth;
import com.example.myway.Model.BluetoothRep;
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
import java.util.List;

public class UserLocationAPI {
    private LatLng userLocation;
    private List<Room> allRooms;
    private HashMap<String,BluetoothRep> iBeacons;
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
        //iBeacons = bleAPI.getDevicesFound();
        iBeacons = new HashMap<>();
        Model.instance.getAllRooms(new Model.GetAllRoomsListener() {
            @Override
            public void onComplete(List<Room> roomList) {
                allRooms=roomList;
                calcUserLocation();
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

    private void calcUserLocation() {

        if(iBeacons.size()<2){
            userLocation = SAMISHAMOON;
            setUserIcon();
        }else {
            if(iBeacons.size()==2){


            }else {
                //iBeacons size is 3 or more, we will only use 3 .
                List<String> keys = (List<String>) iBeacons.keySet();
                Log.d("TAG", "calcUserLocation: "+keys.toString());
                //float xa = (iBeacons.get(keys.get(0))).getDevice().
                float ya = beacon1.locationY;
                float xb = beacon2.locationX;
                float yb = beacon2.locationY;
                float xc = beacon3.locationX;
                float yc = beacon3.locationY;
                float ra = beacon1.filteredDistance;
                float rb = beacon2.filteredDistance;
                float rc = beacon3.filteredDistance;

                float S = (pow(xc, 2.) - pow(xb, 2.) + pow(yc, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(rc, 2.)) / 2.0;
                float T = (pow(xa, 2.) - pow(xb, 2.) + pow(ya, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(ra, 2.)) / 2.0;
                float y = ((T * (xb - xc)) - (S * (xb - xa))) / (((ya - yb) * (xb - xc)) - ((yc - yb) * (xb - xa)));
                float x = ((y * (ya - yb)) - T) / (xb - xa);

                CGPoint point = CGPointMake(x, y);

            }

        }

    }


}
