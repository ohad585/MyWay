package com.example.myway.Model;
import android.graphics.ColorSpace;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addUser(User user, Model.AddUserListener listener) {
        db.collection("users")
                .document(user.getUserName()).set(user.toJson())
                .addOnSuccessListener((successListener)-> {
                    listener.onComplete(true);
                })
                .addOnFailureListener((e)-> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void getUserByUserName(String userName, Model.GetUserByUserNameListener listener) {
        DocumentReference docRef = db.collection("users").document(userName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User u = User.fromJson(document.getData());
                        listener.onComplete(u);
                    } else {
                        listener.onComplete(null);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }

}
