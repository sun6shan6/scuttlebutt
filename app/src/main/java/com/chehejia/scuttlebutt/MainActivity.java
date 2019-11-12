package com.chehejia.scuttlebutt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
    }

    void init() {
        Log.d(TAG, "init: ");
        final Model client = new Model();
        Model server = new Model();


        final Scuttlebutt.StreamOptions cOptions = new Scuttlebutt.StreamOptions();
        cOptions.setName("client");
        Scuttlebutt.StreamOptions sOptions = new Scuttlebutt.StreamOptions();
        sOptions.setName("server");
        Duplex cStream = client.createStream(cOptions);
        Duplex sStream = server.createStream(sOptions);

        client.set("name", "client-update");
        PullStream pullStream = new PullStream();
        pullStream.link(cStream, sStream);
    }

}
