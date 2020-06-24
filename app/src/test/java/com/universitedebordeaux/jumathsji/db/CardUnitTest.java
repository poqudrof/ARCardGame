package com.universitedebordeaux.jumathsji.db;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CardUnitTest {

    @Test
    // Load a card and check if its fields are filled correctly
    public void loadCard() {
        System.out.println("Starting the test : Load a card");

        try {
            Yaml yaml = new Yaml(new CardConstructor());
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/tests/card.yaml");
            assertNotEquals(null, is);

            List<CardYaml> card = yaml.load(is);

            assertNotEquals(null, card);
            assertEquals(1, card.size());

            assertEquals("e3dcm1q_1", card.get(0).id);
            is.close();
        } catch (IOException e) {

        }
        System.out.println("Card successfully loaded.");

    }

    @Test
    // Loads multiple cards in a same file
    public void loadCards() {
        System.out.println("Starting the test : Load two cards");

        try {
            Yaml yaml = new Yaml(new CardConstructor());
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/tests/twocards.yaml");
            assertNotEquals(null, is);
            List<CardYaml> db = yaml.load(is);

            for (CardYaml card : db) {
                assertEquals("question", card.type);
            }

            is.close();
        } catch (IOException e) {

        }
        System.out.println("Cards successfully loaded.");
    }
}