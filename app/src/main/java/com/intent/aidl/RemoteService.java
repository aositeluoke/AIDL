package com.intent.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月09日
 */
public class RemoteService extends Service {
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IBookListener> callbackList = new RemoteCallbackList<>();
    private AtomicBoolean isDestroy = new AtomicBoolean(false);
    private IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public void register(IBookListener listener) throws RemoteException {
            callbackList.register(listener);
        }

        @Override
        public void unRegister(IBookListener listener) throws RemoteException {
            callbackList.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(bookWorker).start();

    }

    private Runnable bookWorker = new Runnable() {
        @Override
        public void run() {
            while (!isDestroy.get()) {
                try {
                    Thread.sleep(2000);
                    int bookId = mBooks.size() + 1;
                    Book book = new Book(bookId, "new book" + bookId);
                    mBooks.add(book);
                    onNewBookArrive(book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void onNewBookArrive(Book book) throws RemoteException {
        for (int i = 0, n = callbackList.beginBroadcast(); i < n; i++) {
            IBookListener listener = callbackList.getBroadcastItem(i);
            if (listener != null) {
                listener.onBookArrive(book);
            }
        }

        callbackList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy.set(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
