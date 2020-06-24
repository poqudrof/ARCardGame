package com.universitedebordeaux.jumathsji.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Card only.
public interface CardDao {
    @Query("SELECT * FROM Card")
    List<Card> getAll();

    @Query("SELECT * FROM Card WHERE id = :id")
    Card getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Card> data);

    @Transaction
    @Query("SELECT * FROM Card WHERE id = :id")
    CardWithLines getCardWithLines(String id);
}
