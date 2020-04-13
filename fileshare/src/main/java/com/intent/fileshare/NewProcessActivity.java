package com.intent.fileshare;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月12日
 */
public class NewProcessActivity extends AppCompatActivity {
    TextView tvStudent;
    private static final String TAG = "NewProcessActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_process);
        tvStudent = findViewById(R.id.tv_student);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream fs = new FileInputStream(new File(MainActivity.Object_FILE_PATH));
                    ObjectInputStream objectInputStream = new ObjectInputStream(fs);
                    final Student student = (Student) objectInputStream.readObject();
                    Log.i(TAG, "run: 读取成功"+student);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvStudent.setText(student.getName());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
