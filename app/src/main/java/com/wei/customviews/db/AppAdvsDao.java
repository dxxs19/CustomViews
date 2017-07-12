package com.wei.customviews.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wei.utillibrary.utils.TimeUtils;

/**
 * app广告统计表
 * author: WEI
 * date: 2017/5/27
 */

public class AppAdvsDao
{
    private final String TAG = getClass().getSimpleName();
    private static AppAdvsDao sAdvsDao;
    private AppAdvsSQLiteHelper mSQLiteHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public AppAdvsDao(Context context)
    {
        mContext = context;
        mSQLiteHelper = new AppAdvsSQLiteHelper(context, UserDAO.DATABASE_NAME, null, 1);
        mDatabase = mSQLiteHelper.getWritableDatabase();
    }

    public static AppAdvsDao getInstance(Context context)
    {
        if (sAdvsDao == null)
        {
            sAdvsDao = new AppAdvsDao(context);
        }
        return sAdvsDao;
    }

    public void insertData(String sourceId, int eventType)
    {
        String sql = "insert into " + AppAdvsSQLiteHelper.TABLE_NAME + "("
                + AppAdvsSQLiteHelper.SOURCE_ID + ","
                + AppAdvsSQLiteHelper.EVENT_TYPE + ","
                + AppAdvsSQLiteHelper.DATE + " ) values " + "( '"
                + sourceId + "','"
                + eventType + "','"
                + TimeUtils.getDay(System.currentTimeMillis())
                + "')";
        Log.e(TAG, "insertData : " + sql);
        mDatabase.execSQL(sql);
        mDatabase.close();
    }

    /**
     * 根据事件类别来统计该类事件发生的次数
     * @param eventType
     * @return
     */
    public int getCountByType(int eventType)
    {
        String sql = "select count(" + AppAdvsSQLiteHelper.EVENT_TYPE  + " ) from " +
                AppAdvsSQLiteHelper.TABLE_NAME + " group by " + AppAdvsSQLiteHelper.DATE +
                ", group by " + AppAdvsSQLiteHelper.EVENT_TYPE;
        Log.e(TAG, "getCountByType : " + sql);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String sourceId = cursor.getString(cursor.getColumnIndex(AppAdvsSQLiteHelper.SOURCE_ID));
            int type = cursor.getInt(cursor.getColumnIndex(AppAdvsSQLiteHelper.EVENT_TYPE));
            String date = cursor.getString(cursor.getColumnIndex(AppAdvsSQLiteHelper.DATE));
            StringBuilder builder = new StringBuilder();
            builder.append(id)
                    .append(",")
                    .append(sourceId)
                    .append(",")
                    .append(type)
                    .append(",")
                    .append(date);
            Log.e(TAG, builder.toString());
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}
