package com.chehejia.scuttlebutt;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Duplex {

    private static final String TAG = "Duplex";

    List <Callback> list = new ArrayList<>();
    Scuttlebutt scuttlebutt;
    Scuttlebutt.StreamOptions options;

    Source source;
    Sink sink;

    Sink peerSink;

    Object buffer[];
    boolean isFirstRead;
    boolean syncSend;
    boolean syncReceive;


    public Duplex(Scuttlebutt scuttlebutt, Scuttlebutt.StreamOptions options) {
        this.scuttlebutt = scuttlebutt;
        this.options = options;
        init();
    }

    void init() {
        source = new Source("write", list, this);
        sink = new Sink(this);
        scuttlebutt.registerCallback(new Callback() {
            @Override
            public void onReceive(List<Update> updates) {
                source.update(updates);
            }
        });
    }


    List<Update> getHistory() {
        return scuttlebutt.getHistory();
    }

    void setHistory(List<Update> updates) {
        scuttlebutt.setHistory(updates);
    }

    class Error {
        String message;
        int code;
    }

    class Abort {
        Error error;
        boolean abort;
    }


    public static class Source {
        List <Callback> list;
        String name;
        Duplex duplex;
        List<Update> history;


        public Source(String name, List <Callback> list, Duplex duplex) {
            this.name = name;
            this.list = list;
            this.duplex = duplex;
            this.history = duplex.getHistory();
        }

        void registerListener (Callback callback, List<Update> updates) {
            Log.d(TAG, "registerListener: ");
            if (!list.contains(callback)) {
                Log.d(TAG, "registerListener() called with: callback = [" + callback + "], updates = [" + updates + "]");
                list.add(callback);
                for (Update update: history) {
                    if (!updates.contains(update)) {
                        update(history, callback);
                        break;
                    }
                }
            }
        }

        void update(List<Update> update) {
            if (list != null && !list.isEmpty()) {
                Iterator<Callback> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onReceive(update);
                    iterator.remove();
                }
            }
        }

        void update(List<Update> update , Callback callback) {
            callback.onReceive(update);
        }

    }

    public static class Sink {
        Abort abort;
        Duplex duplex;

        public Sink(Duplex duplex) {
            this.duplex = duplex;
        }

        void read(final Source sources) {
            Log.d(TAG, "read: ");
            sources.registerListener(new Callback() {
                @Override
                public void onReceive(List<Update> updates) {
                    for (Update update : updates) {
                        Log.d(TAG, "onReceive() called with: updates = [" + update.toString() + "]");
                    }
                    duplex.setHistory(updates);

                    if (abort == null ||(abort != null && !abort.abort)) {
                        read(sources);
                    }
                }
            }, duplex.getHistory());
        }
    }

}
