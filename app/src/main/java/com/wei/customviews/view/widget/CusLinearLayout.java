package com.wei.customviews.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wei.utillibrary.LogUtil;

/**
 * author: WEI
 * date: 2017/2/21
 */

public class CusLinearLayout extends LinearLayout
{
    private final String TAG = getClass().getSimpleName();

    public CusLinearLayout(Context context) {
        this(context, null);
    }

    public CusLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView()
    {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        int count = getChildCount();
//        for (int i = 0 ; i < count ; i ++)
//        {
//            View view = getChildAt(i);
//            view.layout(getPaddingLeft(), getPaddingTop() + i * view.getMeasuredHeight(),
//                    getPaddingLeft() + view.getMeasuredWidth() * (i+1), getPaddingTop() + view.getMeasuredHeight() * (i + 1));
//        }
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
        // 1.返回false或super.onTouchEvent(event)，自己不消耗事件，向上传播；
        // 2.返回true，自己消耗事件。activity的dispatchTouchEvent及所有上层的dispatchTouchEvent还有onInterceptTouchEvent及
        //   自己的dispatchTouchEvent还有onTouchEvent都会被调用；
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        // 1.返回false表示事件不再向下分发，onInterceptTouchEvent及onTouchEvent也不会被调用。事件向上传递；
        // 2.返回true，则onInterceptTouchEvent及onTouchEvent不会被调用，上层的onTouchEvent也不会被调用
        // 3.返回super.dispatchTouchEvent(ev)，则事件继续向下分发，事件由自己的onTouchEvent决定是否处理；
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
