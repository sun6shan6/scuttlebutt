package com.chehejia.scuttlebutt;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class ScuttlebuttApplication extends Application {

    private static final String TAG = "ScuttlebuttApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
//        startService(new Intent(this, ScuttlebuttTest.class));
    }
}
