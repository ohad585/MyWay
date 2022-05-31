package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;

public class log_in_fragment extends Fragment {
    View view;

    TextView userName;
    TextView userPass;
    String uName;
    String uPass;
    Button loginBtn;
    ProgressBar progBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.log_in_fragment, container, false);
        loginBtn=view.findViewById(R.id.log_in_log_in_btn);
        userName=view.findViewById(R.id.log_in_name_et);
        userPass=view.findViewById(R.id.log_in_password_et);
        progBar=view.findViewById(R.id.log_in_progBar);
        progBar.setVisibility(View.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uName=userName.getText().toString();
                uPass=userPass.getText().toString();
                loginUser();

            }
        });
        return view;
    }

    public void loginUser(){
        leave();
        Model.instance.signInWithEmailPass(uName,uPass,(User user, boolean success)->{
            if(success){
                Navigation.findNavController(view).navigate(R.id.action_log_in_fragment_to_mapSelectFragment);
            }
            else {
                back();
                Toast.makeText(getActivity(),"Login Failed try again",Toast.LENGTH_LONG).show();
                Log.d("TAG", "loginUser: Failed");
            }
        });
    }

    private void leave(){
        loginBtn.setClickable(false);
        progBar.setVisibility(View.VISIBLE);
        userName.setClickable(false);
        userPass.setClickable(false);
    }

    private void back(){
        loginBtn.setClickable(true);
        progBar.setVisibility(View.INVISIBLE);
        userPass.setClickable(true);
        userName.setClickable(true);
    }
}