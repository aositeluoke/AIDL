package com.intent.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private IBookManager iBookManager;
    private IBookListener.Stub iBookListener = new IBookListener.Stub() {

        @Override
        public void onBookArrive(Book book) throws RemoteException {
            Log.i(TAG, "onBookArrive: 新书到达了" + book.getBookName() + ",当前线程:" + Thread.currentThread());
        }
    };
    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                iBookManager.register(iBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //获取所有书籍
            try {
                List<Book> books = iBookManager.getBookList();
                Iterator<Book> iterator = books.iterator();
                while (iterator.hasNext()) {
                    Book book = iterator.next();
                    Log.i(TAG, "id: " + book.getBookId() + ",name:" + book.getBookName());
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (iBookManager != null) {
                try {
                    iBookManager.unRegister(iBookListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                iBookManager = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, cnn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(cnn);
    }

    private static int id = 10;

    public void onAddNewBook(View view) {
        if (iBookManager != null) {
            try {
                iBookManager.addBook(new Book(id++, id + ""));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
