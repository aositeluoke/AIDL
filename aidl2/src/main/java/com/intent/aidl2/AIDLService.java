package com.intent.aidl2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.intent.aidl.IPoolManagerImpl;

import androidx.annotation.Nullable;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class AIDLService extends Service {
    private IPoolManagerImpl poolManager = new IPoolManagerImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return poolManager;
    }

    @Override
    public void onDestroy() {
        poolManager.onDestroy();
        super.onDestroy();
    }
}
