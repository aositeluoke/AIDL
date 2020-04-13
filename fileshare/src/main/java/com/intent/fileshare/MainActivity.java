package com.intent.fileshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    public static final String Object_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "student.txt";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestWritePermissions();
    }

    private void requestWritePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        } else {
            writeToFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 110:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        writeToFile();
                    }
                }
                break;
        }
    }

    private void writeToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
//                    Log.i(TAG, "run: 无权限");
//                    return;
//                }
                Student student = new Student(10, "徐恩晟");
                File file = new File(Object_FILE_PATH);
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(student);
                    Log.i(TAG, "run: 写入成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onRead(View view) {
        Intent intent = new Intent(this, NewProcessActivity.class);
        startActivity(intent);
    }
}
