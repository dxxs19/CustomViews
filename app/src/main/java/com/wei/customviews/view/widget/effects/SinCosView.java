package com.wei.customviews.view.widget.effects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wei.customviews.R;

import static android.R.attr.path;

/**
 *
 */
public class SinCosView extends View
{
    private final String TAG = getClass().getSimpleName();
    private double x, y;
    private boolean draw = true;
    private Path mPath;
    private Paint mPaint;
    private int mWidth, mHeight;

    public SinCosView(Context context) {
        this(context, null);
    }

    public SinCosView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinCosView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        mPath = new Path();
//        mPath.moveTo(0, mHeight/2);
        changeAngle();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();
        Log.e(TAG, "mWidth = " + mWidth + ", mHeight = " + mHeight);

        if (x * 100 < mWidth)
        {
            y = Math.sin(x);
            mPath.lineTo((float) x * 100, mHeight/2 + (float) y * 100);
            canvas.drawPath(mPath, mPaint);
        }

//        y = Math.sin(x);
//        Log.e(TAG, "x , y = " + x + ", " + y);
//        mPath.lineTo((float) x * 100, mHeight/2 + (float) y * 100);

//        Matrix matrix = new Matrix();
//        matrix.setTranslate(100, mHeight/2);
//        mPath.transform(matrix);
//        canvas.drawPath(mPath, mPaint);
    }

    private void changeAngle()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (draw)
                {
                    x += Math.PI / 180;
                    postInvalidate();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
