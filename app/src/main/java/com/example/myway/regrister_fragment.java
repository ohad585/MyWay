package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;

public class regrister_fragment extends Fragment {
    TextView name_et;
    TextView pass_et;
    TextView phone_et;
    TextView mail_et;
    Button reg_btn;
    Button cncl_btn;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.registraition_fragment, container, false);
        name_et=view.findViewById(R.id.reg_username_et);
        pass_et=view.findViewById(R.id.reg_pass_et);
        phone_et=view.findViewById(R.id.reg_phone_et);
        mail_et=view.findViewById(R.id.reg_email_et);
        reg_btn=view.findViewById(R.id.reg_reg_btn);
        cncl_btn=view.findViewById(R.id.reg_cncl_btn);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        return view;
    }



    public void save() {
        User user = new User(name_et.getText().toString(), pass_et.getText().toString(), phone_et.getText().toString(), mail_et.getText().toString());
        Model.instance.addUser(user, () -> {
            Navigation.findNavController(view).navigateUp();
        });
    }
}