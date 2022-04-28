package com.example.myway;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.myway.Model.Model;
import com.example.myway.Model.MyWayMap;

import java.util.ArrayList;
import java.util.List;


public class MapSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;
    private Button selectBtn;
    private Spinner selectSpinner;
    private List<MyWayMap> myMaps;
    private MyWayMap choosenMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_select, container, false);
        selectBtn = view.findViewById(R.id.map_select_btn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                MapSelectFragmentDirections.ActionMapSelectFragmentToMainActivity2 action =
//                        MapSelectFragmentDirections.actionMapSelectFragmentToMainActivity2(choosenMap.getName());
//                Navigation.findNavController(view).navigate(action);

                Intent i=new Intent(getContext(),MainActivity2.class);
                i.putExtra("myMapName", choosenMap.getName());
                getContext().startActivity(i);
            }
        });
        selectSpinner = view.findViewById(R.id.map_select_spinner);
        Model.instance.getAllMaps(new Model.GetAllMapsListener() {
            @Override
            public void onComplete(List<MyWayMap> maps) {
                myMaps=maps;
                buildSpinner();
            }
        });

        return view;
    }

    private void buildSpinner() {
        String[] list =new String[myMaps.size()];
        for(int i = 0 ; i < myMaps.size() ; i++){
            list[i] = myMaps.get(i).getName();
        }
        ArrayAdapter ad
                = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        selectSpinner.setAdapter(ad);
        selectSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        choosenMap = myMaps.get(i);
        Log.d("TAG", "onItemSelected: "+choosenMap.getName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("TAG", "onNothingSelected: ");
    }
}