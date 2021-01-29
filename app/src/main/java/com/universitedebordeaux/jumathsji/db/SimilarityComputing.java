package com.universitedebordeaux.jumathsji.db;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityComputing {

    public static double naiveCompute(String txt1, String txt2) {
        if (txt1.length() == 0 || txt2.length() == 0) {
            throw new IllegalArgumentException("Sentence(s) must be at least one word long");
        }

        String[] text1 = toArray(txt1);
        String[] text2 = toArray(txt2);
        double l_min = Math.min(text1.length, text2.length);
        double l_max = Math.max(text1.length, text2.length);
        double cpt = 0;
        double result = 0;

        for (int i = 0; i < l_min; i++) {
            if (text1[i].equals(text2[i])) {
                cpt += 1;
            }
        }
        if (cpt != 0) {
            result = (cpt / l_max) * 100;
        }
        return result;
    }

    public static double naiveComputeString(List<String> a, List<String> b) {
        if (a.size() == 0 || b.size() == 0) {
            throw new IllegalArgumentException("Sentence(s) must be at least one word long");
        }

        String text1 = TextUtils.join(" ", a);
        String text2 = TextUtils.join(" ", b);

        return naiveCompute(text1, text2);
    }

    public static int testDB(String a, String b, String c) {
        List<String> db = Arrays.asList(a.toLowerCase(), b.toLowerCase(), c.toLowerCase());
        Map<String, String> simCards = new HashMap<>();
        int cmp;
        double cosSimValue;
        int result = 0;

        for (int i = 0; i < db.size(); ++i) {
            cmp = i + 1;
            while (cmp < db.size()) {
                cosSimValue = CosineSimilarity.score(db.get(i), db.get(cmp));
                if (cosSimValue >= 0.9) {
                    simCards.put(db.get(i), db.get(cmp));
                }
                cmp++;
            }
        }
        if (simCards.size() != 0)
            result = simCards.size() + 1;
        return result;
    }

    private static String[] toArray(String txt) {
        return txt.split(" ");
    }

    public static void similarCards(CardYaml a, CardYaml b, Map<CardYaml, CardYaml> result, double limit) {
        List<String> lText1 = a.text;
        List<String> lText2 = b.text;
        double similarity = naiveComputeString(lText1, lText2);

        if (similarity >= limit) {
            result.put(a, b);
        }
    }
}