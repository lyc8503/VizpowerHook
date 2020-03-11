package me.lyc8503.vizpowerhook.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "MyContentProvider";

    static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI("me.lyc8503.vizpowerhook", "config", 666);
    }

    SharedPreferences preferences;

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
    public boolean onCreate() {
        try {
            preferences = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
            Log.d(TAG, "MyContentProvider: " + preferences.getAll());

            return true;
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {
            case 666:
                MatrixCursor cursor = new MatrixCursor(new String[]{"name", "forceVertical", "autoRollcall"});
                cursor.addRow(new Object[]{
                        preferences.getString("name", ""),
                        String.valueOf(preferences.getBoolean("forceVertical", false)),
                        String.valueOf(preferences.getBoolean("autoRollcall", false))
                });

                return cursor;
            default:
                try {
                    throw new IllegalAccessException("Invalid URI: " + uri);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "query: ", e);
                }
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
