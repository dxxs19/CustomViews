package com.wei.customviews.view.widget.slidingConflic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * author: WEI
 * date: 2017/2/27
 */

public class HorizontalEx extends ViewGroup
{
    private static final String TAG = "HorizontalEx";

    private boolean isFirstTouch = true;
    private int childIndex;
    private int childCount;
    private int lastXIntercept, lastYIntercept, lastX, lastY;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;


    public HorizontalEx(Context context) {
        this(context, null);
    }

    public HorizontalEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (childCount == 0)
        {
            width = height = 0;
        }
        else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST )
        {
            width = childCount * getChildAt(0).getMeasuredWidth();
            height = getChildAt(0).getMeasuredHeight();
        }
        else if (widthMode == MeasureSpec.AT_MOST)
        {
            width = childCount * getChildAt(0).getMeasuredWidth();
        }
        else
        {
            height = getChildAt(0).getMeasuredHeight();
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        for (int i = 0; i < getChildCount(); i ++)
        {
            View child = getChildAt(i);
            child.layout(left + l, t, r + left, b);
            left += child.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastXIntercept = x;
                lastYIntercept = y;
                intercept = false;
                if (!mScroller.isFinished())
                {
                    mScroller.abortAnimation();
                    intercept = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastXIntercept;
                int deltaY = y - lastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY))
                {
                    intercept = true;
                }
                else
                {
                    intercept = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastXIntercept = x;
        lastYIntercept = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        mVelocityTracker.addMovement(event);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                {
                    mScroller.abortAnimation();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isFirstTouch)
                {
                    lastX = x;
                    lastY = y;
                    isFirstTouch = false;
                }
                int deltaX = x - lastX;
                scrollBy(-deltaX, 0);
                break;

            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int childWidth = getChildAt(0).getMeasuredWidth();
                mVelocityTracker.computeCurrentVelocity(1000, viewConfiguration.getScaledMaximumFlingVelocity());
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity())
                {
                    childIndex = xVelocity < 0 ? childIndex + 1 : childIndex - 1;
                }
                else
                {
                    childIndex = (scrollX + childWidth/2) / childWidth;
                }
                childIndex = Math.min(getChildCount() - 1, Math.max(childIndex, 0));
                smoothScrollBy(childIndex * childWidth - scrollX, 0);
                mVelocityTracker.clear();
                isFirstTouch = true;
                break;
        }
        return true;
    }

    void smoothScrollBy(int dx, int dy)
    {
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }

}
