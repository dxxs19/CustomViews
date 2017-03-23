package com.wei.utillibrary.net.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class DownloadContentProvider extends ContentProvider
{
    private static final String TAG = "DownloadContentProvider";
    private static final String AUTHORITY = "com.wei.utillibrary.download_contentprovider";
    private static final String DB_NAME = "download_db";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int INSERT_CODE = 1;
    private static final int DELETEALL_BYURL_CODE = 2;
    private static final int UPDATE_CODE = 3;
    private static final int QUERYALL_BYURL_CODE = 4;
    private DBOpenHelper mOpenHelper;
    private SQLiteDatabase database;
    private static DownloadContentProvider sDownloadContentProvider;

    static {
        URI_MATCHER.addURI(AUTHORITY, "download/insert", INSERT_CODE);
        URI_MATCHER.addURI(AUTHORITY, "download/deleteByUrl", DELETEALL_BYURL_CODE);
        URI_MATCHER.addURI(AUTHORITY, "download/update", UPDATE_CODE);
        URI_MATCHER.addURI(AUTHORITY, "download/queryByUrl", QUERYALL_BYURL_CODE);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mOpenHelper = new DBOpenHelper(getContext(), DB_NAME, null, 1);
        database = mOpenHelper.getWritableDatabase();
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        switch (URI_MATCHER.match(uri))
        {
            case DELETEALL_BYURL_CODE:
                if (database.isOpen())
                {
                    int deleteCount = database.delete(DBOpenHelper.TABLE_NAME, selection, selectionArgs);
//                    database.close();
                    return deleteCount;
                }
                break;
        }
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
        switch (URI_MATCHER.match(uri))
        {
            case INSERT_CODE:
                if (database.isOpen())
                {
                    long id = database.insert(DBOpenHelper.TABLE_NAME, null, values);
//                    database.close();
                    Uri resultUri = ContentUris.withAppendedId(uri, id);
//                    Log.e(TAG, "resultUri = " + resultUri.toString());
                    return resultUri;
                }
                break;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        switch (URI_MATCHER.match(uri))
        {
            case QUERYALL_BYURL_CODE:
                if (database.isOpen())
                {
                    Cursor cursor = database.query(DBOpenHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                    return cursor;
                }
                break;
        }
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        switch (URI_MATCHER.match(uri))
        {
            case UPDATE_CODE:
                if (database.isOpen())
                {
                    int updateCount = database.update(DBOpenHelper.TABLE_NAME, values, selection, selectionArgs);
//                    database.close();
                    return updateCount;
                }
                break;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
