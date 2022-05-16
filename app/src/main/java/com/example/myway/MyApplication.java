package com.example.myway;


import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private static Context appContext;
    public static ExecutorService executorService = Executors.newFixedThreadPool(2);
    public static Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    public final static int MAP_KEY_SETTING_NUM = 5;//0-bench ,1-synagogue,2-stairs,3-garbage,4-cafeteria
    public static boolean[] mapKeySettings = initMapKeySettings();

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    private static boolean[] initMapKeySettings(){
        boolean[] tempKeySettings = new boolean[MAP_KEY_SETTING_NUM];
        for(int i=0;i<MAP_KEY_SETTING_NUM;i++){
            tempKeySettings[i]=true;
        }
        return tempKeySettings;
    }

    public static void setMapKeySettings(int i,boolean set) {
        mapKeySettings[i]=set;
    }

    public static Context getContext(){
        return appContext;
    };
}
