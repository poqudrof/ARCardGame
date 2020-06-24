package com.universitedebordeaux.jumathsji.ocr;

import android.os.AsyncTask;

import com.universitedebordeaux.jumathsji.db.AppDatabase;
import com.universitedebordeaux.jumathsji.db.CardWithLines;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// In charge of recognition from an Ocr result.
// Uses the database and sometimes the CNN.
public class SearchTask extends AsyncTask<String, Void, List<CardWithLines>> {
    private WeakReference<TextRecognitionActivity> mRefActivity;
    private WeakReference<TfInterpreter> mRefInterpreter;
    private AppDatabase mDB;

    public SearchTask(TextRecognitionActivity activity, TfInterpreter interpreter) {
        mRefActivity = new WeakReference<>(activity);
        mRefInterpreter = new WeakReference<>(interpreter);
        mDB = AppDatabase.getInstance(activity.getApplicationContext());
    }

    @Override
    protected List<CardWithLines> doInBackground(String... texts) {
        if (texts == null || mDB == null) return null;

        // Build Card Map : card id with recognition score.
        Map<String, Long> cardMap = Arrays.stream(texts)
                .map(mDB.lineDao()::getLinesByContents)
                .flatMap(List::stream)
                .map(line -> line.cardId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String cardId;
        // No Card.
        if (cardMap.isEmpty()) return null;

            // Perfect, just one card.
        else if (cardMap.size() == 1) cardId = cardMap.keySet().iterator().next();

            // Multiple cards, we need to go deeper.
        else {
            // Check if we need the CNN.
            if (cardMap.entrySet().stream().anyMatch(e -> mDB.cardDao().getById(e.getKey()).cnn)) {
                cardId = mRefInterpreter.get().predict();
                // CNN returned a result that contradicts the OCR, result of CNN inconclusive
                // Return bests results of OCR
                if (!cardMap.containsKey(cardId)) {
                    return cardMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).map(this::pick).collect(Collectors.toList());
                }
            }
            // No need, the OCR recognition score should be sufficient (I hope).
            else {
                // Choose the card with the best score.
                cardId = cardMap.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).get();
                // Delete it from the Card Map.
                Long s = cardMap.remove(cardId);
                // The Card Map contains the same high score, that means the OCR hasn't read everything, cancel it.
                if (cardMap.containsValue(s)) return null;
            }
        }

        return Stream.of(pick(cardId)).collect(Collectors.toList());
    }

    // Shortcut getter for the lambda.
    private CardWithLines pick(String cardId) {
        return mDB.cardDao().getCardWithLines(cardId);
    }

    @Override
    protected void onPostExecute(List<CardWithLines> cards) {
        TextRecognitionActivity activity = mRefActivity.get();
        if (cards != null && activity != null) {
            activity.sendResult(cards);
        }
    }
}
