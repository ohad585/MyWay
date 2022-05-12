package com.example.myway.Model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ModelFirebase {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference mDatabase;



    public void addUser(User user, Model.AddUserListener listener) {
        db.collection("users")
                .document(user.getEmail()).set(user.toJson())
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
                    signInWithEmail(email, password, new Model.SignInWithEmailPassListener() {
                        @Override
                        public void onComplete(User user, boolean success) {
                            listener.onComplete();
                        }
                    });
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
                            listener.onComplete(null, false);
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
    public void getCurrentUser(Model.getCurrentUserListener listener1) {
        FirebaseUser user = mAuth.getCurrentUser();
        User usr=new User();
        usr.setEmail(user.getEmail());
        usr.setUid(user.getUid());
        usr.setPhoneNum(user.getPhoneNumber());
        usr.setUserName(user.getDisplayName());
        getUserByUserName(usr.getEmail(), new Model.GetUserByUserNameListener() {
            @Override
            public void onComplete(User u) {
                listener1.onComplete(u);
            }
        });
    }

    public void getAllMaps(Model.GetAllMapsListener listener) {
        db.collection("Maps")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<MyWayMap> maps = new LinkedList<MyWayMap>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        MyWayMap m = MyWayMap.fromJson(doc.getData());
                        if (m != null) {
                            maps.add(m);
                        }
                    }
                } else {

                }
                listener.onComplete(maps);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });
    }

    public void getMapByName(String name, Model.GetMapByNameListener listener) {
        db.collection("Maps")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        MyWayMap m = MyWayMap.fromJson(doc.getData());
                        if (m != null) {
                            if(m.getName().matches(name)){
                                 listener.onComplete(m);
                                 return;
                            }
                        }
                    }
                    listener.onComplete(null);
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });
    }
    public void getAllFavPlacesForUserByEmail(String mail,
                                                  Model.GetAllFavPlacesForUserListener listener) {
        DocumentReference docRef = db.collection("users").document(mail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                LinkedList<RoomGraph.RoomRepresent> favRoomsListRooms=new LinkedList<>();
                DocumentSnapshot document = task.getResult();
                List<String> favRoomsListString = (List<String>) document.get("Favorite Rooms");
                RoomGraph.RoomRepresent r;
                for (int i = 0; i < favRoomsListString.size(); i++) {
                    r = NavAlg.g.getRoomByName(favRoomsListString.get(i));
                    favRoomsListRooms.add(r);
                }
                listener.onComplete(favRoomsListRooms);
            }
        });
    }
    public void addRoomToFavPlacesByUserName(String userName, Room room,Model.addRoomToFavPlacesByUserNameListener listener) {
        String roomName=room.getDetails();
        db.collection("users").document(userName).update("favoritePlaces",FieldValue.arrayUnion(roomName))
                .addOnSuccessListener((successListener) -> {
                    Log.d("TAG", "add room to fav success");
                    listener.onComplete(true);
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                    listener.onComplete(false);
                });

    }

    public void removeRoomFromFavPlacesByUserName(String userName, String roomName, Model.removeRoomFromFavPlacesByUserNameListener removeRoomFromFavPlacesByUserNameListener) {
        //String roomName=room.getDetails();
        db.collection("users").document(userName).update("Favorite Rooms", FieldValue.arrayRemove(roomName))
                .addOnSuccessListener((successListener) -> {
                    Log.d("TAG", "remove room from fav success");
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });

    }


    public void getAllHistoryPlacesForUserByEmail(String mail,
                                                  Model.GetAllHistoryPlacesForUserListener listener) {
        DocumentReference docRef = db.collection("users").document(mail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                LinkedList<RoomGraph.RoomRepresent> HistoryPlacesListRooms=new LinkedList<>();
                DocumentSnapshot document = task.getResult();
                List<String> HistoryPlacesListString = (List<String>) document.get("HistoryPlaces");
                RoomGraph.RoomRepresent r;
                for (int i = 0; i < HistoryPlacesListString.size(); i++) {
                        r = NavAlg.g.getRoomByName(HistoryPlacesListString.get(i));
                       HistoryPlacesListRooms.add(r);
                }
                listener.onComplete(HistoryPlacesListRooms);
            }
        });
    }



    public void addRoomToHistoryPlacesByUserMail(String userMail, RoomGraph.RoomRepresent room, Model.addRoomToHistoryPlacesByUserNameListener listener) {
        String roomName=room.getRoom();
        db.collection("users").document(userMail).update("HistoryPlaces", FieldValue.arrayUnion(roomName))
                .addOnSuccessListener((successListener) -> {
                    Log.d("TAG", "add room to HistoryPlaces success");
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });

    }

    public void updateUserByEmail(String email, String name, String phone, String pass,String oldPass, Model.UpdateUserByEmailListener listener) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPass);
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    mAuth.getCurrentUser().updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Password updated");
                                Map<String,Object> data = new HashMap<>();
                                data.put("userName",name);
                                data.put("password",pass);
                                data.put("phone",phone);
                                db.collection("users").document(email).update(data)
                                        .addOnSuccessListener((successListener) -> {
                                            Log.d("TAG", "add room to HistoryPlaces success");
                                            listener.onComplete();
                                        })
                                        .addOnFailureListener((e) -> {
                                            Log.d("TAG", e.getMessage());
                                        });

                            } else {
                                Log.d("TAG", "Error password not updated");
                            }
                        }
                    });


                }
            }
        });

    }
}