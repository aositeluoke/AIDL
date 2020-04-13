package com.intent.contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //uri不要写错了，错误写法：content://com.intent.contentprovider.book
                Uri book = Uri.parse("content://com.intent.contentprovider/book");
                Cursor bookCursor = getContentResolver().query(book, null, null, null, null);
                while (bookCursor.moveToNext()) {
                    int _id = bookCursor.getInt(bookCursor.getColumnIndex("_id"));
                    String name = bookCursor.getString(bookCursor.getColumnIndex("name"));
                    Log.i(TAG, "onCreate: " + _id + ",name:" + name);
                }
                bookCursor.close();
            }
        }).start();
    }
}
