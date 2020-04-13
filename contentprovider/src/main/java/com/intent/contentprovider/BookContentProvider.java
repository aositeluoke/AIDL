package com.intent.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 类描述:
 * 作者:xuesA
 * 时间:2020年04月13日
 */
public class BookContentProvider extends ContentProvider {
    private SQLiteDatabase db;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "com.intent.contentprovider";
    private static final int BOOK_CODE = 0;
    private static final int USER_CODE = 1;

    static {
        mUriMatcher.addURI(AUTHORITY, "book", BOOK_CODE);
        mUriMatcher.addURI(AUTHORITY, "user", USER_CODE);
    }

    @Override
    public boolean onCreate() {
        db = new DbOpenHelper(getContext()).getWritableDatabase();
//        db.execSQL("insert into book values(1,'Android')");
//        db.execSQL("insert into book values(2,'JAVA')");
//        db.execSQL("insert into book values(3,'IOS')");
//        db.execSQL("insert into user values(1,'徐恩晟',19)");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        long row = db.insert(tableName, null, values);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int row = db.delete(getTableName(uri), selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int row = db.update(getTableName(uri), values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case BOOK_CODE:
                return DbOpenHelper.BOOK_TABLE_NAME;
            case USER_CODE:
                return DbOpenHelper.USER_TABLE_NAME;
        }
        throw new IllegalArgumentException("Unsupport uri:" + uri);
    }
}
