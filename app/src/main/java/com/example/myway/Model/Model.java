package com.example.myway.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.myway.MainActivity;

import java.util.List;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){}

    public interface AddUserListener{
        void onComplete(boolean flag);
    }

    public void addUser(User user, AddUserListener listener) {
        getUserByUserName(user.getUserName(), new GetUserByUserNameListener() {
            @Override
            public void onComplete(User u) {
                if (u==null){
                    modelFirebase.addUser(user, listener);
                }
                else{
                    listener.onComplete(false);
                }
            }
        });
    }

    public interface GetUserByUserNameListener{
        void onComplete(User u);
    }

    public void getUserByUserName(String userName,GetUserByUserNameListener listener) {
        modelFirebase.getUserByUserName(userName, listener);
    }
}
