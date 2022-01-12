package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONException;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set the map coordinates to Sami shamoon ashdod.
        LatLng samiShamoon = new LatLng(31.80687, 34.65846);
        // Set the map type to Normal.
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker on the map coordinates.
        googleMap.addMarker(new MarkerOptions()
                .position(samiShamoon)
                .title("Sami"));
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(samiShamoon));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        Polygon room160 = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.8073, 34.65781),//Top left
                        new LatLng(31.80728, 34.65785),//Top right
                        new LatLng(31.8072, 34.65779),//Botom right
                        new LatLng(31.80722, 34.65775))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room160.setTag("Room 160");

        Polygon room161 = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80728, 34.65785),//Top left
                        new LatLng(31.80726, 34.6579),//Top right
                        new LatLng(31.80722, 34.65787),//Botom right
                        new LatLng(31.80724, 34.65782))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room161.setTag("Room 161");







        Polygon room162 = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80721, 34.65787),//Top left
                        new LatLng(31.80719, 34.65792),//Top right
                        new LatLng(31.80714, 34.65789),//Botom right
                        new LatLng(31.80717, 34.65784))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room162.setTag("Room 162");

        Polygon room163= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80719, 34.65793),//Top left
                        new LatLng(31.80716, 34.65799),//Top right
                        new LatLng(31.80711, 34.65796),//Botom right
                        new LatLng(31.80714, 34.6579))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room163.setTag("Room 163");

        Polygon room164= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80713, 34.65802),//Top left
                        new LatLng(31.80711, 34.65807),//Top right
                        new LatLng(31.80707, 34.65804),//Botom right
                        new LatLng(31.80709, 34.658))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room164.setTag("Room 164");

        Polygon room165= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80715, 34.6581),//Top left
                        new LatLng(31.80712, 34.65816),//Top right
                        new LatLng(31.80709, 34.65814),//Botom right
                        new LatLng(31.80712, 34.65808))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room165.setTag("Room 165");

        Polygon room166B= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80705, 34.65816),//Top left
                        new LatLng(31.80702, 34.65822),//Top right
                        new LatLng(31.80699, 34.65819),//Botom right
                        new LatLng(31.80702, 34.65814))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room166B.setTag("Room 166B");

        Polygon room166C= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80701, 34.65825),//Top left
                        new LatLng(31.80698, 34.65829),//Top right
                        new LatLng(31.80695, 34.65828),//Botom right
                        new LatLng(31.80697, 34.65823))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room166C.setTag("Room 166C");

        Polygon room166A= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80707, 34.65827),//Top left
                        new LatLng(31.80704, 34.65833),//Top right
                        new LatLng(31.80701, 34.65831),//Botom right
                        new LatLng(31.80704, 34.65825))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room166A.setTag("Room 166A");

        Polygon room167= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80697, 34.65834),//Top left
                        new LatLng(31.80694, 34.65839),//Top right
                        new LatLng(31.80689, 34.65836),//Botom right
                        new LatLng(31.80692, 34.65831))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room167.setTag("Room 167");

        Polygon room168= googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(31.80691, 34.65844),//Top left
                        new LatLng(31.80689, 34.65849),//Top right
                        new LatLng(31.80685, 34.65846),//Botom right
                        new LatLng(31.80687, 34.65842))//Bottom left
                .strokeColor(Color.BLACK)
                .fillColor(0x7F00FF00)
                .clickable(true));
        room168.setTag("Room 168");








        // Display traffic.
        googleMap.setTrafficEnabled(true);
        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            public void onPolygonClick(Polygon polygon) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
                Log.d("TAG", "onPolygonClick: Clicked");


            }
        });
    }



}