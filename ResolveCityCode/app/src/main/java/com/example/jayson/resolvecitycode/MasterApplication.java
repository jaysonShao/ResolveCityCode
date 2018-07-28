package com.example.jayson.resolvecitycode;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by jayson on 2018/7/28.
 */

public class MasterApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
