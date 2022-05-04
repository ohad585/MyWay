package com.example.myway;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.example.myway.Functions.UserLocationAPI;
import com.example.myway.Model.Model;
import com.example.myway.Model.NavAlg;
import com.example.myway.Model.RoomGraph;
import com.example.myway.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.regex.PatternSyntaxException;

public class MainActivity2 extends AppCompatActivity {
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Bluetooth bleInterface;
    private UserLocationAPI userLocationAPI;
    private final int ICON_SIZE = 90;
    private NavController navCtrl;
    private androidx.appcompat.widget.SearchView editsearch;
    private String searchString;
    User currentUser;//if user is logged in
    private TextView instructionTV;
    private LinkedList <Polyline> polylineLinkedList=new LinkedList<>();


    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST = 1;

    private RoomGraph g;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.base_navhost2);

        Intent intent = getIntent();
        String name = intent.getStringExtra("myMapName");
        Bundle bundle = new Bundle();
        bundle.putString("myMapName", name );
        navCtrl = nav_host.getNavController();
        navCtrl.setGraph(R.navigation.nav_graph2,bundle);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);
        editsearch = (SearchView) findViewById(R.id.menu_app_bar_search);
        handleIntent(getIntent());
        g = new RoomGraph();
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                currentUser=user;
            }
        });


        checkPermissions();
    }



    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST);
            }else setupBluetooth();
        }
    }

    @SuppressLint("MissingPermission")
    private void setupBluetooth() {

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        if ((btAdapter != null && !btAdapter.isEnabled())) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        bleInterface = new Bluetooth(btAdapter);


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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //use the query to search your data somehow
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString=query;
                //here enter code so query is add to logged in user history
                RoomGraph.RoomRepresent currentLocation=g.getRoomByName("167"); //change to current location of user
                RoomGraph.RoomRepresent destination=g.getRoomByName(query);
                if (destination==null){
                    showDialogRoomDoesntFound();
                }
                else{
                    Log.d("TAGLiron1", "onCreate: " + NavAlg.instance.Dijkstra(currentLocation,destination));
                    Log.d("TAGLiron2",""+NavAlg.instance.arrayListOfRooms());
                    Log.d("TAGLiron3",""+NavAlg.instance.arrayListOfInstruction());
                    Model.instance.addRoomToHistoryPlacesByUserMail(currentUser.getEmail(),destination);

                }
                instructionTV = findViewById(R.id.instruction_map_fragment);
                instructionTV.setText(NavAlg.instance.arrayListOfInstruction().get(0));
                drawPath();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navCtrl.navigateUp();
                return true;
            case R.id.profile:
                navCtrl.navigate(R.id.action_global_personal_page_fragment);
                return true;
            case R.id.menubar_favorites:
                navCtrl.navigate(R.id.action_global_favorite_places_fragment);

                return true;

            case R.id.menubar_history:
                navCtrl.navigate(R.id.action_global_places_history_fragment);
                return true;

            case R.id.menu_app_bar_search:
                SearchManager searchManager =
                        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView =
                        (SearchView)item.getActionView();
                searchView.setSearchableInfo(
                        searchManager.getSearchableInfo(getComponentName()));

                return true;

        }
        return true;
    }


    private void showDialogRoomDoesntFound() {
        DialogFragment newFragment = new DialogRoomNotFound();
        newFragment.show(getSupportFragmentManager(), "RoomNotFound");
    }
    private void drawPath() {
        for(int i = 0; i<polylineLinkedList.size(); i++){
            polylineLinkedList.get(i).setVisible(false);
            polylineLinkedList.get(i).remove();
        }
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
        Polyline polyline = MapsFragment.googleMap.addPolyline(rectOptions);
        polylineLinkedList.add(polyline);
        Log.d("drawPath","draw polyline between "+roomA.getRoom()+" and "+roomB.getRoom());

    }

}