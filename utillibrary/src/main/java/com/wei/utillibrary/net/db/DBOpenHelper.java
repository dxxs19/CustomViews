package com.wei.utillibrary.net.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author: WEI
 * date: 2017/3/17
 */

public class DBOpenHelper extends SQLiteOpenHelper
{
    public static final String TABLE_NAME = "downloadInfos_table";
    private final String CREATE_TABLE_SQL = "Create table " + TABLE_NAME
            + "(id integer primary key autoincrement, "
            + "download_length real,"
            + "total_length real,"
            + "download_percent real,"
            + "thread_id integer,"
            + "download_url text)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
