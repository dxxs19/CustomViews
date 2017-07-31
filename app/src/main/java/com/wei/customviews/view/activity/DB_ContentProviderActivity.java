package com.wei.customviews.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wei.customviews.R;
import com.wei.customviews.view.AppBaseActivity;
import com.wei.utillibrary.file.PrefUtils;
import com.wei.utillibrary.utils.LogUtil;

import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@EActivity(R.layout.activity_content_provider)
public class DB_ContentProviderActivity extends AppBaseActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MatrixCursor cursor = (MatrixCursor) query();
//        if (cursor.moveToFirst())
//        {
//            do{
//                String id = cursor.getString(cursor.getColumnIndex("id"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String age = cursor.getString(cursor.getColumnIndex("age"));
//                LogUtil.e(TAG, "--- info --- " + id + ", " + name + ", ");
//            }
//            while (cursor.moveToNext());
//        }
        copyDB("chat.db", R.raw.chat);
        openDB(Environment.getExternalStorageDirectory() + File.separator + "chat.db");
    }

    // 拷贝数据库
    private void copyDB(String dbName, int dbRes)
    {
        File file = new File(Environment.getExternalStorageDirectory(), dbName);
        if (file.exists())
        {
            Log.e(TAG, "数据库已复制到目标路径");
        }
        else
        {
            InputStream inputStream = getResources().openRawResource(dbRes);
            try {
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
                Log.e(TAG, "数据库已成功复制到目标路径");
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 打开数据库
    private void openDB(String path)
    {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select * from statistics where event_type = ?", new String[]{"1"});
        while (cursor.moveToNext())
        {
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String source_id = cursor.getString(cursor.getColumnIndex("source_id"));
            Log.e(TAG, "_id = " + _id + ", source_id = " + source_id);
        }
    }

}
