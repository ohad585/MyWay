package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;

public class log_in_fragment extends Fragment {
    View view;

    TextView userName;
    TextView userPass;
    String uName;
    String uPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.log_in_fragment, container, false);
        Button loginBtn=view.findViewById(R.id.log_in_log_in_btn);
        userName=view.findViewById(R.id.log_in_name_et);
        userPass=view.findViewById(R.id.log_in_password_et);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uName=userName.toString();
                uPass=userPass.toString();
                loginUser();

            }
        });
        return view;
    }

    public void loginUser(){
        Model.instance.signInWithEmailPass(uName,uPass,(User user, boolean success)->{
            if(success){
                Navigation.findNavController(view).navigate(R.id.action_log_in_fragment_to_mapSelectFragment);
            }
            else {
                Log.d("TAG", "loginUser: Failed");
            }
        });
    }
}