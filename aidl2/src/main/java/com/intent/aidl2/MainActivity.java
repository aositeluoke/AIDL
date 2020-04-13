package com.intent.aidl2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.intent.aidl.Book;
import com.intent.aidl.IBookListener;
import com.intent.aidl.IBookManager;
import com.intent.aidl.IPoolManager;
import com.intent.aidl.IPoolManagerImpl;
import com.intent.aidl.IUserManager;
import com.intent.aidl.User;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iPoolManager == null) {
                return;
            }
            iPoolManager.asBinder().unlinkToDeath(deathRecipient, 0);
            iPoolManager = null;
            // TODO: 2020-04-13 重连

        }
    };
    private static final String TAG = "MainActivity";
    private IPoolManager iPoolManager;
    private IBookListener mBookListener = new IBookListener.Stub() {
        @Override
        public void onNewBook(Book book) throws RemoteException {
            Log.i(TAG, "onNewBook: " + book.getBookName());
        }
    };
    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IPoolManager manager = IPoolManager.Stub.asInterface(service);
            try {
                IBookManager bookManager = IBookManager.Stub.asInterface(manager.queryBinder(IPoolManagerImpl.BIND_CODE_BOOK));
                bookManager.register(mBookListener);


                IUserManager userManager = IUserManager.Stub.asInterface(manager.queryBinder(IPoolManagerImpl.BIND_CODE_USER));
                userManager.createUser(new User(27, "aositeluoke"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, cnn, Context.BIND_AUTO_CREATE);
    }
}
