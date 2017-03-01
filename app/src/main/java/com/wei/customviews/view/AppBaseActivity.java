package com.wei.customviews.view;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.wei.utillibrary.activity.BaseActivity;
import com.wei.utillibrary.net.RequestCallback;

/**
 * Activity的基类
 * author: WEI
 * date: 2017/2/20
 */

public abstract class AppBaseActivity extends BaseActivity
{
    protected String TAG = "";
    protected Context mContext;
    protected RequestCallback mRequestCallback;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mContext = this;
    }

    public abstract class AbstractRequestCallback implements RequestCallback
    {
        @Override
        public abstract void onSuccess(String content);

        @Override
        public void onFail(String errorMsg) {
            new AlertDialog.Builder(mContext)
                    .setTitle("出错啦")
                    .setMessage(errorMsg)
                    .setPositiveButton("确定", null)
                    .show();
        }
    }

}
