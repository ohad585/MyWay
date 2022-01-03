package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myway.Model.Model;

public class log_in_fragment extends Fragment {
    TextView userName;
    TextView userPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.log_in_fragment, container, false);
        Button loginBtn=view.findViewById(R.id.log_in_log_in_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=view.findViewById(R.id.log_in_name_et);
                userPass=view.findViewById(R.id.log_in_password_et);
                String uName=userName.toString();
                String uPass=userPass.toString();

//                checkUser(uName,uPass);
            }
        });
        return view;
    }

//    public void checkUser(String name,String pass){
//        Model.instance.checkUser(name,pass);
//
//
//    }


}