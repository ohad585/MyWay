package com.example.myway;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;
import com.example.myway.Model.RoomGraph;

import java.util.List;

public class allRoomsHistoryViewModel extends ViewModel {
    LiveData<List<RoomGraph.RoomRepresent>> data = Model.instance.getAllHistoryPlacesForUser();
    public LiveData<List<RoomGraph.RoomRepresent>> getData() {
        return data;
    }
}
