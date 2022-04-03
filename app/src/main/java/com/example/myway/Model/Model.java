package com.example.myway.Model;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myway.MyApplication;
import com.google.android.gms.maps.model.Polygon;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    MutableLiveData<List<Room>> roomsListLd = new MutableLiveData<List<Room>>();

    private Model(){reloadRoomList();}

    void reloadRoomList(){
        modelFirebase.getAllRooms((list)->{
            MyApplication.executorService.execute(()->{
                //3. update local last update date
                //4. add new records to the local db
                Log.d("TAG", "FB returned " + list.size());
                List<Room> rList=new LinkedList<>();
                for(Room r : list){
                    rList.add(r);
                }
                //5. return all records to the caller
                roomsListLd.postValue(rList);
            });
        });
    }
    public LiveData<List<Room>> getAll(){
        return roomsListLd;
    }

    public List<String> getBluetoothDevices() {
        List<String> devices = new LinkedList<>();

        return devices;
    }


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
    public interface SaveRoomListener {
        void onComplete();
    }
    public void saveRoom(Room r, SaveRoomListener listener){
        modelFirebase.saveRoom(r,listener);
    }

    public interface GetAllRoomsListener{
        void onComplete(List<Room> roomList);
    }
    public void getAllRooms(GetAllRoomsListener listener){
        modelFirebase.getAllRooms(listener);
    }

    public interface GetRoomDetailsListener{
        void onComplete(Room room);
    }
    public void GetRoomDetails(Polygon p,GetRoomDetailsListener listener){
        modelFirebase.GetRoomDetails(p,listener);
    }

}
