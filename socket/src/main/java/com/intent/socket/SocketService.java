package com.intent.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class SocketService extends Service {
    private AtomicBoolean isDestory = new AtomicBoolean(false);
    private final ThreadLocal<Integer> tl = new ThreadLocal<>();

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestory.get()) {
                    try {
                        ServerSocket serverSocket = new ServerSocket(8089);
                        final Socket socket = serverSocket.accept();
                        Log.i(TAG, "服务端:链接成功 ");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    handleClient(socket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private static final String TAG = "SocketService";

    private void handleClient(Socket socket) throws IOException {
        tl.set(0);
        //最后一个参数一定要autoFlush,否则收不到数据
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (!isDestory.get()) {
            String msg = reader.readLine();
            int num = tl.get() + 1;
            tl.set(num);
            //使用print收不到数据
            writer.println("服务端Socket收到了" + num);
            Log.i(TAG, "客户端发过来的消息: " + msg);
        }

        reader.close();
        writer.close();
        socket.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isDestory.set(true);
        super.onDestroy();
    }
}
