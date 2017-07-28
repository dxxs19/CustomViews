package com.wei.customviews.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import com.wei.customviews.R;
import com.wei.customviews.view.AppBaseActivity;
import com.wei.utillibrary.file.PrefUtils;
import com.wei.utillibrary.utils.LogUtil;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_content_provider)
public class ContentProviderActivity extends AppBaseActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        PrefUtils.putString(this, "id", "9527");
        PrefUtils.putString(this, "name", "laifu");
        PrefUtils.putString(this, "age", "28");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MatrixCursor cursor = (MatrixCursor) query();
        if (cursor.moveToFirst())
        {
            do{
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String age = cursor.getString(cursor.getColumnIndex("age"));
                LogUtil.e(TAG, "--- info --- " + id + ", " + name + ", ");
            }
            while (cursor.moveToNext());
        }
    }

    /**
     * ContentProvider操作XML文件的封装示例方法
     * @author liuyazhuang
     */
    public Cursor query() {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String[] columns = new String[] { "id", "name", "age" };
        MatrixCursor stringCursor = new MatrixCursor(columns);
        String row[] = new String[3];
        row[0] = sp.getString("id", "");
        row[1] = sp.getString("name", "");
        row[2] = sp.getString("age", "");
        stringCursor.addRow(row);
        return stringCursor;
    }
}
