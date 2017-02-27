package com.wei.customviews.view.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.wei.customviews.db.UserContentProvider;
import com.wei.customviews.db.UserDAO;
import com.wei.customviews.view.BaseActivity;
import com.wei.customviews.view.adapter.RecyclerAdapter;
import com.wei.customviews.view.fragment.SlidingConflictFragment;
import com.wei.utillibrary.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;
import static android.R.id.switch_widget;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SlidingConflictFragment.OnFragmentInteractionListener
{
    @ViewById(R.id.rc_view)
    RecyclerView mRecyclerView;
    @ViewById(R.id.imgView)
    ImageView mImageView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData = new ArrayList<>();
    private UserDAO mUserDAO;
    private ContentResolver mContentResolver;

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
        mContentResolver = getContentResolver();
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
                deleteData();
                break;

            case R.id.query_item:
                queryData();
                break;

            case R.id.update_item:
                updateData();
                break;
        }
        return true;
    }

    private void updateData()
    {
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/update");
        ContentValues values = new ContentValues();
        values.put("password", "ajdjfljadj");
        values.put("username", "大美女");
        int count = mContentResolver.update(uri, values, "_id = ?", new String[]{"3"});
        LogUtil.e(TAG, "更新了" + count);
    }

    private void deleteData()
    {
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/delete");
        int count = mContentResolver.delete(uri, "_id = ?", new String[]{"1"});
        LogUtil.e(TAG, "删除了" + count);
    }

    private void queryData()
    {
//        Cursor cursor = mUserDAO.queryAllUsers();
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/queryAll");
        Cursor cursor = mContentResolver.query(uri, new String[]{"_id", "password", "username"}, "_id > ?", new String[]{"0"}, "_id");

        if (cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("username"));
                String pass = cursor.getString(cursor.getColumnIndex("password"));
                LogUtil.e(TAG, "--- info --- " + id + ", " + name + ", " + pass);
            }
            while (cursor.moveToNext());
        }
    }

    private void addData()
    {
        ContentValues values = new ContentValues();
        values.put("username", "caixiangwei");
        values.put("password", "15458787");
//        mUserDAO.addUser(values);
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/insert");
        uri = mContentResolver.insert(uri, values);
        long id = ContentUris.parseId(uri);
        LogUtil.e(TAG, "插入的id:" + id);
    }

    private int count = 10;
    @Override
    protected void onResume() {
        super.onResume();

//        mHandler.obtainMessage();
//        mHandler.sendEmptyMessage(UPDATE);
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
        Thread.dumpStack();
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
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new MyTask().execute("wei");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class MyTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e(TAG, "已完成" + values);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "result : " + s);
        }
    }
}
