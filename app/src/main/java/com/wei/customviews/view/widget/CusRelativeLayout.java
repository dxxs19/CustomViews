package com.wei.customviews.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.wei.utillibrary.utils.LogUtil;

/**
 * author: WEI
 * date: 2017/2/21
 */

public class CusRelativeLayout extends RelativeLayout
{
    private final String TAG = getClass().getSimpleName();

    public CusRelativeLayout(Context context) {
        super(context);
    }

    public CusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(TAG, "--- onTouchEvent(MotionEvent event) ---");
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "--- ACTION_DOWN ---");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG, "--- ACTION_MOVE ---");
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "--- ACTION_UP ---");
                break;

            case MotionEvent.ACTION_CANCEL:
                LogUtil.e(TAG, "--- ACTION_CANCEL ---");
                break;
        }
        // 1.返回false或super.onTouchEvent(event)，则表示不响应事件，那么该事件将会不断向上层View的onTouchEvent方法传递，
        //   直到某个View的onTouchEvent方法返回true，如果到了最顶层View还是返回false，那么认为该事件不消耗，则在同一个事件系列中，
        //   当前View无法再次接收到事件，该事件会交由Activity的onTouchEvent进行处理；
        // 2.返回true，自己消耗事件。activity的dispatchTouchEvent及所有上层的dispatchTouchEvent还有onInterceptTouchEvent及
        //   自己的dispatchTouchEvent还有onTouchEvent都会被调用；
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        // 1.返回false表示事件不再向下分发，onInterceptTouchEvent及onTouchEvent也不会被调用。
        //   事件向上传递（如果本层控件已经是Activity，那么事件将被系统消费或处理）；
        // 2.返回true，则onInterceptTouchEvent及onTouchEvent不会被调用，上层的onTouchEvent也不会被调用。事件无法被消费
        // 3.返回super.dispatchTouchEvent(ev)，事件将分发给本层的事件拦截onInterceptTouchEvent 方法进行处理，
        //   事件由自己的onTouchEvent决定是否处理；
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- onInterceptTouchEvent(MotionEvent ev) ---");
        // 1.返回true表示拦截事件由自己的onTouchEvent处理。能不能处理由onTouchEvent的返回值决定；
        // 2.返回false或super.onInterceptTouchEvent(ev)表示不拦截事件，继续向下传播
        return super.onInterceptTouchEvent(ev);
    }
}
