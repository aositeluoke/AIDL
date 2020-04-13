package com.intent.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.intent.aidlx.AIDLService;

import java.util.List;

/**
 * 类描述:相当于在服务端提供的代码
 * 作者:xues
 * 时间:2020年04月13日
 */
public class IBinderPoolImpl extends IBinderPool.Stub {
    private static final String TAG = "IBinderPoolImpl";
    private AIDLService mAIDLService;

    public IBinderPoolImpl(AIDLService service) {
        mAIDLService = service;
    }

    @Override
    public IBinder queryBiner(int binderCode) throws RemoteException {
        Binder binder = null;
        switch (binderCode) {
            case BinderPool.BINDER_CODE_BOOK:
                binder = new IBookManager.Stub() {
                    @Override
                    public void addBook(Book book) throws RemoteException {
                        Log.i(TAG, "addBook: "+book.getBookName());
                        mAIDLService.books.add(book);
                    }

                    @Override
                    public List<Book> getBookList() throws RemoteException {
                        return mAIDLService.books;
                    }

                    @Override
                    public void register(IBookListener listener) throws RemoteException {
                        mAIDLService.listeners.register(listener);
                    }

                    @Override
                    public void unregister(IBookListener listener) throws RemoteException {
                        mAIDLService.listeners.unregister(listener);
                    }
                };
                break;
            case BinderPool.BINDER_CODE_USER:
                binder = new IUserManager.Stub() {
                    @Override
                    public void onCreateUser(User user) throws RemoteException {
                        Log.i(TAG, "onCreateUser: 客户端创建User," + user.getName());
                    }
                };
                break;
        }
        return binder;
    }
}
