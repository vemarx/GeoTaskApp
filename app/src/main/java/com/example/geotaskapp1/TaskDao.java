package com.example.geotaskapp1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long inserir(Task task);

    @Update
    void atualizar(Task task);

    @Delete
    void excluir(Task task);

    @Query("SELECT * FROM Task")
    List<Task> listar();
}