package com.universitedebordeaux.joue_maths_gie.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
// Describe your SQL queries here, they will be automatically implemented.
// DAO Card only.
public interface CardSQLDao {

    // Select all cards in the database.
    @Transaction
    @Query("SELECT * FROM cards WHERE card_id = :card_id")
    CardWithLines getCardWithLines(String card_id);


    // NO USAGE
    // Select a cards in the database.
    @Transaction
    @Query("SELECT * FROM cards WHERE card_id = :card_id")
    Cards getCard(String card_id);

    // SINGLE USAGE IN "MANUAL" SEARCH.
    // Select all cards in the database.
    @Transaction
    @Query("SELECT * FROM cards WHERE id = :id")
    CardWithLines getCardFromIdWithLines(int id);

    // SINGLE USAGE IN DEBUG
    @Query("SELECT * FROM cards")
    List<Cards> getAll();
}