package com.universitedebordeaux.jumathsji.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Line only.
public interface LineSQLDao {

    @Query("SELECT * FROM Line WHERE line = :text")
    List<Line> getLinesByContents(String text);
}