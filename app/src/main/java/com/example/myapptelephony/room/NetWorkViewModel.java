package com.example.myapptelephony.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NetWorkViewModel extends AndroidViewModel {
    private NetWorkRepository repository;
    private LiveData<List<NetWork>> netWorkInfo;
    private NetWork netWork;
    public NetWorkViewModel(@NonNull Application application) {
        super(application);
        repository = new NetWorkRepository(application);
        netWorkInfo = repository.getNetWorkInfo();
    }
    public void insert(NetWork netWork){
        repository.insert(netWork);
    }
    public void Update(NetWork netWork){
        repository.Update(netWork);
    }
    public void Delete(NetWork netWork){
        repository.delete(netWork);
    }
    public void DeleteAll(){
        repository.deleteAll();
    }
    public LiveData<List<NetWork>> getNetWorkInfo(){
        return netWorkInfo;
    }
}
