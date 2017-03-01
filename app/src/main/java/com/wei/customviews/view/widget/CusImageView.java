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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.wei.utillibrary.utils.LogUtil;

/**
 * author: WEI
 * date: 2017/2/21
 */

public class CusImageView extends ImageView
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
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CusImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView()
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

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(this, "rotation", 0, 360);
        objectAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimator2.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                objectAnimator,
                objectAnimator1,
                objectAnimator2
        );
        animatorSet.setDuration(1500);
        // 加速减速插值器
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
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
        // 2.返回true，则onInterceptTouchEvent及onTouchEvent不会被调用，上层的onTouchEvent也不会被调用。事件无法被消费
        // 3.返回super.dispatchTouchEvent(ev)，则事件继续向下分发，事件由自己的onTouchEvent决定是否处理；
        return super.dispatchTouchEvent(ev);
    }

}
