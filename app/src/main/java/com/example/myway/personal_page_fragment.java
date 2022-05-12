package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;


public class personal_page_fragment extends Fragment {
    User u;

    View view;
    TextView phone_et;
    TextView email_et;
    TextView user_name_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                u=user;
                setup();
            }
        });
        view = inflater.inflate(R.layout.personal_page_fragment, container, false);

        Button editDetails = view.findViewById(R.id.profile_editDetails_btn);
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        return view;
    }

    private void setup() {
        phone_et = view.findViewById(R.id.profile_phone_info);
        email_et = view.findViewById(R.id.profile_mail_info);
        user_name_et = view.findViewById(R.id.profile_name_info);

        phone_et.setText(u.getPhoneNum());
        email_et.setText(u.getEmail());
        user_name_et.setText(u.getUserName());

    }

    private void edit() {
        personal_page_fragmentDirections.ActionPersonalPageFragmentToEditUserInfoFragment action =
                personal_page_fragmentDirections.actionPersonalPageFragmentToEditUserInfoFragment(u.getUserName(),
                        u.getPassword(),u.getEmail(),u.getPhoneNum());
        Navigation.findNavController(view).navigate(action);
    }
}