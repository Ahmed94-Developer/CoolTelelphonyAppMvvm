package com.example.myapptelephony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import static com.example.myapptelephony.MainActivity.KEY_TASK_DESC;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Loading work data").build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest  request = new OneTimeWorkRequest.Builder(NetWorker.class)
                .addTag("my_tag")
                .setConstraints(constraints)
                .setInitialDelay(0,TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();
        WorkManager.getInstance(LoginActivity.this)
                .enqueue(request);


    }
}
