package com.example.myapptelephony.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NetWorkDao {

    @Insert
    void insert(NetWork netWork);

    @Update
    void Update(NetWork netWork);

    @Delete
    void delete(NetWork netWork);

    @Query("DELETE FROM network_table")
    void deleteAllInfo();

    @Query("SELECT *FROM network_table ")
    LiveData<List<NetWork>> getAllInfo();


}
