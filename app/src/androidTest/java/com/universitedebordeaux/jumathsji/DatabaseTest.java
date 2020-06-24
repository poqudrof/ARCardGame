package com.universitedebordeaux.jumathsji;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.universitedebordeaux.jumathsji.db.AppDatabase;
import com.universitedebordeaux.jumathsji.db.Card;
import com.universitedebordeaux.jumathsji.db.CardConstructor;
import com.universitedebordeaux.jumathsji.db.CardYaml;
import com.universitedebordeaux.jumathsji.download.UpdateDBTask;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    // Loads the assets/cards folder and checks the uniqueness of their id
    public void uniqueIdTest() {
        Log.d("uniqueIdTest", "Starting ID uniqueness test");

        Context appContext;
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.universitedebordeaux.jumathsji", appContext.getPackageName());

        String uri = appContext.getResources().getString(R.string.database_url);
        UpdateDBTask updater = new UpdateDBTask(appContext, mf -> {
            String[] names = new File(appContext.getExternalFilesDir(mf.name), "data").list();
            assertNotEquals(null, names);
        });

        updater.execute(uri);

        AppDatabase db = AppDatabase.getInstance(appContext);
        List<Card> list = db.cardDao().getAll();

        String[] idList = new String[list.size()];
        int count = 0;

        for (Card card : list) {
            String id = card.id;

            for (int j = 0; j < count; j++) {
                assertNotEquals(id, idList[j]);
            }

            idList[count++] = id;
        }

        Log.d("uniqueIdTest", "Database checked. No identical Ids found");

    }

}
