package com.sgs.exceptioncontrol;

import java.util.Random;

public class Main {

    private static final long interval = 1000 * 60;
    public static void main(String... args) {
        final Random r = new Random();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int value = r.nextInt(10);
                    SignalMode mode = new SignalMode(value, System.currentTimeMillis());
                     if (Control.getInstance().watch(mode, interval)) {
                         upload(mode);
                     } else {
                         System.err.println(mode.getSignal() + " not in upload interval" );
                     }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    static void upload(SignalMode mode) {
//        System.out.println("normal send:" + mode.getSignal());
    }
}
