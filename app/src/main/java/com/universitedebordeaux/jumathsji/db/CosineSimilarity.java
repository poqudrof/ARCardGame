package com.universitedebordeaux.jumathsji.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosineSimilarity {
    private static class Values {
        private int val1;
        private int val2;

        public Values(int a, int b) {
            this.val1 = a;
            this.val2 = b;
        }

        public void setVal1(int a) {
            this.val1 = a;
        }

        public void setVal2(int b) {
            this.val2 = b;
        }

        public void setValues(int a, int b) {
            setVal1(a);
            setVal2(b);
        }
    } // End Values Class

    public static double score(String text1, String text2) {
        if (text1.length() == 0 || text2.length() == 0) {
            throw new IllegalArgumentException("Sentence must be at least one word long");
        }
        String[] text1Words = text1.split(" ");
        String[] text2Words = text2.split(" ");
        Map<String, Values> wordFreqVector = new HashMap<>();
        List<String> distinctWords = new ArrayList<>();

        distinctWords(text1Words, distinctWords, wordFreqVector);
        wordFreq(text2Words, distinctWords, wordFreqVector);

        double vectAB = 0.0000000;
        double vectA = 0.0000000;
        double vectB = 0.0000000;
        for (int i = 0; i < distinctWords.size(); i++) {
            Values vals12 = wordFreqVector.get(distinctWords.get(i));
            double freq1 = vals12.val1;
            double freq2 = vals12.val2;
            vectAB = vectAB + freq1 * freq2;
            vectA = vectA + freq1 * freq1;
            vectB = vectB + freq2 * freq2;
        }

        return ((vectAB) / (Math.sqrt(vectA) * Math.sqrt(vectB)));
    }

    private static void distinctWords(String[] text1Words,
                                      List<String> distinctWords,
                                      Map<String, Values> wordFreqVector) {
        if (text1Words.length == 0) {
            throw new IllegalArgumentException("Sentence must be at least one word long");
        }
        for (String text : text1Words) {
            String word = text.trim();
            if (!word.isEmpty()) {
                if (wordFreqVector.containsKey(word)) {
                    Values vals1 = wordFreqVector.get(word);
                    int freq1 = vals1.val1 + 1;
                    int freq2 = vals1.val2;
                    vals1.setValues(freq1, freq2);
                    wordFreqVector.put(word, vals1);
                } else {
                    Values vals1 = new Values(1, 0);
                    wordFreqVector.put(word, vals1);
                    distinctWords.add(word);
                }
            }
        }
    }

    private static void wordFreq(String[] text2Words,
                                 List<String> distinctWords,
                                 Map<String, Values> wordFreqVector) {
        if (text2Words.length == 0) {
            throw new IllegalArgumentException("Sentence must be at least one word long");
        }
        for (String text : text2Words) {
            String word = text.trim();
            if (!word.isEmpty()) {
                if (wordFreqVector.containsKey(word)) {
                    Values vals1 = wordFreqVector.get(word);
                    int freq1 = vals1.val1;
                    int freq2 = vals1.val2 + 1;
                    vals1.setValues(freq1, freq2);
                    wordFreqVector.put(word, vals1);
                } else {
                    Values vals1 = new Values(0, 1);
                    wordFreqVector.put(word, vals1);
                    distinctWords.add(word);
                }
            }
        }
    }
}