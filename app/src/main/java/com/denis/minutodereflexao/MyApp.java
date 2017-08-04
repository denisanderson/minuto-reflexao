package com.denis.minutodereflexao;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context mContext;

    public static Context getAppContext() {
        return MyApp.mContext;
    }

    public void onCreate() {
        super.onCreate();
        MyApp.mContext = getApplicationContext();
    }
}