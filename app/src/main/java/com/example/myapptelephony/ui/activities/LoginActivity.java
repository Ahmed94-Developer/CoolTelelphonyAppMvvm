package com.example.myapptelephony.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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

import com.example.myapptelephony.databinding.ActivityLoginBinding;
import com.example.myapptelephony.worker.NetWorker;
import com.example.myapptelephony.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import static com.example.myapptelephony.ui.activities.MainActivity.KEY_TASK_DESC;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin;
    EditText EmailText,passWordText;
    FirebaseAuth muth;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.btnLogin.setOnClickListener(this);

        FirebaseApp.initializeApp(this);
        muth = FirebaseAuth.getInstance();

       // btnLogin.setOnClickListener(this);

        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Loading Network Details").build();
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
        String mail = binding.mail.getText().toString();
        String passWord = binding.passWord.getText().toString();
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(passWord)){
            muth.signInWithEmailAndPassword(mail,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                    }else {
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(LoginActivity.this, "some fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
