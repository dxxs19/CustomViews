package com.wei.customviews.view.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.wei.customviews.R;
import com.wei.customviews.db.UserDAO;
import com.wei.customviews.view.BaseActivity;
import com.wei.customviews.view.adapter.RecyclerAdapter;
import com.wei.utillibrary.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;
import static android.R.id.switch_widget;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity
{
    @ViewById(R.id.rc_view)
    RecyclerView mRecyclerView;
    @ViewById(R.id.imgView)
    ImageView mImageView;
    @ViewById(R.id.txtView)
    TextView mTextView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData = new ArrayList<>();
    private UserDAO mUserDAO;

    @AfterViews
    void initView()
    {
        TAG = getClass().getSimpleName();

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

        mUserDAO = UserDAO.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_item:
                addData();
                break;

            case R.id.remove_item:
//                deleteData();
                break;

            case R.id.query_item:
                queryData();
                break;

            case R.id.update_item:
//                updateData();
                break;
        }
        return true;
    }

    private void queryData()
    {
        Cursor cursor = mUserDAO.queryAllUsers();
        if (cursor.moveToFirst())
        {
            do{
                String name = cursor.getString(cursor.getColumnIndex("username"));
                String pass = cursor.getString(cursor.getColumnIndex("password"));
                LogUtil.e(TAG, "--- info --- " + name + ", " + pass);
            }
            while (cursor.moveToNext());
        }
    }

    private void addData()
    {
        ContentValues values = new ContentValues();
        values.put("username", "caixiangwei");
        values.put("password", "15458787");
        mUserDAO.addUser(values);
    }

    private int count = 10;
    @Override
    protected void onResume() {
        super.onResume();

        mHandler.obtainMessage();
        mHandler.sendEmptyMessage(UPDATE);
    }

    private final int UPDATE = 0x011;
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == UPDATE)
            {
                mImageView.setImageResource(R.mipmap.ic_launcher);
//                mTextView.setText(msg.what + "");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(TAG, "--- onTouchEvent(MotionEvent event) ---");
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "--- ACTION_DOWN ---");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG, "--- ACTION_MOVE ---");
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "--- ACTION_UP ---");
                break;

            case MotionEvent.ACTION_CANCEL:
                LogUtil.e(TAG, "--- ACTION_CANCEL ---");
                break;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        return super.dispatchTouchEvent(ev);
    }
}
