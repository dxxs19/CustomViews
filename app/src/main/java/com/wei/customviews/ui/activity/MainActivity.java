package com.wei.customviews.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wei.customviews.R;
import com.wei.customviews.ui.BaseActivity;
import com.wei.customviews.ui.adapter.RecyclerAdapter;
import com.wei.utillibrary.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity
{
    @ViewById(R.id.rc_view)
    RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData = new ArrayList<>();

    @AfterViews
    void initView()
    {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i < 10; i ++)
        {
            mData.add("Recycler" + i);
        }

        mRecyclerAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
