package com.chehejia.scuttlebutt;

import android.util.Log;


public class Model extends Scuttlebutt {
    private static final String TAG = "Model";

    public Model() {

    }

    @Override
    boolean isAccepted(Object accept, Update update) {
        return false;
    }

    public void onReceive(Object action, Object value) {
        switch ((String)action) {
            case "sync":
                break;
            default:
                break;
        }
        Log.d(TAG, "connect() called with: action = [" + action + "]");
    }

}
