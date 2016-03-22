package com.aack.meinv.ui.activity;

import android.app.Application;

import com.lusfold.androidkeyvaluestore.KVStore;


/**
 * Created by root on 16-3-21.
 */
public class UIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KVStore.init(this, "Application");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        KVStore.destroy();
    }
}
