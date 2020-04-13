package com.intent.aidl;

import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class IPoolManagerImpl extends IPoolManager.Stub {
    public static final int BIND_CODE_BOOK = 0;
    public static final int BIND_CODE_USER = 1;
    public AtomicBoolean isDestory = new AtomicBoolean(false);
    public RemoteCallbackList<IBookListener> mListener = new RemoteCallbackList<>();
    public CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();


    public IPoolManagerImpl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestory.get()) {
                    try {
                        Thread.sleep(1000);
                        Book book = new Book(mBooks.size(), Thread.currentThread() + "-" + mBooks.size());
                        mBooks.add(book);
                        onNewBook(book);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }

    private static final String TAG = "IPoolManagerImpl";

    @Override
    public IBinder queryBinder(int bindCode) throws RemoteException {
        IBinder binder = null;
        switch (bindCode) {
            case BIND_CODE_BOOK:
                binder = new IBookManager.Stub() {
                    @Override
                    public void register(IBookListener listener) throws RemoteException {
                        mListener.register(listener);
                    }

                    @Override
                    public void unregister(IBookListener listener) throws RemoteException {
                        mListener.unregister(listener);
                    }
                };
                break;
            case BIND_CODE_USER:
                binder = new IUserManager.Stub() {
                    @Override
                    public void createUser(User user) throws RemoteException {
                        Log.i(TAG, "创建新用户: " + user.getName());
                    }
                };
                break;
        }
        return binder;
    }

    public void onNewBook(Book book) {
        int N = mListener.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IBookListener listener = mListener.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onNewBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
        mListener.finishBroadcast();
    }

    public void onDestroy() {
        isDestory.set(true);
    }
}
