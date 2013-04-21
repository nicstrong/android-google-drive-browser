package com.nicstrong.android.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import roboguice.content.RoboContentProvider;

public class GoogleDriveContentProvider extends RoboContentProvider {
    private static final int FILES = 1;
    private static final int FILE_ID = 2;

    private static UriMatcher URI_MATCHER = null;

    @Override
    public boolean onCreate() {
        if (URI_MATCHER == null) {
            URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
            // can't do at static construction because we need a context to read the online authority
            URI_MATCHER.addURI(GoogleDriveContract.AUTHORITY, "files", FILES);
            URI_MATCHER.addURI(GoogleDriveContract.AUTHORITY, "files/*", FILE_ID);
        }
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case FILES:
                return GoogleDriveContract.File.CONTENT_DIR_TYPE;
            case FILE_ID:
                return GoogleDriveContract.File.CONTENT_ITEM_TYPE;
            default: {
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case FILES:

            case FILE_ID:
            default: {
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case FILES:

            case FILE_ID:
            default: {
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
