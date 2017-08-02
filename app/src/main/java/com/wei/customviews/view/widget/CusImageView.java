package com.wei.customviews.view.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;
import com.wei.utillibrary.utils.LogUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * author: WEI
 * date: 2017/2/21
 */

public class CusImageView extends android.support.v7.widget.AppCompatImageView
{
    private final String TAG = getClass().getSimpleName();

    public CusImageView(Context context) {
        this(context, null);
    }

    public CusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public CusImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context);
//    }

    void initView(Context context)
    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "--- onClick ---");
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "--- OnTouchListener ---");
                return false;
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.8f, 1.5f);
        objectAnimator.setEvaluator(new FloatEvaluator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "scaleY", 0.8f, 1.5f);
        objectAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator1.setRepeatCount(ValueAnimator.INFINITE);

//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(this, "rotation", 0, 360);
//        objectAnimator2.setRepeatMode(ValueAnimator.REVERSE);
//        objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimator2.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                objectAnimator,
                objectAnimator1//,
//                objectAnimator2
        );
        animatorSet.setDuration(1500);
        // 加速减速插值器
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        animatorSet.start();

        mScroller = new Scroller(context);
    }

    int mLastX, mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(TAG, "--- onTouchEvent(MotionEvent event) ---");
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

//        smoothScrollTo(200, 800);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "--- ACTION_DOWN ---");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG, "--- ACTION_MOVE ---");
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
//                int translationX = (int) (ViewHelper.getTranslationX(this) + deltaX);
//                int translationY = (int) (ViewHelper.getTranslationY(this) + deltaY);
//                ViewHelper.setTranslationX(this, translationX);
//                ViewHelper.setTranslationY(this, translationY);
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "--- ACTION_UP ---");
                break;

            case MotionEvent.ACTION_CANCEL:
                LogUtil.e(TAG, "--- ACTION_CANCEL ---");
                break;
        }

        mLastX = x;
        mLastY = y;
        // 1.返回false，自己不消耗事件，向上传播
        // 2.返回true或super.onTouchEvent(event)，自己消耗事件。activity的dispatchTouchEvent及所有上层的dispatchTouchEvent还有onInterceptTouchEvent及
        //   自己的dispatchTouchEvent还有onTouchEvent都会被调用。super.onTouchEvent(event)与ViewGroup的不一样
//        return super.onTouchEvent(event);
//        return true;
        return false;
    }

    private void smoothScrollTo(int destX, int destY)
    {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int deltaX = destX - scrollX;
        int deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, 0, deltaX, 0, 3000);
        invalidate();
    }

    Scroller mScroller = null;
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        // 1.返回false，则事件不再向下分发，onInterceptTouchEvent及onTouchEvent不会被调用。事件向上传递；
        // 2.返回true， 则事件不再向下分发，onInterceptTouchEvent及onTouchEvent不会被调用，上层的onTouchEvent也不会被调用,只有上层及本层dispatchTouchEvent(MotionEvent ev)被调用
        // 3.返回super.dispatchTouchEvent(ev)，则事件继续向下分发，事件由自己的onTouchEvent决定是否处理，一般返回这个；
        return super.dispatchTouchEvent(ev);
//        return true;
//        return false;
    }

}
