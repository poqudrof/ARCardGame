package com.universitedebordeaux.joue_maths_gie.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.universitedebordeaux.joue_maths_gie.R;

// Singleton class to manage the SQLite database, cannot be called in the graphics thread.
@Database(entities = {Card.class, Line.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase db = null;

    // DAO Card getter.
    public abstract CardSQLDao cardDao();

    // DAO Line getter.
    public abstract LineSQLDao lineDao();

    // Import the database from the local db class stored in the assets folder.
    // TODO: problem with a HARD CODED DATABASE NAME
    // The loader should handle this.
    public static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.db_name))
                   .createFromAsset(context.getString(R.string.db_name))
                   .build();
    }

    // Delete the database.
    // N.B : All database is permanently stored inside the phone. If you decide to change the tables for
    // example, you will have annoying SQL errors.
    // Delete it each time the application stopped (without crashing).
    public static void removeDatabase(Context context) {
        context.deleteDatabase(context.getString(R.string.db_name));
    }
}