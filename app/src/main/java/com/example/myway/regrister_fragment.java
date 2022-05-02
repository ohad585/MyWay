package com.example.myway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
        cncl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });



        return view;
    }



    public void save() {
        String name=name_et.getText().toString();
        String phone=phone_et.getText().toString();
        String pass=pass_et.getText().toString();
        String mail=mail_et.getText().toString();

        if (name.matches("")|| phone.matches("") || pass.matches("")|| mail.matches("") ){
            AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("All fields must be filled");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            User user = new User(name, pass, phone, mail,true);

            Model.instance.addUser(user, (boolean flag) -> {
                Log.d("TAG", Boolean.toString(flag));
                if (flag==true){
                    Model.instance.regModel(mail, pass, () ->  Navigation.findNavController(view).navigate(R.id.action_regrister_fragment_to_mapSelectFragment));
                }
                if(flag==false){
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("User name already exist");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });
        }
    }


}