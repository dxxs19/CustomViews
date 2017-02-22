package com.wei.customviews.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.R.attr.id;
import static android.R.attr.value;
import static de.greenrobot.event.EventBus.TAG;

/**
 *
 */
public class UserContentProvider extends ContentProvider
{
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static String AUTHORITY = "com.wei.customviews.UsercontentProvider";
    private DatabaseHelper mDatabaseHelper;
    private static final int PERSON_INSERT_CODE = 0;
    private static final int PERSON_DELETE_CODE = 1;
    private static final int PERSON_UPDATE_CODE = 2;
    private static final int PERSON_QUERYALL_CODE = 3;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static {
        URI_MATCHER.addURI(AUTHORITY, "user/insert", PERSON_INSERT_CODE);
        URI_MATCHER.addURI(AUTHORITY, "user/delete", PERSON_DELETE_CODE);
        URI_MATCHER.addURI(AUTHORITY, "user/update", PERSON_UPDATE_CODE);
        URI_MATCHER.addURI(AUTHORITY, "user/queryAll", PERSON_QUERYALL_CODE);
    }

    public UserContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri))
        {
            case PERSON_DELETE_CODE:
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
                if (database.isOpen())
                {
                    int count = database.delete(DatabaseHelper.USER_TABLE, selection, selectionArgs);
                    database.close();
                    return count;
                }
                break;
        }
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
    public Uri insert(Uri uri, ContentValues values)
    {
        switch (URI_MATCHER.match(uri))
        {
            case PERSON_INSERT_CODE:
                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                if (db.isOpen())
                {
                    long id = db.insert(DatabaseHelper.USER_TABLE, null, values);
                    db.close();
                    Uri resultUri = ContentUris.withAppendedId(uri, id);
                    Log.e(TAG, "resultUri = " + resultUri);
                    return resultUri;
                }
                break;
        }
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate()
    {
        // TODO: Implement this to initialize your content provider on startup.
        mDatabaseHelper = UserDAO.getInstance(getContext()).getDatabaseHelper();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri))
        {
            case PERSON_QUERYALL_CODE:
                SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
                if (database.isOpen())
                {
                    Cursor cursor = database.query(DatabaseHelper.USER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                    return cursor;
                }
                break;
        }
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (URI_MATCHER.match(uri))
        {
            case PERSON_UPDATE_CODE:
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
                if (database.isOpen())
                {
                    int count = database.update(DatabaseHelper.USER_TABLE, values, selection, selectionArgs);
                    database.close();
                    return count;
                }
                break;
        }
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
