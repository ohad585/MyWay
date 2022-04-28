package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_select, container, false);
        selectBtn = view.findViewById(R.id.map_select_btn);
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
        List<String> list = new ArrayList<String>();
        for(MyWayMap m :myMaps){
            list.add(m.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapSelectFragment.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}