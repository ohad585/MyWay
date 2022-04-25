package com.example.myway;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;

import java.util.List;

public class allFavoritePlacesViewModel  extends ViewModel {
        LiveData<List<Room>> data = Model.instance.getAllFavoritePlaces();
        public LiveData<List<Room>> getData() {
            return data;
        }
    }

