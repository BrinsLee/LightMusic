package com.brins.lightmusic.utils;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {

    public interface IHandler{
        void handleMsg(Message msg);
    }

    private final WeakReference<IHandler> mRef;

    public WeakHandler(IHandler handler) {
        mRef = new WeakReference<>(handler);
    }

    public WeakHandler(Looper looper, IHandler handler) {
        super(looper);
        mRef = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(Message msg) {
        IHandler handler = mRef.get();
        if (handler != null && msg != null){
            handler.handleMsg(msg);
        }
    }
}
