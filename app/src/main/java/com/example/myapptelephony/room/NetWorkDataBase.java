package com.example.myapptelephony.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapptelephony.model.NetWork;

@Database(entities = {NetWork.class},version = 1)
public abstract class NetWorkDataBase extends RoomDatabase {
    public static NetWorkDataBase instance;
    public abstract NetWorkDao netWorkDao();

    public static synchronized NetWorkDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NetWorkDataBase.class,"network_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
    public static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }
    };
    private static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>{
        private NetWorkDao netWorkDao;
        private PopulateAsyncTask(NetWorkDataBase db){
            netWorkDao = db.netWorkDao();
        }

        public PopulateAsyncTask(NetWorkDao netWorkDao) {
            this.netWorkDao = netWorkDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
