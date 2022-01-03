package com.example.myway.Model;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ModelFirebase {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

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

    public void checkUser(String name, String pass, Model.checkLogInListener listener) {
        DocumentReference docRef = db.collection("users").document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User u = User.fromJson(document.getData());
                        String passFromDB = u.getPassword();
                        if (pass == passFromDB) {
                            //login success
                            listener.onComplete(u);
                        }
                    } else {
                        //name doesnt match to password
                        listener.onComplete(null);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }
        public void reg(String email,String password,Model.RegistrationByMailPassListener listener){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        listener.onComplete();
                        Log.d("Tag","success");
                    } else {
                        Log.d("Tag","not success",task.getException());

                    }
                }
            });

        }
    }
