package com.universitedebordeaux.joue_maths_gie.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CardDeck {

    // TODO Move the main deck to application ?
    static HashMap<String, Card> cardIds;
    private static Set<Card> selectedCards;
    private static List<CardLine> lines;

    public static void createMainDeck(List<Card> cards){
        cardIds = new HashMap<String, Card>();
        lines = new ArrayList<>();

        for(Card card : cards){
            cardIds.put(card.card_id, card);
        }

        for(Card card : cards){
            for(CardLine line: card.lines){
                lines.add(line);
            }
        }
    }

    public static List<Card> matchText(String text){

      ArrayList<Card> matches = new ArrayList<Card>();
      for(CardLine cardLine : lines){
        if(cardLine.line.equalsIgnoreCase(text)){
          matches.add(cardLine.card);
        } 
      }
      return matches;      
    }

    public static Card findCardByID(String id){
        return cardIds.get(id);
    }

    public static Set<Card> selectedCards() {
        return selectedCards;
    }
    // Define set data method.
    public static void selectCards(Set<Card> cards) {
        selectedCards = cards;
    }
}
