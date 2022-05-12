package com.example.myway;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;


public class home_page_fragment extends Fragment {
    private User myUser;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.home_page_fragment, container, false);


        Button login_btn=view.findViewById(R.id.home_log_in_btn);
        Button reg_btn=view.findViewById(R.id.home_register_btn);
        login_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_page_fragment_to_log_in_fragment));

        reg_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_page_fragment_to_regrister_fragment));
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                myUser = user;
                checkUserConnected();
            }
        });
        return view;
    }

    private void checkUserConnected() {
        if(myUser!=null&&myUser.isBlind()){
            Intent i=new Intent(getContext(),MainActivity2.class);
            i.putExtra("myMapName", "Sami Shamoon College of engineering");
            getContext().startActivity(i);
        }else if (myUser!=null){
            Navigation.findNavController(view).navigate(R.id.action_home_page_fragment_to_mapSelectFragment);
        }
    }


}