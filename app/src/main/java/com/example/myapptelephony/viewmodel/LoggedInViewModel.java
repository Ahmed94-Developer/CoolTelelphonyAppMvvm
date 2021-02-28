package com.example.myapptelephony.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapptelephony.repository.AuthAppRepository;


public class LoggedInViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<Boolean> userLoggedOut;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLoggedOut = authAppRepository.getUserloggedOut();
    }
    public void logOut(){
        authAppRepository.logOut();
    }

    public MutableLiveData<Boolean> getUserLoggedOut() {
        return userLoggedOut;
    }
}
