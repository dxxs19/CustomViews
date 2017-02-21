package com.wei.customviews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * author: WEI
 * date: 2017/2/21
 */

public class UserDAO
{
    public final static String DATABASE_NAME = "customviews.db";
    private static UserDAO mUserDAO ;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;
    private SQLiteDatabase db;

    public UserDAO(Context context)
    {
        mContext = context;
        mDatabaseHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, 1);
        db = mDatabaseHelper.getWritableDatabase();
    }

    public static UserDAO getInstance(Context context)
    {
        if (mUserDAO == null)
        {
            synchronized (UserDAO.class)
            {
                if (mUserDAO == null)
                {
                    mUserDAO = new UserDAO(context);
                }
            }
        }
        return mUserDAO;
    }

    public void addUser(ContentValues values)
    {
        db.insert(DatabaseHelper.USER_TABLE, null, values);
    }

    public Cursor queryAllUsers()
    {
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.USER_TABLE, null);
        return cursor;
    }

}
