package com.wei.customviews.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.wei.customviews.R;
import com.wei.utillibrary.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.gravity;
import static android.R.attr.width;
import static com.wei.customviews.R.attr.horizontal_Space;
import static com.wei.customviews.R.attr.isFull;

/**
 * author: WEI
 * date: 2017/2/22
 */

public class CusViewGroup extends ViewGroup
{
    private final String TAG = getClass().getSimpleName();
    private List<LineFeed> mLineFeeds = null;
    /*
         *对齐方式 right 0，left 1，center 2
        */
    private int gravity;
    /**
     * 水平间距,单位px
     */
    private float horizontal_Space;
    /**
     * 垂直间距,单位px
     */
    private float vertical_Space;
    /**
     * 是否自动填满
     */
    private boolean isFill;

    public CusViewGroup(Context context) {
        this(context, null);
    }

    public CusViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CusViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initType(context, attrs);
    }

    // 初始化样式，得到间隔，对齐方式等参数值
    private void initType(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusViewGroup);
        gravity = typedArray.getInt(R.styleable.CusViewGroup_gravity, 1);
//        String gravityStr = typedArray.getString(R.styleable.CusViewGroup_gravity);
//        if (gravityStr.equals("left"))
//        {
//            gravity = 1;
//        }
//        else if (gravityStr.equals("right"))
//        {
//            gravity = 0;
//        }
//        else
//        {
//            gravity = 2;
//        }
        horizontal_Space = typedArray.getDimension(R.styleable.CusViewGroup_horizontal_spacing, 0);
        vertical_Space = typedArray.getDimension(R.styleable.CusViewGroup_vertical_spacing, 0);
        isFill = typedArray.getBoolean(R.styleable.CusViewGroup_isFill, false);
        typedArray.recycle();
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
                    if (i != 0) {
                        width += horizontal_Space;
                    }
                    width += view.getMeasuredWidth();
                    LogUtil.e(TAG, "getChildAt(" + i + ").getmeasuredWidth() = " + getChildAt(i).getMeasuredWidth());
                }
                width += getPaddingLeft() + getPaddingRight();
                width = width > widthSize ? widthSize : width;
                break;

            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i ++)
                {
                    if (i != 0) {
                        width += horizontal_Space;
                    }
                    width += getChildAt(i).getMeasuredWidth();
                }
                width += getPaddingLeft() + getPaddingRight();
                break;

            default:
                width = widthSize;
                break;
        }

        /**
         * 不能够在定义属性时初始化，因为onMeasure方法会多次调用,导致
         * mLineFeeds尺寸比实际大
         */
        mLineFeeds = new ArrayList<>();
        LineFeed lineFeed = new LineFeed();
        for (int i = 0; i < childCount; i ++)
        {
            View view = getChildAt(i);
            if (lineFeed.lineWidth + view.getMeasuredWidth() + horizontal_Space> width)
            {
                if (lineFeed.mSameLineViews.size() == 0)
                {
                    // 还没有添加view
                    lineFeed.addView(view);
                    mLineFeeds.add(lineFeed);
                    lineFeed = new LineFeed();
                }
                else
                {
                    mLineFeeds.add(lineFeed);
                    lineFeed = new LineFeed();
                    lineFeed.addView(view);
                }
            }
            else
            {
                lineFeed.addView(view);
            }
        }

        /**
         * 添加最后一行
         */
        if (lineFeed.mSameLineViews.size() > 0 && !mLineFeeds.contains(lineFeed))
        {
            mLineFeeds.add(lineFeed);
        }

        height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mLineFeeds.size(); i ++)
        {
            if (i != 0)
            {
                height += vertical_Space;
            }
            height += mLineFeeds.get(i).height; // 行x高
        }

        switch (heightMode)
        {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;

            case MeasureSpec.AT_MOST:
                height = height > heightSize ? heightSize : height;
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
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        LogUtil.e(TAG, "--- onLayout ---, " + l + ", " + t + ", " + r + ", " + b);
        t = getPaddingTop(); // 此行代码不能省，需要知道子view在容器内的paddingTop，不然有可能会出现子view显示不全的情况。
        for (int i = 0; i < mLineFeeds.size(); i ++)
        {  // 行
            LineFeed lineFeed = mLineFeeds.get(i);
            int left = getPaddingLeft();
            int contentWidth = getMeasuredWidth() - lineFeed.lineWidth;
            LogUtil.e(TAG, "lineFeed.lineWidth = " + lineFeed.lineWidth + ", contentWidth = " + contentWidth);
            List<View> views = lineFeed.mSameLineViews;
            for (int j = 0; j < views.size(); j ++)
            {  // 该行有多少列(子view)
                View view = views.get(j);
                if (isFill) {//需要充满当前行时
                    view.layout(left, t, left + view.getMeasuredWidth() + contentWidth / lineFeed.mSameLineViews.size(), t + view.getMeasuredHeight());
                    left += view.getMeasuredWidth() + horizontal_Space + contentWidth / lineFeed.mSameLineViews.size();
                } else {
                    switch (gravity) {
                        case 0://右对齐
                            view.layout(left + contentWidth, t, left + contentWidth + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        case 2://居中对齐
                            view.layout(left + contentWidth / 2, t, left + contentWidth / 2 + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        default://左对齐
                            view.layout(left, t, left + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                    }
                    left += view.getMeasuredWidth() + horizontal_Space;
                }
            }
            t += lineFeed.height + vertical_Space;
        }
    }

    /**
     * 计算布局所有子view可以分成几行，
     * LineFeed即为一行的模板，mSameLineViews即为同一行中包含有几个子view
     */
    private final class LineFeed
    {
        private List<View> mSameLineViews = new ArrayList<>();
        // 该行所有view中高度最高值
        private int height;
        // 当前行中所需要占用的宽度
        private int lineWidth = getPaddingLeft() + getPaddingRight();

        private void addView(View view)
        {
            if (mSameLineViews.size() != 0)
            {
                lineWidth += horizontal_Space;
            }
            height = height > view.getMeasuredHeight() ? height : view.getMeasuredHeight();
            lineWidth += view.getMeasuredWidth();
            mSameLineViews.add(view);
        }
    }

}
