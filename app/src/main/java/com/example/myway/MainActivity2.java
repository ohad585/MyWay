package com.example.myway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.myway.Functions.UserLocationAPI;
import com.example.myway.Model.Model;
import com.example.myway.Model.MyWayMap;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Bluetooth bleInterface;
    private UserLocationAPI userLocationAPI;
    private Resources appRes;
    private final int ICON_SIZE = 90;
    private Button checkloc_btn;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST = 1;
    private final int CAMERA_ZOOM = 19;

    private GoogleMap googleMap;
    private TextView instructionTV;
    private RoomGraph g;
    private MyWayMap myMap;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //RoomGraph g = new RoomGraph();
        checkloc_btn = findViewById(R.id.checkloc_btn);
        checkloc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocationAPI.init();
            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("myMapName");
        Model.instance.getMapByName(name, new Model.GetMapByNameListener() {
            @Override
            public void onComplete(MyWayMap map) {
                myMap = map;
                checkPermissions();
            }
        });
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST);
            }else setupBluetooth();
        }
    }

    private void setupBluetooth() {

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        if ((btAdapter != null && !btAdapter.isEnabled())) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        bleInterface = new Bluetooth(btAdapter);
        setup();
    }


    private void setup() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        g = new RoomGraph();
     Log.d("TAGLiron1", "onCreate: " + NavAlg.instance.Dijkstra(g.getRoomByName("167"), g.getRoomByName("cafeteria")));
//        Log.d("TAGLiron2",""+NavAlg.instance.arrayListOfRooms());
//        Log.d("TAGLiron3",""+NavAlg.instance.arrayListOfInstruction());

        Log.d("TAG123", "onCreate: " + NavAlg.instance.Dijkstra(g.getRoomByName("168"), g.getRoomByName("J2")));
        instructionTV = findViewById(R.id.instruction_mainactivity);
        instructionTV.setText(NavAlg.instance.arrayListOfInstruction().get(0));
    }


    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap=googleMap1;
        List<Polygon> polygonList=new LinkedList<>();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(myMap.getLatLng()).title(myMap.getName()));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(CAMERA_ZOOM));
        Model.instance.getAllRooms(new Model.GetAllRoomsListener() {
            @Override
            public void onComplete(List<Room> roomList) {
                for(Room r:roomList){
                    Polygon p = googleMap.addPolygon(r.retPolygonOptions());
                    p.setClickable(true);
                    p.setTag(r.getDetails());
//                    Marker marker = googleMap.addMarker(
//                            new MarkerOptions()
//                                    .position((LatLng) p.getPoints().get(0))
//                                    .title(r.getDetails()));
                    polygonList.add(p);
                }
                onRoomsReady();
            }
        });
        drawPath();
        Drawable circleDrawable = getResources().getDrawable(R.drawable.stairs);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker stairsMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80722, 34.65782))
                .title("stairs")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.magen_david_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker synagogueMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80713, 34.65805))
                .title("synagogue")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.cafeteria_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker cafeteria = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80719, 34.65808))
                .title("cafeteria")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.garbage_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker garbage = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80716, 34.65799))
                .title("garbage")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80704, 34.65821))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80709, 34.65823))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80705, 34.6581))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench4 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.8069, 34.65847))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench5 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80705, 34.6581))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench6 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80705, 34.6581))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
        circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker bench7 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.80722, 34.65797))
                .title("bench")
                .icon(markerIcon)
                .anchor(0, 0)
        );
    }



    private void onRoomsReady() {
        bleInterface.runScan();

        // Display traffic.
        //googleMap.setTrafficEnabled(true);
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


//        List<IBeacon> beacons = new LinkedList<>();
//        beacons.add(new IBeacon("000","168",34.65844,31.80686));
//        beacons.add(new IBeacon("001","J1",34.65828,31.80693));
//        beacons.add(new IBeacon("002","J2",34.65836,31.80704));
//        beacons.add(new IBeacon("003","J3",34.65824,31.80702));
//        beacons.add(new IBeacon("004","161",34.65785,31.80723));
//        beacons.add(new IBeacon("005","163",34.65797,31.80718));
//        beacons.add(new IBeacon("006","165",34.65814,31.80714));
//        beacons.add(new IBeacon("007","166A",34.65829,31.80706));
//        beacons.add(new IBeacon("008","Fr",0,0));
//        beacons.add(new IBeacon("009","Fr",0,0));
//
//        for(IBeacon b : beacons){
//            Model.instance.saveIBeacon(b, new Model.SaveIBeaconListener() {
//                @Override
//                public void onComplete() {
//                    Log.d("TAG", "onComplete: IBeacon "+b.getName()+" saved");
//                }
//            });
//        }
        userLocationAPI = new UserLocationAPI(googleMap,bleInterface,getResources());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myMap.getLatLng()));
    }

    private void drawPath() {

        for (int i = 0; i < NavAlg.instance.arrayListOfRooms().size() - 1; i++) {
            drawPolylineBetween2Rooms(
                    g.getRoomByName(NavAlg.instance.arrayListOfRooms().get(i)),
                    g.getRoomByName(NavAlg.instance.arrayListOfRooms().get(i + 1)));
        }
    }

    private void drawPolylineBetween2Rooms(RoomGraph.RoomRepresent roomA, RoomGraph.RoomRepresent roomB) {
        PolylineOptions rectOptions=new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng( roomA.getDoorY(),roomA.getDoorX()),
                        new LatLng( roomB.getDoorY(),roomB.getDoorX()))
                .width(20)
                .color(Color.BLACK);
        Polyline polyline = googleMap.addPolyline(rectOptions);
        Log.d("drawPath","draw polyline between "+roomA.getRoom()+" and "+roomB.getRoom());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length == 0) {
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST:
                setupBluetooth();
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}