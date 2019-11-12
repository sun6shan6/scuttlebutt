package com.chehejia.scuttlebutt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Scuttlebutt {


    public Scuttlebutt() {

    }

    long timestamp = 0;
    long history = 0;
    List<Update> mHistory = new ArrayList<>();
    Callback callback;

    void set(Object key, Object value) {
        timestamp = System.currentTimeMillis();
        Update newData = new Update(timestamp, value, key);
        applyUpdate(newData);
    }

    Duplex createStream(StreamOptions options) {
        return new Duplex(this, options);
    }

    public List<Update> getHistory() {
        return mHistory;
    }

    public void setHistory(List<Update> updates) {
        mHistory = updates;
    }

    void applyUpdate(Update update) {
        Iterator iterator = mHistory.iterator();
        while (iterator.hasNext()) {
            Update it = (Update) iterator.next();
            if (it.key.equals(update.key)) {
                iterator.remove();
                break;
            }
        }
        mHistory.add(update);
        if (callback != null) {
            callback.onReceive(mHistory);
        }
    }

    void registerCallback(Callback callback) {
        this.callback = callback;
    }

    abstract boolean isAccepted(Object accept, Update update);

    public static class StreamOptions {

        boolean readable = false;
        boolean writable = false;
        boolean tail = false;
        String name = "";
        long clock = 0;
        boolean sendClock = false;

        public boolean isReadable() {
            return readable;
        }

        public void setReadable(boolean readable) {
            this.readable = readable;
        }

        public boolean isWritable() {
            return writable;
        }

        public void setWritable(boolean writable) {
            this.writable = writable;
        }

        public boolean isTail() {
            return tail;
        }

        public void setTail(boolean tail) {
            this.tail = tail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getClock() {
            return clock;
        }

        public void setClock(long clock) {
            this.clock = clock;
        }

        public boolean isSendClock() {
            return sendClock;
        }

        public void setSendClock(boolean sendClock) {
            this.sendClock = sendClock;
        }

    }

}
