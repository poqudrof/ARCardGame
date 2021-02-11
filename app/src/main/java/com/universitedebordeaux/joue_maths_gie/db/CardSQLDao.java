package com.universitedebordeaux.joue_maths_gie.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Card only.
public interface CardSQLDao {

    @Query("SELECT * FROM Card")
    List<Card> getAll();

    @Query("SELECT * FROM Card WHERE id = :id")
    Card getById(String id);

    @Transaction
    @Query("SELECT * FROM Card WHERE id = :id")
    CardWithLines getCardWithLines(String id);
}