package com.example.geotaskapp1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void inserir(Task task);

    @Query("SELECT * FROM Task")
    List<Task> listar();
}
