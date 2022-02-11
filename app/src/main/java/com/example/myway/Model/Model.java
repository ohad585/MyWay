package com.example.myway.Model;

import com.google.firebase.auth.FirebaseUser;

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
    public interface checkLogInListener{
          void onComplete(User u);
      }
//    public void checkUser(String name, String pass,checkLogInListener listener) {
//        modelFirebase.checkUser(name,pass,listener);
//
//    }

    public interface RegistrationByMailPassListener{
        void onComplete();
    }

    public void regModel(String email,String pass,RegistrationByMailPassListener listener){
        modelFirebase.regWithEmail(email, pass, listener);
    }
    public interface SignInWithEmailPassListener{
        void onComplete(User user, boolean success);
    }
    public void signInWithEmailPass(String email,String password,SignInWithEmailPassListener listener){
        modelFirebase.signInWithEmail(email,password,listener);
    }
}
