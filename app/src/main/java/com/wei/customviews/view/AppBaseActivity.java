package com.wei.customviews.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.wei.utillibrary.net.RequestCallback;
import com.wei.utillibrary.utils.ToastUtil;

/**
 * Activity的基类
 * author: WEI
 * date: 2017/2/20
 */

public abstract class AppBaseActivity extends BaseActivity
{
    protected Context mContext;
    protected RequestCallback mRequestCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void showMsg(String msg)
    {
        ToastUtil.showMsg(this, msg);
    }

}
