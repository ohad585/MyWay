package com.example.myway;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;

public class EditUserInfoFragment extends Fragment {
    String email;
    String oldPass;

    View view;
    TextView phone_et;
    TextView pass_et;
    TextView user_name_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);
        email = EditUserInfoFragmentArgs.fromBundle(getArguments()).getUserEmail();
        String userName = EditUserInfoFragmentArgs.fromBundle(getArguments()).getUserName();
        String phone = EditUserInfoFragmentArgs.fromBundle(getArguments()).getPhone();
        oldPass = EditUserInfoFragmentArgs.fromBundle(getArguments()).getPassword();

        user_name_et= view.findViewById(R.id.edit_user_user_name_et);
        pass_et = view.findViewById(R.id.edit_user_pass_et);
        phone_et = view.findViewById(R.id.edit_user_phone_et);

        user_name_et.setText(userName);
        pass_et.setText(oldPass);
        phone_et.setText(phone);

        Button save_btn = view.findViewById(R.id.edit_user_save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        return view;
    }

    private void save() {
        String name = user_name_et.getText().toString();
        String phone = phone_et.getText().toString();
        String pass = pass_et.getText().toString();
        Model.instance.updateUserByEmail(email, name, phone, pass,oldPass ,new Model.UpdateUserByEmailListener() {
            @Override
            public void onComplete() {
                Log.d("TAG", "onComplete: ");
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

}