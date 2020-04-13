package com.intent.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.intent.aidlx.AIDLService;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class BinderPool {
    public static final int BINDER_CODE_BOOK = 1;
    public static final int BINDER_CODE_USER = 2;

    private static BinderPool pool;
    private Context mContext;

    private static final String TAG = "BinderPool";

    private IBinderPool iBinderPool;

    private BinderPool(Context context) {
        mContext = context;
    }

    public static BinderPool getInstance(Context context) {
        if (pool == null) {
            synchronized (BinderPool.class) {
                if (pool == null) {
                    pool = new BinderPool(context);
                }
            }
        }
        return pool;
    }


    private IBinder.DeathRecipient mRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBinderPool == null) {
                return;
            }
            iBinderPool.asBinder().unlinkToDeath(mRecipient, 0);
            iBinderPool = null;
            // TODO: 2020-04-13 重新链接服务

        }
    };

    private IBookListener listener = new IBookListener.Stub() {
        @Override
        public void onNewBook(Book book) throws RemoteException {
            Log.i(TAG, "onNewBook: " + book.getBookName());
        }
    };
    private ServiceConnection mCnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                //常见错误：（IUserManager）iBinderPool.queryBiner(BINDER_CODE_USER)
                IUserManager userManager = IUserManager.Stub.asInterface(iBinderPool.queryBiner(BINDER_CODE_USER));


                IBookManager bookManager = IBookManager.Stub.asInterface(iBinderPool.queryBiner(BINDER_CODE_BOOK));
                bookManager.register(listener);

                bookManager.addBook(new Book(12, "艺术探索"));
                userManager.onCreateUser(new User(20, "aositeluoke"));

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void bindService(Context context) {
        context.bindService(new Intent(context, AIDLService.class), mCnn, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        context.unbindService(mCnn);
    }

}
