package com.wei.utillibrary.utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created on 2014/5/27.
 *
 * @author <a href="754881660@qq.com">ganmingzhu</a>
 *         desc:
 */
public class ViewTreeUtils {

    /**
     * 便捷的ViewTreeObserver操作
     * @param view
     * @param listener
     */
    public static void onGlobalLayout(final View view, final ViewTreeObserver.OnGlobalLayoutListener listener){
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                listener.onGlobalLayout();
                ViewTreeObserver obs = view.getViewTreeObserver();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    obs.removeOnGlobalLayoutListener(this);
                else
                    obs.removeGlobalOnLayoutListener(this);
            }
        });
    }
}
