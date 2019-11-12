package com.chehejia.scuttlebutt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.chehejia.scuttlebutt.Duplex.Sink;

public class ScuttlebuttTest extends Service {

    private static final String TAG = "ScuttlebuttTest";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


}
