package com.example.myway;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.fragment.app.DialogFragment;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;
import com.example.myway.Model.User;

import java.util.ArrayList;

public class DialogRoomDetails extends DialogFragment {

    Room r;
    User currentUser;

    public DialogRoomDetails(Room r1) {
        r = r1;
    }

    @SuppressLint("ResourceType")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                currentUser = user;
            }
        });
        ArrayList selectedItems = new ArrayList();
        String addTOfav = "ADD TO FAVORITE";
        selectedItems.add(addTOfav);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Room Details:")
                .setMessage(r.getDetails())
                .setMultiChoiceItems(R.xml.checkboxarray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            Model.instance.addRoomToFavPlacesByUserName(currentUser.getUserName(), r);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

