package com.wei.customviews.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wei.utillibrary.LogUtil;

/**
 * Activity的基类
 * author: WEI
 * date: 2017/2/20
 */

public class BaseActivity extends AppCompatActivity
{
    public String TAG = "";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG = getClass().getSimpleName();
        LogUtil.e(TAG, "当前class : " + TAG);
    }
}
