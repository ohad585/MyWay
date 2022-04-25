package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;


public class personal_page_fragment extends Fragment {
    User u;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                u=user;
            }
        });
        View view= inflater.inflate(R.layout.personal_page_fragment, container, false);
        return view;
    }
}