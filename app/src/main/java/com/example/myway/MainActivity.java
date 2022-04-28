package com.example.myway;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myway.Model.NavAlg;
import com.example.myway.Model.RoomGraph;

public class MainActivity extends AppCompatActivity {
    NavController navCtrl;
    androidx.appcompat.widget.SearchView editsearch;
    String searchString;
    RoomGraph g = new RoomGraph();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtrl = nav_host.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCtrl);
        editsearch = (SearchView) findViewById(R.id.menu_app_bar_search);

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
                RoomGraph.RoomRepresent currentLocation=g.getRoomByName("167"); //change to current location
                RoomGraph.RoomRepresent destination=g.getRoomByName(query);
                if (destination==null){
                    showDialogRoomDoesntFound();
                }
                else{
                    Log.d("TAGLiron1", "onCreate: " + NavAlg.instance.Dijkstra(currentLocation,destination));
                    Log.d("TAGLiron2",""+NavAlg.instance.arrayListOfRooms());
                    Log.d("TAGLiron3",""+NavAlg.instance.arrayListOfInstruction());
                }
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
                case R.id.menu_app_bar_search:
                    Log.d("TAGLIRON","KKK");
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
}