package com.wei.customviews.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.wei.customviews.R;
import com.wei.customviews.db.MyContentProvider;
import com.wei.customviews.view.AppBaseActivity;
import com.wei.utillibrary.file.PrefUtils;
import com.wei.utillibrary.utils.LogUtil;
import com.wei.utillibrary.utils.MD5Utils;
import com.wei.utillibrary.utils.OsUtil;

import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.wei.utillibrary.utils.OsUtil.getMacAddress;

@EActivity(R.layout.activity_content_provider)
public class DB_ContentProviderActivity extends AppBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        copyDB("chat.db", R.raw.chat);
//        openDB(Environment.getExternalStorageDirectory() + File.separator + "chat.db");
//        int a = 2 << 3; // 2的3次方
//        LogUtil.e(TAG, "a = " + a);

//        testContentProvider();

        generateUniqueCode();

    }

    private void generateUniqueCode()
    {
        String uniqueCode = Build.SERIAL;
        if (TextUtils.isEmpty(uniqueCode))
        {
            uniqueCode = OsUtil.getMacAddress(mContext);
            if (TextUtils.isEmpty(uniqueCode))
            {
                if (Build.VERSION.SDK_INT < 23) {
                    uniqueCode = OsUtil.getIMEI(mContext);
                } else {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        uniqueCode = OsUtil.getIMEI(mContext);
                    }
                }
            }
        }
        LogUtil.e(TAG, "uniqueCode : " + uniqueCode );
        LogUtil.e(TAG, "md5加密后的uniqueCode : " + MD5Utils.MD5Encode(uniqueCode));
    }

    private void testContentProvider() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(MyContentProvider.URI + "/statistics/query"),
                new String[]{"source_id", "_id", "event_type"}, "event_type = ?", new String[]{"1"}, "_id");
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String source_id = cursor.getString(cursor.getColumnIndex("source_id"));
            Log.e(TAG, "_id = " + _id + ", source_id = " + source_id);
        }
    }

    // 拷贝数据库
    private void copyDB(String dbName, int dbRes) {
        File file = new File(Environment.getExternalStorageDirectory(), dbName);
        if (file.exists()) {
            Log.e(TAG, "数据库已复制到目标路径");
        } else {
            InputStream inputStream = getResources().openRawResource(dbRes);
            try {
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
                Log.e(TAG, "数据库已成功复制到目标路径");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 打开数据库
    private void openDB(String path) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select * from statistics where event_type = ?", new String[]{"1"});
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String source_id = cursor.getString(cursor.getColumnIndex("source_id"));
            Log.e(TAG, "_id = " + _id + ", source_id = " + source_id);
        }
    }

}
