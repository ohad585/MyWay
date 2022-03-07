package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
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

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        RoomGraph g = new RoomGraph();
        Log.d("TAG123", "onCreate: "+NavAlg.instance.Dijkstra(g.getRoomByName("168"),g.getRoomByName("J2")));

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

    }



    private void drowPolylineBetween2Points(double pointAX,double pointAY,double pointBX,double pointBY) {
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(pointAX,pointAY),
                        new LatLng(pointBX,pointBY)));

    }


}