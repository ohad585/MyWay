package com.example.myway;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;
import com.example.myway.Model.User;

import java.util.ArrayList;

public class DialogRoomDetails extends DialogFragment {

    Room r;
    User currentUser;
    Dialog dialog;

    public DialogRoomDetails(Room r1 ,User cUser) {
        r = r1;
        currentUser=cUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_room_details, container, false);
        TextView roomName = view.findViewById(R.id.dialog_room_details_room_name_tv);
        TextView extraDit = view.findViewById(R.id.dialog_room_details_extra_det_tv);
        Button okBtn = view.findViewById(R.id.dialog_room_details_ok_btn);
        Button cnclBtn = view.findViewById(R.id.dialog_room_details_cncl_btn);
        Button favBtn = view.findViewById(R.id.dialog_room_details_fav_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: OK Clicked");
                dismiss();
            }
        });

        cnclBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: Cancel Clicked");
                dismiss();

            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "User :"+currentUser.getUserName() + " added " + r.getDetails() + " to favorites");
                Model.instance.addRoomToFavPlacesByUserName(currentUser.getEmail(), r, new Model.addRoomToFavPlacesByUserNameListener() {
                    @Override
                    public void onComplete(boolean success) {
                        if(success){
                            Toast.makeText(getActivity(),"Room added to favorites", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            Toast.makeText(getActivity(),"error try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        roomName.setText(r.getDetails());
        extraDit.setText(r.getExtraDetails());

        return view;
    }

//    @SuppressLint("ResourceType")
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        String addTOfav = "ADD TO FAVORITE";
//        String[] selectedItems = new String[]{addTOfav,"Zibi"};
//        boolean[] checkedItems = new boolean[]{false,false};
//        // Use the Builder class for convenient dialog construction
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View promptView = layoutInflater.inflate(R.layout.dialog_room_details, null);
//
//
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        return dialog;

    }
}

