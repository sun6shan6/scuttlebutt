package com.sgs.exceptioncontrol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiPredicate;


public class Control {

    LinkedBlockingDeque<SignalMode> mSignalList = new LinkedBlockingDeque<>();
    Map<Integer, SignalRecode>  mRecodes = new HashMap<>();
    Map<Integer, SignalMode>  mExceptionRecodes = new HashMap<>();
    private static Control mInstance;
    private Object mExceptionLock = new Object();
    ExecutorService executors = Executors.newSingleThreadExecutor();
    private static final int MAX_RECODE = 3;


    private Control() {
        looper();
    }

    public synchronized static Control getInstance() {
        if (mInstance == null) {
            mInstance = new Control();
        }
        return mInstance;
    }

    public boolean watch(SignalMode mode, long interval) {
        mSignalList.add(mode);
        return biPredicate.test(mode, interval);
    }

    BiPredicate<SignalMode, Long> biPredicate = new BiPredicate<SignalMode, Long>() {
        @Override
        public boolean test(SignalMode mode, Long interval) {
            if (mExceptionRecodes.containsKey(mode.getSignal())) {
                if (mode.getTimestamp() - mExceptionRecodes.get(mode.getSignal()).getTimestamp() > interval) {
                    return true;
                }
                return false;
            }
            return true;
        }
    };

    private void looper() {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        SignalMode mode = mSignalList.take();
                        if (!mRecodes.containsKey(mode.getSignal())) {
                            resetRecode(mode);
                        } else {
                            SignalRecode recode = mRecodes.get(mode.getSignal());
                            if (mode.getTimestamp() - recode.getMode().getTimestamp() > 1000 * 60) {
                                System.out.println("resetRecode mode:" + mode.getSignal());
                                resetRecode(mode);
                                synchronized (mExceptionLock) {
                                    if (mExceptionRecodes.containsKey(mode.getSignal())) {
                                        mExceptionRecodes.remove(mode.getSignal());
                                    }
                                }
                            } else  {
                                int count = recode.getCount();
                                System.out.println("signal:" + recode.getMode().getSignal() + " count:" + count);
                                if (recode.getCount() > (MAX_RECODE -1)) {
                                    System.err.println("exception signal:" + mode.getSignal());
                                    synchronized (mExceptionLock) {
                                        if (!mExceptionRecodes.containsKey(mode.getSignal())) {
                                            mExceptionRecodes.put(mode.getSignal(), mode);
                                        } else {
                                            mExceptionRecodes.replace(mode.getSignal(), mode);
                                        }
                                    }
                                    resetRecode(mode);
                                } else {
                                    recode.setCount(count + 1);
                                }
                            }
                            mRecodes.put(mode.getSignal(), recode);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void resetRecode(SignalMode mode) {
        SignalRecode recode = new SignalRecode(1, mode);
        if (mRecodes.containsKey(mode.getSignal())) {
            mRecodes.replace(mode.getSignal(), recode);
        } else {
            mRecodes.put(mode.getSignal(), recode);
        }
    }


    class SignalRecode {
        int count;
        SignalMode mode;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public SignalMode getMode() {
            return mode;
        }

        public void setMode(SignalMode mode) {
            this.mode = mode;
        }

        public SignalRecode(int count, SignalMode mode) {
            this.count = count;
            this.mode = mode;
        }
    }
}
