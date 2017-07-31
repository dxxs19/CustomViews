package com.wei.customviews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wei.utillibrary.utils.LogUtil;

/**
 * author: WEI
 * date: 2017/3/1
 */

public class BaseActivity extends AppCompatActivity
{
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG, "--- onCreate ---");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.e(TAG, "--- onRestoreInstanceState ---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e(TAG, "--- onRestart ---");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e(TAG, "--- onStart ---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG, "--- onResume ---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e(TAG, "--- onPause ---");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.e(TAG, "--- onSaveInstanceState ---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e(TAG, "--- onStop ---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG, "--- onDestroy ---");
    }
}
