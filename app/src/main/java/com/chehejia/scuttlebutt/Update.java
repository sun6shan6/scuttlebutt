package com.chehejia.scuttlebutt;

public class Update {
    long timestamp;
    Object value;
    Object key;


    public Update(long timestamp, Object value, Object key) {
        this.timestamp = timestamp;
        this.value = value;
        this.key = key;
    }

    @Override
    public String toString() {
        return "Update{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                ", key=" + key +
                '}';
    }
}
