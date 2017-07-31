package com.wei.customviews.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.wei.utillibrary.utils.LogUtil;

public class MyService extends Service
{
    private final String TAG = getClass().getSimpleName();
    private int count = 0;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "--- onCreate ---");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "--- onStartCommand ---");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 10)
                {
                    try {
                        Thread.sleep(1000);
                        count ++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "--- onBind ---");
        return new MyBinder();
    }

    public class MyBinder extends Binder
    {
        public int getCount()
        {
            return count;
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogUtil.e(TAG, "--- onRebind ---");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(TAG, "--- onUnbind ---");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG, "--- onDestroy ---");
    }
}
