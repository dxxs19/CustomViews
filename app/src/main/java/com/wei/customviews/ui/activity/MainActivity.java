package com.wei.customviews.ui.activity;

import com.wei.customviews.R;
import com.wei.customviews.ui.BaseActivity;
import com.wei.utillibrary.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity
{
    @AfterViews
    void initView()
    {
        LogUtil.e(TAG, "初始化成功！");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
