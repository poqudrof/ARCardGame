package com.universitedebordeaux.jumathsji.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Line only.
public interface LineDao {
    @Query("SELECT * FROM Line WHERE contents = :text")
    List<Line> getLinesByContents(String text);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Line> data);
}
