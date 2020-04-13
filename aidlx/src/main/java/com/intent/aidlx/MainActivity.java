package com.intent.aidlx;

import android.os.Bundle;

import com.intent.aidl.BinderPool;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BinderPool.getInstance(this).bindService(this);

    }

    @Override
    protected void onDestroy() {
        BinderPool.getInstance(this).unbindService(this);
        super.onDestroy();

    }
}
