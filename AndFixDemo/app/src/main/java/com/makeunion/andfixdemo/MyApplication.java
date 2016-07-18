package com.makeunion.andfixdemo;

import android.app.Application;

/**
 * Created by renjialiang on 2016/5/11.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VersionManager.getInstance(getApplicationContext()).initLoad();
    }
}
