package com.intent.aidlx;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.intent.aidl.Book;
import com.intent.aidl.IBinderPoolImpl;
import com.intent.aidl.IBookListener;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月10日
 */
public class AIDLService extends Service {

    private Binder binder;
    private AtomicBoolean isDestory = new AtomicBoolean(false);
    public CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();
    public RemoteCallbackList<IBookListener> listeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new IBinderPoolImpl(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestory.get()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int id = books.size() + 1;
                    Book book = new Book(id, "bookName:id_" + id + "," + Thread.currentThread());
                    books.add(book);
                    onNewBookArrived(book);
                }
            }
        }).start();
    }

    private void onNewBookArrived(Book book) {
        int N = listeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IBookListener listener = listeners.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onNewBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        listeners.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        isDestory.set(true);
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int result = checkCallingOrSelfPermission("com.intent.aidlx.permission.ACCESS_BOOK_SERVICE");
        if (result == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return binder;
    }
}
