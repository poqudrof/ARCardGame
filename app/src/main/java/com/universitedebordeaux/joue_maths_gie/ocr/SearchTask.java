package com.universitedebordeaux.joue_maths_gie.ocr;

import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchTask {

    public List<CardWithLines> doInBackground(String[] lines) {
        if (lines == null || AppDatabase.db == null) {
            return null;
        }

        // Build Card Map : card id with recognition score.
        Map<String, Long> cardMap = Arrays.stream(lines)
                .map(AppDatabase.db.lineDao()::getLinesByContents)
                .flatMap(List::stream)
                .map(line -> line.cardId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String cardId = null;

        // No Card.
        if (cardMap.isEmpty()) {
            return null;
        } else if (cardMap.size() == 1) {
            // Perfect, just one card.
            cardId = cardMap.keySet().iterator().next();
        } else {
            // Multiple cards, we need to go deeper.
            // Choose the card with the best score.
            Optional<String> maybeCardId = cardMap.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);

            if (maybeCardId.isPresent()) {
                cardId = maybeCardId.get();
                // Delete it from the Card Map.
                Long s = cardMap.remove(cardId);
                // The Card Map contains the same high score, that means the OCR hasn't read everything, cancel it.
                if (cardMap.containsValue(s)) {
                    return null;
                }
            }
        }
        return Stream.of(pick(cardId)).collect(Collectors.toList());
    }

    private CardWithLines pick(String cardId) {
        return AppDatabase.db.cardDao().getCardWithLines(cardId);
    }
}