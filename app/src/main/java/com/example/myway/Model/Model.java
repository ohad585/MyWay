package com.example.myway.Model;

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
    MutableLiveData<List<RoomGraph.RoomRepresent>> favPlacesForUserLd = new MutableLiveData<>();
    MutableLiveData<LoadingState> favPlacesForUserLoadingState = new MutableLiveData<LoadingState>();
    MutableLiveData<List<RoomGraph.RoomRepresent>> historyPlacesForUserLd = new MutableLiveData<>();
    MutableLiveData<LoadingState> historyPlacesForUserLoadingState = new MutableLiveData<LoadingState>();


    public enum LoadingState {
        loading,
        loaded
    }

    private Model(){
        reloadRoomList();
        favPlacesForUserLoadingState.setValue(LoadingState.loaded);
        historyPlacesForUserLoadingState.setValue(LoadingState.loaded);
    }
    public LiveData<LoadingState> getHistoryListForUserLoadingState() {
        return historyPlacesForUserLoadingState;
    }

    public LiveData<LoadingState> getFavPlacesListForUserLoadingState() {
        return favPlacesForUserLoadingState;
    }

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




    public interface GetAllFavPlacesForUserListener {
        void onComplete(LinkedList<Room> data);
    }

    public LiveData<List<RoomGraph.RoomRepresent>> getAllFavPlacesForUser() {
        return favPlacesForUserLd;
    }


    public void reloadFavPlaceListByMail(String email) {
        favPlacesForUserLoadingState.setValue(LoadingState.loading);
        modelFirebase.getAllFavPlacesForUserByEmail(email,(list) -> {
            MyApplication.executorService.execute(() -> {
                List<RoomGraph.RoomRepresent> favRoomList = new LinkedList<>();
                for(RoomGraph.RoomRepresent r : list) {
                        favRoomList.add(r);
                }
                favPlacesForUserLd.postValue(favRoomList);
                favPlacesForUserLoadingState.postValue(LoadingState.loaded);
            });
        });
    }

    public interface addRoomToFavPlacesByUserNameListener {
        void onComplete();
    }


    public void addRoomToFavPlacesByUserName(String userName, Room room) {
        modelFirebase.addRoomToFavPlacesByUserName(userName,room,new addRoomToFavPlacesByUserNameListener(){
            @Override
            public void onComplete() {
                Log.d("TAG", "add room to fav success");
            }
        });
    }
    public interface removeRoomFromFavPlacesByUserNameListener {
        void onComplete();
    }

    public void removeRoomToFavPlacesByMail(String userName, String roomName) {
        modelFirebase.removeRoomFromFavPlacesByUserName(userName,roomName,new removeRoomFromFavPlacesByUserNameListener(){
            @Override
            public void onComplete() {
                Log.d("TAG", "remove room from fav success");
            }
        });
    }






    public interface GetAllHistoryPlacesForUserListener {
        void onComplete(LinkedList<RoomGraph.RoomRepresent> data);
    }

    public LiveData<List<RoomGraph.RoomRepresent>> getAllHistoryPlacesForUser() {
        return historyPlacesForUserLd;
    }


    public void reloadHistoryPlacesListByMail(String email) {
        historyPlacesForUserLoadingState.setValue(LoadingState.loading);
        modelFirebase.getAllHistoryPlacesForUserByEmail(email,(list) -> {
            MyApplication.executorService.execute(() -> {
                List<RoomGraph.RoomRepresent> HistoryPlacesListRooms = new LinkedList<>();
                for(RoomGraph.RoomRepresent r : list) {
                    HistoryPlacesListRooms.add(r);
                }




                historyPlacesForUserLd.postValue(HistoryPlacesListRooms);
                historyPlacesForUserLoadingState.postValue(LoadingState.loaded);
            });
        });
    }

    public interface addRoomToHistoryPlacesByUserNameListener {
        void onComplete();
    }


    public void addRoomToHistoryPlacesByUserMail(String userMail, RoomGraph.RoomRepresent room) {
        modelFirebase.addRoomToHistoryPlacesByUserMail(userMail,room,new addRoomToHistoryPlacesByUserNameListener(){
            @Override
            public void onComplete() {
                Log.d("TAG", "add room to HistoryPlaces success");
            }
        });
    }






























    public interface GetMapByNameListener{
        void onComplete(MyWayMap map);
    }
    public void getMapByName(String name , GetMapByNameListener listener) {
        modelFirebase.getMapByName(name,listener);
    }

    public interface GetAllMapsListener{
        void onComplete(List<MyWayMap> maps);
    }
    public void getAllMaps(GetAllMapsListener listener) {
        modelFirebase.getAllMaps(listener);
    }

    public interface getBluetoothDevicesListener{
        void onComplete(List<String> devices);
    }
    public void getBluetoothDevices(getBluetoothDevicesListener listener) {
        List<String> devices = new LinkedList<>();
        modelFirebase.getAllBeacons(new GetAllBeaconsListener() {
            @Override
            public void onComplete(List<IBeacon> beacons) {
                for(IBeacon beacon:beacons){
                    devices.add(beacon.getUid());
                }
                listener.onComplete(devices);
            }
        });
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

    public interface GetAllBeaconsListener {
        void onComplete(List<IBeacon> beacons);
    }
    public void getAllBeacons(GetAllBeaconsListener listener){
        modelFirebase.getAllBeacons(listener);
    }

    public interface SaveIBeaconListener{
        void onComplete();
    }
    public void saveIBeacon(IBeacon b , SaveIBeaconListener listener){
        modelFirebase.saveIBeacon(b,listener);
    }
    public interface getCurrentUserListener {
        void onComplete(User user);
    }

    public void getCurrentUser(getCurrentUserListener listener) {
        modelFirebase.getCurrentUser(listener);
    }
}
