package com.example.myapptelephony.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapptelephony.model.NetWork;

@Dao
public interface NetWorkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NetWork netWork);

    @Update
    void Update(NetWork netWork);

    @Delete
    void delete(NetWork netWork);

    @Query("DELETE FROM network_table")
    void deleteAllInfo();

    @Query("SELECT *FROM network_table ")
    LiveData<NetWork> getAllInfo();
}
