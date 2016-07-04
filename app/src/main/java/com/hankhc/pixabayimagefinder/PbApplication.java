package com.hankhc.pixabayimagefinder;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by hankchiu on 16/7/4.
 */
public class PbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
