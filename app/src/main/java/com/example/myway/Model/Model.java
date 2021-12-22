package com.example.myway.Model;

import java.util.List;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){}

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(User user, AddUserListener listener) {
        modelFirebase.addUser(user, listener);
    }
}
