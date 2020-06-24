package com.universitedebordeaux.jumathsji.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class cosineSimilarityTest {
    private cosineSimilarity cosineSimilarity;

    String a = "Lorem ipsum dolor sit amet."; // 5 words Lorem Ipsum
    String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent eu."; // 10 words Lorem Ipsum
    String c = "Lorem ipsum dolor amet sit."; // 5 words Lorem Ipsum w/ 2 last words inverted
    String d = ""; // Void text
    String e = "Lorme ipsum dolor sit amet, consectetur adipiscing elit. Praesent eu."; //same as b, with 2 inverted letters


    @Before
    public void setUp() throws Exception {
        cosineSimilarity = new cosineSimilarity();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void score_equals() {
        assertEquals(1, cosineSimilarity.score(a, a), 0.001);
    }

    @Test
    public void score_diff() {
        assertNotEquals(1, cosineSimilarity.score(a, b), 0.001);
    }

    @Test
    public void score_sim() {
        assertNotEquals(1, cosineSimilarity.score(a, c), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void score_empty() {
        cosineSimilarity.score(a, d);
    }

    @Test
    public void tmpDBTest1() {
        testDB(a, b, c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDBEmpty() {
        testDB(a, b, d);
    }

    private void testDB(String a, String b, String c) {
        List<String> db = Arrays.asList(a, b, c);
        Map<String, String> simCards = new HashMap<>();
        int cmp;
        double cosSimValue = 0.0;
        double delta = 0.001;
        for (int i = 0; i < db.size(); i++) {
            cmp = i + 1;
            while (cmp < db.size()) {
                cosSimValue = cosineSimilarity.score(db.get(i), db.get(cmp));
                if (cosSimValue + delta >= 1.0 || cosSimValue - delta >= 1.0) {
                    simCards.put(db.get(i), db.get(cmp));
                }
                cmp++;
            }
        }
        if (simCards.size() != 0) {
            for (String i : simCards.keySet()) {
                System.out.println("Similar Cards : {" + i + "}, {" + simCards.get(i) + "}");
            }
        } else {
            System.out.println("No similar cards");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void score_spaceV1() {
        cosineSimilarity.score(a, " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void score_spaceV2() {
        cosineSimilarity.score(" ", a);
    }

    @Test
    public void tmpTestDB2() {
        testDB(a, a, a);
    }

    @Test
    public void tmpTestDB3() {
        testDB(a, b, e);
    }
}