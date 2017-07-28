package com.wei.utillibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * author: WEI
 * date: 2017/7/26
 */

public class ToastUtil
{
    public static void showMsg(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
