package com.wei.customviews.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import static org.xbill.DNS.Opcode.QUERY;

public class MyContentProvider extends ContentProvider
{
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String AUTHORITY = "com.wei.customviews.CONTENT_PROVIDER";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY);
    private static final int QUERY_STATISTICS = 1;
    private static final int UPDATE_STATISTICS = 2;
    private DatabaseHelper mDatabaseHelper;
    static {
        URI_MATCHER.addURI(AUTHORITY, "statistics/query", QUERY_STATISTICS);
        URI_MATCHER.addURI(AUTHORITY, "statistics/update", UPDATE_STATISTICS);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        switch (URI_MATCHER.match(uri))
        {
            case QUERY_STATISTICS:
                String path = Environment.getExternalStorageDirectory() + File.separator + "chat.db";
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = database.rawQuery("select * from statistics where event_type = ?", new String[]{"1"});
                return cursor;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
