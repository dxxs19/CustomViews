package com.wei.customviews.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.xbill.DNS.tests.primary;

import static android.R.attr.key;
import static android.R.attr.password;

/**
 * author: WEI
 * date: 2017/2/21
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public final static String USER_TABLE = "user";
    private String sql_user = "create table " +  USER_TABLE + "(_id integer primary key autoincrement, username text, password text);";

    /**
     * @param context  上下文环境（例如，一个 Activity）
     * @param name   数据库名字
     * @param factory  一个可选的游标工厂（通常是 Null）
     * @param version  数据库模型版本的整数
     *
     * 会调用父类 SQLiteOpenHelper的构造函数
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     * @param errorHandler
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     *  在数据库第一次创建的时候会调用这个方法
     *  根据需要对传入的SQLiteDatabase 对象填充表和初始化数据。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_user);
    }

    /**
     * 当数据库需要修改的时候（两个数据库版本不同），Android系统会主动的调用这个方法。
     * 一般我们在这个方法里边删除数据库表，并建立新的数据库表.
     * @param db     SQLiteDatabase 对象
     * @param oldVersion 旧的版本号
     * @param newVersion 新的版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
