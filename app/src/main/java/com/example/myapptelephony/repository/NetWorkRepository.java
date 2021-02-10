package com.example.myapptelephony.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapptelephony.model.NetWork;
import com.example.myapptelephony.room.NetWorkDao;
import com.example.myapptelephony.room.NetWorkDataBase;

public class NetWorkRepository {
    private NetWorkDao netWorkDao;
    private LiveData<NetWork> netWorkInfo;

    public NetWorkRepository(Application application) {
        NetWorkDataBase dataBase = NetWorkDataBase.getInstance(application);
        netWorkDao = dataBase.netWorkDao();
        netWorkInfo = netWorkDao.getAllInfo();
    }
    public void insert(NetWork netWork){
        new InsertAsyncTask(netWorkDao).execute(netWork);

    }
    public void Update(NetWork netWork){
        new UpdateAsyncTask(netWorkDao).execute(netWork);
    }
    public void delete(NetWork netWork){
        new deleteAsyncTask(netWorkDao).execute(netWork);
    }
    public void deleteAll(){
        new deleteAllAsyncTask(netWorkDao).execute();
    }
    public LiveData<NetWork> getNetWorkInfo(){
        return netWorkInfo;
    }

    private static class InsertAsyncTask extends AsyncTask<NetWork,Void,Void>{
       private NetWorkDao netWorkDao;

        public InsertAsyncTask(NetWorkDao netWorkDao) {
            this.netWorkDao = netWorkDao;
        }

        @Override
        protected Void doInBackground(NetWork... netWorks) {
            netWorkDao.insert(netWorks[0]);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<NetWork,Void,Void>{
        private NetWorkDao netWorkDao;

        public UpdateAsyncTask(NetWorkDao netWorkDao) {
            this.netWorkDao = netWorkDao;
        }

        @Override
        protected Void doInBackground(NetWork... netWorks) {
            netWorkDao.Update(netWorks[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<NetWork,Void,Void>{
        private NetWorkDao netWorkDao;

        public deleteAsyncTask(NetWorkDao netWorkDao) {
            this.netWorkDao = netWorkDao;
        }

        @Override
        protected Void doInBackground(NetWork... netWorks) {
            netWorkDao.delete(netWorks[0]);
            return null;
        }
    }
    private static class deleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
       private NetWorkDao netWorkDao;

        public deleteAllAsyncTask(NetWorkDao netWorkDao) {
            this.netWorkDao = netWorkDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            netWorkDao.deleteAllInfo();
            return null;
        }
    }
}
