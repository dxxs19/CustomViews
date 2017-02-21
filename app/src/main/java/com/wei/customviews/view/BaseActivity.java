package com.wei.customviews.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.wei.customviews.R;
import com.wei.utillibrary.LogUtil;

/**
 * Activity的基类
 * author: WEI
 * date: 2017/2/20
 */

public class BaseActivity extends AppCompatActivity
{
    protected String TAG = "";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

}
