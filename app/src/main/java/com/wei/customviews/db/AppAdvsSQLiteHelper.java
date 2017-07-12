package com.wei.customviews.db;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * author: WEI
 * date: 2017/5/27
 */

public class AppAdvsSQLiteHelper extends SQLiteOpenHelper
{
    private final String TAG = getClass().getSimpleName();
    public static final String TABLE_NAME = "statistics"; // 统计表
    public static final String SOURCE_ID = "source_id"; // 事件源主键
    public static final String EVENT_TYPE = "event_type"; // 自定义事件类型，例如，0表示打开广告，1表示点击广告......
    public static final String DATE = "date"; // 事件发生日期，精确到天

    public AppAdvsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AppAdvsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

//    public AppAdvsSQLiteHelper(Context context) {
//        super(context, UserDAO.DATABASE_NAME, null, 1);
//    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SOURCE_ID + " TEXT,"
                + EVENT_TYPE + " INTEGER,"
                + DATE + " TEXT" +
                ")";
        Log.i(TAG, "创建" + TABLE_NAME + "表:" + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "--onUpgrade()-- 数据库版本升级 from " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            default:
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
        }
    }
}
