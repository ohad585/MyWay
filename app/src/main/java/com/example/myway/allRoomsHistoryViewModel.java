package com.example.myway;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;

import java.util.List;

public class allRoomsHistoryViewModel extends ViewModel {
    LiveData<List<Room>> data = Model.instance.getAllRoomHistory();
    public LiveData<List<Room>> getData() {
        return data;
    }
}
