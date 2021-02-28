package com.example.myapptelephony.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapptelephony.repository.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }
    public void login(String email,String password){
        authAppRepository.login(email,password);
    }
    public void Register(String email,String password){
        authAppRepository.Register(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}
