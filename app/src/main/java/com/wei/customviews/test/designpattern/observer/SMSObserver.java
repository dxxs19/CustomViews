package com.wei.customviews.test.designpattern.observer;

import android.database.ContentObserver;
import android.os.Handler;

import com.wei.utillibrary.utils.LogUtil;

/**
 * author: WEI
 * date: 2017/3/10
 */

public class SMSObserver extends ContentObserver
{
    private final String TAG = getClass().getSimpleName();
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SMSObserver(Handler handler) {
        super(handler);
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        LogUtil.e(TAG, "selfChange : " + selfChange);
        mHandler.obtainMessage(0x11, "信息有变化！").sendToTarget();
    }
}
