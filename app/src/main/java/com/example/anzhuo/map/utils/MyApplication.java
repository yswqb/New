package com.example.anzhuo.map.utils;

import android.app.Application;

/**
 * 夜间模式设置
 */
public class MyApplication extends Application{

    public static AppConfig appConfig;


    @Override
    public void onCreate() {
        super.onCreate();
        appConfig = new AppConfig(getApplicationContext());

    }


}
