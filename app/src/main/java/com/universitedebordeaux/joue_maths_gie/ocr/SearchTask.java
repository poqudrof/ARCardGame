package com.universitedebordeaux.joue_maths_gie.ocr;

import android.util.Log;

import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;
import com.universitedebordeaux.joue_maths_gie.db.Card;
import com.universitedebordeaux.joue_maths_gie.db.CardDeck;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Compare words received and card in database.
public class SearchTask {

    public Set<Card> doInBackground(String[] lines) {

        if (lines == null) {
            return null;
        }

        Set<Card> cards = new HashSet<Card>();
        HashMap<Card, Integer> scores = new HashMap<>();

        Log.v("MATCHER", "matching...");

        // TODO: Scores & approximate match.
        for(String line : lines){
          List<Card> found = CardDeck.matchText(line);
          for(Card card : found){
            Integer v = scores.getOrDefault(card, (Integer) 0);
            scores.put(card, (Integer) (v + 1));
          }
        }

        // No card found. 
        if(scores.size() == 0){
          Log.v("MATCHER", "no card found");
          return null;
        }

        // Sort the matches 
        LinkedHashMap<Card, Integer> sortedCards = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        // Sort & print the matches
         scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);

        Card bestMatch = (Card) sortedCards.keySet().toArray()[0];
        Log.v("MATCHER", "best match " + bestMatch.toString());

        return sortedCards.keySet();

        // // Build Card Map : card id with recognition score.
        // Map<Integer, Long> cardMap = Arrays.stream(lines)
        //         .map(AppDatabase.db.lineDao()::getLinesByContents)
        //         .flatMap(List::stream)
        //         .map(line -> line.card)
        //         .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Integer cardId = null;

        // // No Card.
        // if (cardMap.isEmpty()) {
        //     return null;
        // } else if (cardMap.size() == 1) {
        //     // Perfect, just one card.
        //     cardId = cardMap.keySet().iterator().next();
        // } else {
        //     // Multiple cards, we need to go deeper.
        //     // Choose the card with the best score.
        //     Optional<Integer> maybeCardId = cardMap.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);

        //     if (maybeCardId.isPresent()) {
        //         cardId = maybeCardId.get();
        //         // Delete it from the Card Map.
        //         Long s = cardMap.remove(cardId);
        //         // The Card Map contains the same high score, that means the OCR hasn't read everything, cancel it.
        //         if (cardMap.containsValue(s)) {
        //             return null;
        //         }
        //     }
        // }
        // return Stream.of(pick(cardId)).collect(Collectors.toList());
        
    }

    private CardWithLines pick(Integer cardId) {
        return AppDatabase.db.cardDao().getCardFromIdWithLines(cardId);
    }
}