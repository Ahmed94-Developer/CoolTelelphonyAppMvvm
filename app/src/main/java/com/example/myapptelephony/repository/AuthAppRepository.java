package com.example.myapptelephony.repository;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapptelephony.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppRepository {
    private Application application;
    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> userloggedOut;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.userloggedOut = new MutableLiveData<>();

    }
    public void login(String email,String passWord){
        mAuth.signInWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(application.getApplicationContext(), MainActivity.class);
                    application.getApplicationContext().startActivity(i);
                    userLiveData.postValue(mAuth.getCurrentUser());
                    userloggedOut.postValue(false);
                }else {
                    Toast.makeText(application.getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void Register(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(application.getApplicationContext(),MainActivity.class);
                    application.getApplicationContext().startActivity(i);
                    userLiveData.postValue(mAuth.getCurrentUser());
                }else {
                    Toast.makeText(application.getApplicationContext(), "Registiration Failed", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }
    public void logOut(){
        mAuth.signOut();
        userloggedOut.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getUserloggedOut() {
        return userloggedOut;
    }
}
