package com.universitedebordeaux.jumathsji;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.universitedebordeaux.jumathsji.db.AppDatabase;
import com.universitedebordeaux.jumathsji.db.Card;
import com.universitedebordeaux.jumathsji.db.CardWithLines;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseUnitTest {


    // Create a database and verify it exists
    @Test
    public void useAppContext() {

        AppDatabase db;
        Context appContext;
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.universitedebordeaux.jumathsji", appContext.getPackageName());

        db = AppDatabase.getInstance(appContext);
        assertNotEquals(null, db);
    }


    // Add a card with a known ID
    @Test
    public void addCardTest() {

        AppDatabase db;
        Context appContext;
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = AppDatabase.getInstance(appContext);
        db.clearAllTables();

        List<Card> data = new ArrayList<>();
        data.add(new Card("id1"));

        db.cardDao().insertAll(data);

        Card card = db.cardDao().getById("id1");
        assertEquals("id1", card.id);
    }

    // Add multiple cards with a known ID
    @Test
    public void addMultipleCardsTest() {
        AppDatabase db;
        Context appContext;
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = AppDatabase.getInstance(appContext);
        db.clearAllTables();

        List<Card> data = new ArrayList<>();
        data.add(new Card("id1"));
        data.add(new Card("id2"));
        data.add(new Card("id3"));
        data.add(new Card("id4"));

        db.cardDao().insertAll(data);

        List<Card> list = db.cardDao().getAll();

        assertEquals(4, list.size());
    }

    // TODO
    @Test
    public void addCardWithLinetest() {
        AppDatabase db;
        Context appContext;
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = AppDatabase.getInstance(appContext);

        List<CardWithLines> data = new ArrayList<>();

        data.add(new CardWithLines());
    }

}
