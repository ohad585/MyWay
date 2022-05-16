package com.example.myway;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

public class map_key_fragment extends Fragment {
    CheckBox bench;
    CheckBox cafeteria;
    CheckBox synagogue;
    CheckBox garbage;
    CheckBox stairs;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_key_fragment, container, false);

        bench = view.findViewById(R.id.mapkey_bench_cb);
        cafeteria = view.findViewById(R.id.mapkey_cafeteria_cb);
        synagogue = view.findViewById(R.id.mapkey_synagogue_cb);
        stairs = view.findViewById(R.id.mapkey_stairs_cb);
        garbage = view.findViewById(R.id.mapkey_garbage_cb);
        for(int i=0;i<5;i++){
            switch(i) {
                case 0:
                        bench.setChecked(MyApplication.mapKeySettings[i]);
                    break;
                case 1:
                        synagogue.setChecked(MyApplication.mapKeySettings[i]);
                    break;
                case 2:
                    stairs.setChecked(MyApplication.mapKeySettings[i]);
                    break;
                case 3:
                    garbage.setChecked(MyApplication.mapKeySettings[i]);
                    break;
                case 4:
                    cafeteria.setChecked(MyApplication.mapKeySettings[i]);
                    break;
            }
        }

        bench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+ bench.isChecked());
                MyApplication.setMapKeySettings(0,bench.isChecked());
            }
        });
        synagogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+ synagogue.isChecked());
                MyApplication.setMapKeySettings(1,synagogue.isChecked());
            }
        });
        stairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+ stairs.isChecked());
                MyApplication.setMapKeySettings(2,stairs.isChecked());
            }
        });
        garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+ garbage.isChecked());
                MyApplication.setMapKeySettings(3,garbage.isChecked());
            }
        });
        cafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: "+ cafeteria.isChecked());
                MyApplication.setMapKeySettings(4,cafeteria.isChecked());
            }
        });

        return view;
    }
}
