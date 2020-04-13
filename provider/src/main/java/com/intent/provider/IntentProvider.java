package com.intent.provider;

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
 * 作者:xues
 * 时间:2020年04月13日
 */
public class IntentProvider extends ContentProvider {
    public SQLiteDatabase db;

    public static final int TABLE_CODE_BOOK = 0;
    public static final int TABLE_CODE_USER = 1;

    public static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String AUTHORITY = "com.intent.provider";

    static {
        mUriMatcher.addURI(AUTHORITY, DbOpenHelper.BOOK_TABLE_NAME, TABLE_CODE_BOOK);
        mUriMatcher.addURI(AUTHORITY, DbOpenHelper.USER_TABLE_NAME, TABLE_CODE_USER);
    }

    @Override
    public boolean onCreate() {
        db = new DbOpenHelper(getContext()).getWritableDatabase();
        db.execSQL("insert into book values(1,'Android艺术探索')");
        db.execSQL("insert into book values(2,'Java艺术探索')");
        db.execSQL("insert into book values(3,'IOS艺术探索')");
        db.execSQL("insert into user values(3,'aositeluoke',28)");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return db.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long row = db.insert(getTableName(uri), null, values);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
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

    public String getTableName(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case TABLE_CODE_BOOK:
                return DbOpenHelper.BOOK_TABLE_NAME;
            case TABLE_CODE_USER:
                return DbOpenHelper.USER_TABLE_NAME;
        }
        throw new IllegalArgumentException("Unsuport uri:" + uri);
    }
}
