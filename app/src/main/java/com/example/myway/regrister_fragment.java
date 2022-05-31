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
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myway.Model.Model;
import com.example.myway.Model.User;

public class regrister_fragment extends Fragment {
    TextView name_et;
    TextView pass_et;
    TextView phone_et;
    TextView mail_et;
    CheckBox blind_cb;
    Button reg_btn;
    Button cncl_btn;
    ProgressBar progBar;
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
        blind_cb = view.findViewById(R.id.reg_is_blind_cb);
        progBar = view.findViewById(R.id.reg_progBar);
        progBar.setVisibility(View.INVISIBLE);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        cncl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave();
                Navigation.findNavController(view).navigateUp();

            }
        });



        return view;
    }



    public void save() {
        leave();
        String name=name_et.getText().toString();
        String phone=phone_et.getText().toString();
        String pass=pass_et.getText().toString();
        String mail=mail_et.getText().toString();
        boolean blind = blind_cb.isChecked();

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
            back();
        }
        else {
            User user = new User(name, pass, phone, mail,blind);

            Model.instance.addUser(user, (boolean sucsess) -> {
                Log.d("TAG", Boolean.toString(sucsess));
                if (sucsess){
                    Model.instance.regModel(mail, pass, (boolean success) ->  {
                        if(success) {
                            Navigation.findNavController(view).navigate(R.id.action_regrister_fragment_to_mapSelectFragment);
                        }else{
                            AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                            alertDialog.setTitle("ERROR");
                            alertDialog.setMessage("Something went wrong ");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            back();
                        }
                    });
                }
                else{
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
                    back();
                }
            });
        }
    }

    private void leave(){
        reg_btn.setClickable(false);
        cncl_btn.setClickable(false);
        name_et.setClickable(false);
        pass_et.setClickable(false);
        phone_et.setClickable(false);
        mail_et.setClickable(false);
        blind_cb.setClickable(false);
        progBar.setVisibility(View.VISIBLE);
    }

    private void back(){
        reg_btn.setClickable(true);
        cncl_btn.setClickable(true);
        name_et.setClickable(true);
        pass_et.setClickable(true);
        phone_et.setClickable(true);
        mail_et.setClickable(true);
        blind_cb.setClickable(true);
        progBar.setVisibility(View.INVISIBLE);
    }

}