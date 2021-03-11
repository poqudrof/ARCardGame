package com.universitedebordeaux.joue_maths_gie.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Card only.
public interface CardSQLDao {

    // Select all cards in the database.
    @Transaction
    @Query("SELECT * FROM Card WHERE id = :id")
    CardWithLines getCardWithLines(String id);
}