package com.example.myway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.myway.Model.IBeacon;
import com.example.myway.Model.Model;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Bluetooth bleInterface;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST = 1;

    GoogleMap googleMap;
    TextView instructionTV;
    RoomGraph g;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //RoomGraph g = new RoomGraph();
        //Log.d("TAG123", "onCreate: "+NavAlg.instance.Dijkstra(g.getRoomByName("168"),g.getRoomByName("J2")));

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        bleInterface = new Bluetooth(btAdapter);
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
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
        g = new RoomGraph();
        Log.d("TAG123", "onCreate: " + NavAlg.instance.Dijkstra(g.getRoomByName("168"), g.getRoomByName("J2")));
        //Log.d("TAGLiron2",""+NavAlg.instance.arrayListOfRooms());
        instructionTV = findViewById(R.id.instruction_mainactivity);
        instructionTV.setText(NavAlg.instance.arrayListOfInstruction().get(0));
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
        //drowPath();

        List<IBeacon> beacons = new LinkedList<>();
        beacons.add(new IBeacon("000","168",34.65844,31.80686));
        beacons.add(new IBeacon("001","J1",34.65828,31.80693));
        beacons.add(new IBeacon("002","J2",34.65836,31.80704));
        beacons.add(new IBeacon("003","J3",34.65824,31.80702));
        beacons.add(new IBeacon("004","161",34.65785,31.80723));
        beacons.add(new IBeacon("005","163",34.65797,31.80718));
        beacons.add(new IBeacon("006","165",34.65814,31.80714));
        beacons.add(new IBeacon("007","166A",34.65829,31.80706));
        beacons.add(new IBeacon("008","Fr",0,0));
        beacons.add(new IBeacon("009","Fr",0,0));

        for(IBeacon b : beacons){
            Model.instance.saveIBeacon(b, new Model.SaveIBeaconListener() {
                @Override
                public void onComplete() {
                    Log.d("TAG", "onComplete: IBeacon "+b.getName()+" saved");
                }
            });
        }
    }

    private void drowPath() {
        for (int i = 0; i < NavAlg.instance.arrayListOfRooms().size() - 2; i++) {
            drawPolylineBetween2Rooms(
                    g.getRoomByName(NavAlg.instance.arrayListOfRooms().get(i)),
                    g.getRoomByName(NavAlg.instance.arrayListOfRooms().get(i + 1)));
        }
    }

    private void drawPolylineBetween2Rooms(RoomGraph.RoomRepresent roomA, RoomGraph.RoomRepresent roomB) {
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(roomA.getDoorX(), roomA.getDoorY()),
                        new LatLng(roomB.getDoorX(), roomB.getDoorY())));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length == 0) {
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