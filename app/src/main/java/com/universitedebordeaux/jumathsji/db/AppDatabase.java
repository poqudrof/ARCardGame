package com.universitedebordeaux.jumathsji.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;

// Singleton class to manage the SQLite database, cannot be called in the graphics thread.
@Database(entities = {Card.class, Line.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "jumathsji";
    private static volatile AppDatabase instance;

    AppDatabase() {
    }

    // DAO Card getter.
    public abstract CardDao cardDao();

    // DAO Line getter.
    public abstract LineDao lineDao();

    // Create/Open the database with Room builder.
    // Room always needs a context to create a SQLite database.
    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    // Reloads the database from a folder containing YAML files in the correct format.
    /*
    public static void reload(Context context, File dir) {
        if (dir == null || dir.list().length == 0) return;

        AppDatabase db = getInstance(context);

        // Drop tables
        db.clearAllTables();

        Log.d("DB Loader", dir.getPath());

        Yaml yaml = new Yaml(new CardConstructor());
        for (String file : dir.list((d, name) -> name.toLowerCase().endsWith(".yaml"))) {
            List<CardYaml> cardYamlList;
            try {
                cardYamlList = yaml.load(new FileInputStream(new File(dir, file)));

                db.cardDao().insertAll(CardYaml.getCards(cardYamlList));
                db.lineDao().insertAll(CardYaml.getLines(cardYamlList));
            } catch (FileNotFoundException e) {
                Log.e("DB Loader", "File not found : " + file);
            } catch (Exception e) {
                Log.e("DB Loader", "File card malformed : " + file);
            }
        }
    }
    */

    public static void reload(Context context)
    {
        AppDatabase db = getInstance(context);
        Yaml yaml;
        final String[] rawFiles = {"e3dcm1", "emcm2"};

        db.clearAllTables();

        Log.d("DB Loader", "res/raw");

        yaml = new Yaml(new CardConstructor());
        for (final String file : rawFiles) {
            List<CardYaml> cardYamlList;
            InputStream inputStream = context.getResources().openRawResource(context.getResources().
                    getIdentifier(file, "raw", context.getPackageName()));

            try {
                cardYamlList = yaml.load(inputStream);

                db.cardDao().insertAll(CardYaml.getCards(cardYamlList));
                db.lineDao().insertAll(CardYaml.getLines(cardYamlList));
            } catch (final Exception e) {
                Log.e("DB Loader", "File card malformed : " + file);
                e.printStackTrace();
            }
        }
    }

    // Main getter.
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
}