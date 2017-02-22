package com.wei.customviews.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wei.utillibrary.LogUtil;

/**
 * author: WEI
 * date: 2017/2/22
 */

public class CusViewGroup extends ViewGroup
{
    private final String TAG = getClass().getSimpleName();

    public CusViewGroup(Context context) {
        super(context);
    }

    public CusViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CusViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        LogUtil.e(TAG, "--- onMeasure ---");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0, height = 0;
        int childCount = getChildCount();
        LogUtil.e(TAG, "widthMode : " + widthMode + ", widthSize : " + widthSize + ", heightMode : " +
            heightMode + ", heightSize : " + heightSize + ", childCount : " + childCount);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        switch (widthMode)
        {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;

            case MeasureSpec.AT_MOST:
                for (int i = 0; i < childCount; i ++)
                {
                    width += getChildAt(i).getMeasuredWidth();
                }
                width += getPaddingLeft() + getPaddingRight();
                width = width > widthSize ? widthSize : width;
                break;

            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i ++)
                {
                    width += getChildAt(i).getMeasuredWidth();
                }
                width += getPaddingLeft() + getPaddingRight();
                break;

            default:
                width = widthSize;
                break;
        }



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.e(TAG, "--- onLayout ---");
    }
}
