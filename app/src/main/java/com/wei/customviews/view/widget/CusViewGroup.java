package com.wei.customviews.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wei.utillibrary.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author: WEI
 * date: 2017/2/22
 */

public class CusViewGroup extends ViewGroup
{
    private final String TAG = getClass().getSimpleName();
    private List<View> mViews = new ArrayList<>();

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
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0, height = 0;
        int childCount = getChildCount();

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        switch (widthMode)
        {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;

            case MeasureSpec.AT_MOST:
                for (int i = 0; i < childCount; i ++)
                {
                    View view = getChildAt(i);
                    mViews.add(view);
                    width += view.getMeasuredWidth();
                    LogUtil.e(TAG, "getChildAt(" + i + ").getmeasuredWidth() = " + getChildAt(i).getMeasuredWidth());
                }
                width += getPaddingLeft() + getPaddingRight();
                width = width > widthSize ? widthSize : width;

                if (width > widthSize)
                {
                    // 计算行数
                    int row = width%widthSize == 0 ? width/widthSize : (width/widthSize + 1);
                    for (int i = 0; i < row; i ++)
                    {
                        height += measureHeight();
                    }
                }
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

        switch (heightMode)
        {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;

            case MeasureSpec.AT_MOST:
                mViews.clear();
                for (int i = 0; i < childCount; i ++)
                {
                    View view = getChildAt(i);
                    mViews.add(view);
                    width += view.getMeasuredWidth();
                    LogUtil.e(TAG, "getChildAt(" + i + ").getmeasuredWidth() = " + getChildAt(i).getMeasuredWidth());
                }
                width += getPaddingLeft() + getPaddingRight();
                if (width > widthSize)
                {
                    // 计算行数
                    int row = width%widthSize == 0 ? width/widthSize : (width/widthSize + 1);
                    for (int i = 0; i < row; i ++)
                    {
                        height += measureHeight();
                    }
                }
                width = width > widthSize ? widthSize : width;
                break;

            case MeasureSpec.UNSPECIFIED:
                break;

            default:
                break;
        }

        LogUtil.e(TAG, "widthMode : " + widthMode + ", widthSize : " + widthSize + ", heightMode : " +
                heightMode + ", heightSize : " + heightSize + ", childCount : " + childCount + ", width : " + width + ", height : " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.e(TAG, "--- onLayout ---, " + l + ", " + t + ", " + r + ", " + b);
        int size = getChildCount();
        int width = 0, height = 0, index = 0;
        count = 0;
        for (int i = 0; i < size; i ++)
        {
            View view = getChildAt(i);
            width += view.getMeasuredWidth();
            if ((width += (getPaddingLeft() + getPaddingRight())) > widthSize)
            {
                height += getMaxHeight(mViews.subList(index, (i==index) ? (i+1) : i));
                setLayout(mViews.subList(index, (i==index) ? (i+1) : i), height);
                index = i;
                width = view.getMeasuredWidth();
            }
        }
    }

    int count = 0;
    private void setLayout(List<View> views, int height)
    {
        for (int i = 0; i < views.size(); i ++ )
        {
            View view = views.get(i);
            view.layout(getPaddingLeft() + view.getMeasuredWidth() * i, view.getMeasuredHeight() * count + getPaddingTop(),
                    view.getMeasuredWidth() * (i + 1) + getPaddingLeft(), view.getMeasuredHeight() * (count + 1) + getPaddingTop());
        }
        count ++;
    }

    int index = 0, width = 0, widthSize = 0;
    private int measureHeight()
    {
        int count = mViews.size();
        int height = 0;
        for (int i = index; i < count; i++)
        {
            View view = mViews.get(i);
            width += view.getMeasuredWidth();
            if ((width += (getPaddingLeft() + getPaddingRight())) > widthSize) {
                height = getMaxHeight(mViews.subList(index, (i==index) ? (i+1) : i));
                index = i;
                width = view.getMeasuredWidth();
                break;
            }
        }
        return height;
    }

    private int getMaxHeight(List<View> views)
    {
        List<Integer> heights = new ArrayList<>();
        for (View view : views)
        {
            int height = view.getMeasuredHeight();
            heights.add(height);
        }

        int maxHeight = heights.get(0);
        for (int i = 0 ; i < heights.size() - 1 ; i ++)
        {
            int height = heights.get(i + 1);
            if (maxHeight < height)
            {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

}
