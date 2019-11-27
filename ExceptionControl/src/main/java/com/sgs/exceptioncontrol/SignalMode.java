package com.sgs.exceptioncontrol;

public class SignalMode {

    int signal;
    long timestamp;

    public SignalMode(int signal, long timestamp) {
        this.signal = signal;
        this.timestamp = timestamp;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
