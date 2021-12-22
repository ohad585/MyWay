package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class home_page_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.home_page_fragment, container, false);


        Button login_btn=view.findViewById(R.id.home_log_in_btn);
        Button reg_btn=view.findViewById(R.id.home_register_btn);
        login_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_page_fragment_to_log_in_fragment));
        reg_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_page_fragment_to_regrister_fragment));
        return view;
    }




}