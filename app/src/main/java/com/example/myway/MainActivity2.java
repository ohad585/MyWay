package com.example.myway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.myway.Model.Model;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;
import android.os.AsyncTask;


public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST = 1;

    GoogleMap googleMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //RoomGraph g = new RoomGraph();
        //Log.d("TAG123", "onCreate: "+NavAlg.instance.Dijkstra(g.getRoomByName("168"),g.getRoomByName("J2")));

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        checkPermissions();
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.FOREGROUND_SERVICE}, PERMISSION_REQUEST);
            }else setup();
        }
    }

    private void setup() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        GoogleMap.OnPolygonClickListener listener = null;
        googleMap=googleMap1;
        List<Polygon> polygonList=new LinkedList<>();
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
        Model.instance.getAllRooms(new Model.GetAllRoomsListener() {
            @Override
            public void onComplete(List<Room> roomList) {
                for(Room r:roomList){
                    //Log.d("TAG112", "onMapReady: "+r.getDetails());
                    Polygon p = googleMap.addPolygon(r.retPolygonOptions());
                    p.setClickable(true);
                    p.setTag(r.getDetails());
//                    Marker marker = googleMap.addMarker(
//                            new MarkerOptions()
//                                    .position((LatLng) p.getPoints().get(0))
//                                    .title(r.getDetails()));
                    polygonList.add(p);
                }
            }
        });


        drowPolylineBetween2Points(31.8072, 34.65801,31.80714, 34.65814);

        // Display traffic.
        googleMap.setTrafficEnabled(true);
        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            public void onPolygonClick(Polygon polygon) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
                Model.instance.GetRoomDetails(polygon, new Model.GetRoomDetailsListener() {
                    @Override
                    public void onComplete(Room room) {
                        DialogFragment newFragment = new DialogRoomDetails(room);
                        newFragment.show(getSupportFragmentManager(), "TAG");
                    }
                });

            }
        });
        new Bluetooth(btAdapter);
    }



    private void drowPolylineBetween2Points(double pointAX,double pointAY,double pointBX,double pointBY) {
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(pointAX,pointAY),
                        new LatLng(pointBX,pointBY)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(grantResults.length==0){
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST:
                setup();
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}