package com.example.myway;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myway.Functions.UserLocationAPI;
import com.example.myway.Model.Model;
import com.example.myway.Model.MyWayMap;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;
import com.example.myway.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static UserLocationAPI userLocationAPI;
    private View view;
    private final int ICON_SIZE = 60;
    public static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private final int CAMERA_ZOOM = 19;
    Bluetooth bleInterface;
    private Button micBtn;
    private static Button stopNavBtn;
    public static GoogleMap googleMap;
    private TextView instructionTV;
    private RoomGraph g;
    private MyWayMap myMap;
    private String name = null;
    public static TextToSpeech textToSpeech;
    private User myUser;
    View mapView;
    private ImageView center_on_user;
    private List<Marker> bMarkers;
    private List<Marker> synMarkers;
    private List<Marker> stairsMarkers;
    private List<Marker> gMarkers;
    private List<Marker> cafMarkers;
    private SupportMapFragment mapFragment;
    private ProgressBar progBar;
    private int currentFloor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        instructionTV = view.findViewById(R.id.instruction_map_fragment);
        mapView = view.findViewById(R.id.map);
        micBtn = view.findViewById(R.id.maps_mic_btn);
        stopNavBtn = view.findViewById(R.id.maps_stop_nav_btn);
        center_on_user = view.findViewById(R.id.maps_center_on_user_img);
        progBar = view.findViewById(R.id.maps_progBar);
        progBar.setVisibility(View.INVISIBLE);
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        center_on_user.setClickable(true);
        center_on_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: CENTER ON IMAGE CLICKED");
                LatLng temp = null;
                temp = userLocationAPI.getCurrentUserLocation();
                if (temp == null) {
                    temp = myMap.getLatLng();
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(temp));

            }
        });
        stopNavBtn.setVisibility(View.INVISIBLE);
        stopNavBtn.setClickable(false);
        stopNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity2) (getActivity())).stopNavigation();
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

        setHasOptionsMenu(true);
        return view;
    }

    public static void showStopBtn() {
        stopNavBtn.setClickable(true);
        stopNavBtn.setVisibility(View.VISIBLE);
    }

    public static void hideStopBtn() {
        stopNavBtn.setVisibility(View.INVISIBLE);
        stopNavBtn.setClickable(false);
    }

    private void blindSetup() {
        instructionTV = view.findViewById(R.id.instruction_map_fragment);
        center_on_user.setVisibility(View.INVISIBLE);
        center_on_user.setClickable(false);
        instructionTV.setVisibility(View.INVISIBLE);
        mapView.setVisibility(View.INVISIBLE);
        mapView.setClickable(false);
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast.makeText(getContext(), " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void preSetup() {
        if (name == null) {
            name = "Sami Shamoon College of engineering";
        }
        Model.instance.getMapByName(name, new Model.GetMapByNameListener() {
            @Override
            public void onComplete(MyWayMap map) {
                myMap = map;
                if (myUser.isBlind()) {
                    //User Is Blind
                    blindSetup();
                } else {
                    //User can see
                    micBtn.setClickable(false);
                    micBtn.setVisibility(View.INVISIBLE);
                }
                setup();
            }
        });
    }

    private void setup() {
        if (mapFragment == null) {
            mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
            g = new RoomGraph();
        }

//        instructionTV = view.findViewById(R.id.instruction_map_fragment);
//        instructionTV.setText(NavAlg.instance.arrayListOfInstruction().get(0));

    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;
        if (myUser.isBlind()) {
            mapView.setVisibility(View.INVISIBLE);
        }
        List<Polygon> polygonList = new LinkedList<>();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.addMarker(new MarkerOptions().position(myMap.getLatLng()).title(myMap.getName()));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(CAMERA_ZOOM));
        bleInterface = ((MainActivity2) getActivity()).getBluthoothInterface();
        userLocationAPI = new UserLocationAPI(googleMap, bleInterface, getResources());
        ((MainActivity2) getActivity()).setupAPI(userLocationAPI);
        Model.instance.getAllRooms(new Model.GetAllRoomsListener() {
            @Override
            public void onComplete(List<Room> roomList) {
                for (Room r : roomList) {
                    if (r.getFloor() == currentFloor) {
                        Polygon p = googleMap.addPolygon(r.retPolygonOptions());
                        p.setClickable(true);
                        p.setTag(r.getDetails());
                        polygonList.add(p);
                    }
                }
                drawExtraMarkers(true);
            }
        });
        //drawPath();
    }

    private void drawExtraMarkers(boolean first) {
        Log.d("TAG", "drawExtraMarkers");
        bMarkers = new LinkedList<>();
        gMarkers = new LinkedList<>();
        cafMarkers = new LinkedList<>();
        stairsMarkers = new LinkedList<>();
        synMarkers = new LinkedList<>();
        Drawable circleDrawable;
        BitmapDescriptor markerIcon;
        if (MyApplication.mapKeySettings[2]) {
            circleDrawable = getResources().getDrawable(R.drawable.stairs);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker stairsMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80722, 34.65782))
                    .title("stairs")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            stairsMarkers.add(stairsMarker);
        }
        if (MyApplication.mapKeySettings[1]) {
            circleDrawable = getResources().getDrawable(R.drawable.magen_david_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker synagogueMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80713, 34.65805))
                    .title("synagogue")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            synMarkers.add(synagogueMarker);
        }
        if (MyApplication.mapKeySettings[4]) {
            circleDrawable = getResources().getDrawable(R.drawable.cafeteria_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker cafeteria = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80719, 34.65808))
                    .title("cafeteria")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            cafMarkers.add(cafeteria);
        }
        if (MyApplication.mapKeySettings[3]) {
            circleDrawable = getResources().getDrawable(R.drawable.garbage_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker garbage = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80716, 34.65799))
                    .title("garbage")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            gMarkers.add(garbage);
        }
        if (MyApplication.mapKeySettings[0]) {
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench1 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80704, 34.65821))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench1);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench2 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80709, 34.65823))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench3 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80705, 34.6581))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench4 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.8069, 34.65847))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench5 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80705, 34.6581))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench6 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80705, 34.6581))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
            circleDrawable = getResources().getDrawable(R.drawable.bench_icon);
            markerIcon = getMarkerIconFromDrawable(circleDrawable);
            Marker bench7 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(31.80722, 34.65797))
                    .title("bench")
                    .icon(markerIcon)
                    .anchor(0, 0)
            );
            bMarkers.add(bench2);
        }
        MyApplication.setMapKeySettings(5, false);
        if (first) {
            onRoomsReady();
        }
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
                        DialogFragment newFragment = new DialogRoomDetails(room, myUser);
                        newFragment.show(getActivity().getSupportFragmentManager(), "TAG");

                    }
                });

            }

        });

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
        PolylineOptions rectOptions = new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(roomA.getDoorY(), roomA.getDoorX()),
                        new LatLng(roomB.getDoorY(), roomB.getDoorX()))
                .width(20)
                .color(Color.BLACK);
        Polyline polyline = googleMap.addPolyline(rectOptions);
        Log.d("drawPath", "draw polyline between " + roomA.getRoom() + " and " + roomB.getRoom());

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
                                 @Nullable Intent data) {
        Log.d("TAGO", "onActivityResult: ++++++++++++++++++");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapsFragment.REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Log.d("TAG", "onActivityResult: " + Objects.requireNonNull(result).get(0));
                ((MainActivity2) getActivity()).startNavigation(Objects.requireNonNull(result).get(0));
            }
        }
    }

    public static UserLocationAPI getUserLocationAPI() {
        return userLocationAPI;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) {

        }

    }


    //    @Override
//    public void onResume() {
//        Log.d("TAG", "onResume: +++++++++++++++++++++++++++++++");
//        super.onResume();
//        if (MyApplication.mapKeySettings[5]) {
////            for (Marker m : markers) {
////                //m.remove();
////                m.setVisible(false);
////            }
//            //drawExtraMarkers(false);
//            if(MyApplication.mapKeySettings[0]){
//                for(Marker m:bMarkers){
//                    m.setVisible(false);
//                }
//            }else {
//                for(Marker m :bMarkers){
//                    m.setVisible(true);
//                }
//            }
//            if(MyApplication.mapKeySettings[1]){
//                for(Marker m:synMarkers){
//                    m.setVisible(false);
//                }
//            }else {
//                for(Marker m :synMarkers){
//                    m.setVisible(true);
//                }
//            }
//            if(MyApplication.mapKeySettings[2]){
//                for(Marker m:stairsMarkers){
//                    m.setVisible(false);
//                }
//            }else {
//                for(Marker m :stairsMarkers){
//                    m.setVisible(true);
//                }
//            }
//            if(MyApplication.mapKeySettings[3]){
//                for(Marker m:gMarkers){
//                    m.setVisible(false);
//                }
//            }else {
//                for(Marker m :gMarkers){
//                    m.setVisible(true);
//                }
//            }
//            if(MyApplication.mapKeySettings[4]){
//                for(Marker m:cafMarkers){
//                    m.setVisible(false);
//                }
//            }else {
//                for(Marker m :cafMarkers){
//                    m.setVisible(true);
//                }
//            }
//            MyApplication.setMapKeySettings(5,false);
//        }
//
//    }

    private void leave() {
        center_on_user.setClickable(false);
        stopNavBtn.setClickable(false);
        mapView.setClickable(false);
        progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        leave();
        Log.d("+++++TAG", "onOptionsItemSelected: ");
        return super.onOptionsItemSelected(item);
    }
}