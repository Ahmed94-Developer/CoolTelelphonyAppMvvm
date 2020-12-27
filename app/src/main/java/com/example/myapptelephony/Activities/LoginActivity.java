package com.example.myapptelephony.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapptelephony.NetWorker;
import com.example.myapptelephony.R;

import java.util.concurrent.TimeUnit;

import static com.example.myapptelephony.Activities.MainActivity.KEY_TASK_DESC;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin;
    EditText EmailText,passWordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        EmailText = (EditText) findViewById(R.id.mail);
        passWordText = (EditText) findViewById(R.id.passWord);

        btnLogin.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        String Mail = EmailText.getText().toString();
        String passWord = passWordText.getText().toString();
        if (!TextUtils.isEmpty(Mail) && !TextUtils.isEmpty(passWord)){
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(LoginActivity.this, "some fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}
