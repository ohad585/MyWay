package com.example.myway.Model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;


public class ModelFirebase {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void addUser(User user, Model.AddUserListener listener) {
        db.collection("users")
                .document(user.getUserName()).set(user.toJson())
                .addOnSuccessListener((successListener) -> {
                    listener.onComplete(true);
                })
                .addOnFailureListener((e) -> {
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

    public void regWithEmail(String email, String password, Model.RegistrationByMailPassListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onComplete();
                    Log.d("Tag", "success");
                } else {
                    Log.d("Tag", "not success", task.getException());

                }
            }
        });

    }

    public void signInWithEmail(String email, String password, Model.SignInWithEmailPassListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onComplete(new User(user), true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(null, true);
                        }
                    }
                });
    }


    public void saveRoom(Room r, Model.SaveRoomListener listener) {
        db.collection("Rooms")
                .add(r.toJson())
                .addOnSuccessListener((successListener) -> {
                    listener.onComplete();
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void getAllRooms(Model.GetAllRoomsListener listener) {
        db.collection("Rooms")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Room> roomList = new LinkedList<Room>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Room s = Room.fromJson(doc.getData());
                        if (s != null) {
                            roomList.add(s);
                        }
                    }
                } else {

                }
                listener.onComplete(roomList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });

    }

    public void GetRoomDetails(Polygon p, Model.GetRoomDetailsListener listener) {
        db.collection("Rooms").whereEqualTo("DETAILS", p.getTag().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            //if sucess return the pizza
                            Room r = Room.fromJson(document.getData());
                            listener.onComplete(r);
                        } else {
                            listener.onComplete(null);
                        }
                    }
                }
            }
        });
    }

    public void getAllBeacons(Model.GetAllBeaconsListener listener) {
        db.collection("Beacons")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<IBeacon> beaconList = new LinkedList<IBeacon>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        IBeacon b = IBeacon.fromJson(doc.getData());
                        if (b != null) {
                            beaconList.add(b);
                        }
                    }
                } else {

                }
                listener.onComplete(beaconList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });

    }

    public void saveIBeacon(IBeacon b, Model.SaveIBeaconListener listener) {
        db.collection("Beacons")
                .add(b.toJson())
                .addOnSuccessListener((successListener) -> {
                    listener.onComplete();
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });
    }
}