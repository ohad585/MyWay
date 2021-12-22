package com.example.myway.Model;
import android.graphics.ColorSpace;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;


public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addUser(User user, Model.AddUserListener listener) {
        db.collection("users")
                .document(user.getUserName()).set(user.toJson())
                .addOnSuccessListener((successListener)-> {
                    listener.onComplete();
                })
                .addOnFailureListener((e)-> {
                    Log.d("TAG", e.getMessage());
                });
    }


}
