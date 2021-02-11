package com.universitedebordeaux.joue_maths_gie.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;

// Singleton class to manage the SQLite database, cannot be called in the graphics thread.
@Database(entities = {Card.class, Line.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "joue_maths_gie.db";

    // DAO Card getter.
    public abstract CardSQLDao cardDao();

    // DAO Line getter.
    public abstract LineSQLDao lineDao();

    // Import the new database
    public static AppDatabase create(Context context, String Filename) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                   .createFromAsset(Filename)
                   .build();
    }
}