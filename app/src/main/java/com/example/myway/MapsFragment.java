package com.example.myway;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myway.Model.Model;
import com.example.myway.Model.MyWayMap;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;
import com.example.myway.Model.User;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    private View view;
    private final int ICON_SIZE = 90;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private final int CAMERA_ZOOM = 19;

    private Button micBtn;
    private GoogleMap googleMap;
    private TextView instructionTV;
    private RoomGraph g;
    private MyWayMap myMap;
    private String name = null;
    private TextToSpeech textToSpeech;
    private User myUser;
    View mapView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = view.findViewById(R.id.map);
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        if (getArguments() != null) {
            name = getArguments().getString("myMapName");
        }

        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                myUser = user;
                preSetup();
            }
        });

        micBtn = view.findViewById(R.id.maps_mic_btn);
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(NavAlg.instance.arrayListOfInstruction().get(0).toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        return view;
    }

    private void preSetup() {
        if(myUser.isBlind()){
            //User Is Blind
            instructionTV.setVisibility(View.INVISIBLE);
                mapView.setVisibility(View.INVISIBLE);
                mapView.setClickable(false);
                micBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent
                            = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                            Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                    try {
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(), " " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }else {
            //User can see
            micBtn.setClickable(false);
            micBtn.setVisibility(View.INVISIBLE);
            Model.instance.getMapByName(name, new Model.GetMapByNameListener() {
                @Override
                public void onComplete(MyWayMap map) {
                    myMap = map;
                    setup();
                }
            });
        }
    }

    private void setup() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        g = new RoomGraph();
        Log.d("TAGLiron1", "onCreate: " + NavAlg.instance.Dijkstra(g.getRoomByName("167"), g.getRoomByName("cafeteria")));
//        Log.d("TAGLiron2",""+NavAlg.instance.arrayListOfRooms());
//        Log.d("TAGLiron3",""+NavAlg.instance.arrayListOfInstruction());

        Log.d("TAG123", "onCreate: " + NavAlg.instance.Dijkstra(g.getRoomByName("168"), g.getRoomByName("J2")));
        instructionTV = view.findViewById(R.id.instruction_map_fragment);
        instructionTV.setText(NavAlg.instance.arrayListOfInstruction().get(0));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

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
                drawExtraMarkers();
            }
        });
        //drawPath();
    }

    private void drawExtraMarkers(){
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
        onRoomsReady();
    }


    private void onRoomsReady() {
        // Display traffic.
        //googleMap.setTrafficEnabled(true);
        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            public void onPolygonClick(Polygon polygon) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
                Model.instance.GetRoomDetails(polygon, new Model.GetRoomDetailsListener() {
                    @Override
                    public void onComplete(Room room) {
                        DialogFragment newFragment = new DialogRoomDetails(room);
                        newFragment.show(getParentFragmentManager(), "TAG");
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        Log.d("TAGO", "onActivityResult: ++++++++++++++++++");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Log.d("TAG", "onActivityResult: "+Objects.requireNonNull(result).get(0));
            }
        }
    }

}