package com.example.geotaskapp1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;

    // Chave do registro no Firebase
    public String firebaseKey;

    @Override
    public String toString() {
        return "✔ " + titulo;
    }
}