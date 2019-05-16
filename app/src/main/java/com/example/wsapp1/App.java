package com.example.wsapp1;

import android.app.Application;

public class App extends Application {

    public static ApiHelper apiHelper;
    public static DataManager dm;

    @Override
    public void onCreate() {
        super.onCreate();
        apiHelper = ApiHelper.Creator.newApi();
        dm = new DataManager();
    }
}
