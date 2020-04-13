package com.intent.socket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et_content);
        startService(new Intent(this, SocketService.class));
        connectSocketServer();
    }

    private PrintWriter printWriter;
    private static final String TAG = "MainActivity";

    private void connectSocketServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                while (socket == null)
                    try {
                        socket = new Socket("localhost", 8089);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                Log.i(TAG, "run:链接成功");
                try {
                    printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (!MainActivity.this.isFinishing()) {
                        String msg = reader.readLine();
                        Log.i(TAG, "服务器发来的消息: " + msg);
                    }
                    printWriter.close();
                    reader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "run: "+e.getMessage());
                }

            }
        }).start();
    }

    public void onSend(View view) {
        if (printWriter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    printWriter.println(editText.getText().toString());
                }
            }).start();
        }
    }
}
