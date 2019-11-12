package com.chehejia.scuttlebutt;

import java.util.List;

public interface Callback {
    void onReceive(List<Update> updates);
}
