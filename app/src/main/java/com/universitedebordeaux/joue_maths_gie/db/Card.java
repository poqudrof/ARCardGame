package com.universitedebordeaux.joue_maths_gie.db;

import android.util.Log;

import com.universitedebordeaux.joue_maths_gie.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// https://developer.android.com/reference/org/json/JSONArray
// JSON load: https://stackoverflow.com/questions/19945411/how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listview
public class Card {

    public String card_id;
    public int id;
    public Integer number_in_role;
    public int deck;
    public String card_type;
    public int card_role;
    public String answer;
    public String tip;
    public String published_at, created_at, updated_at, updated_by;
    // useless
    public String title;
    public ArrayList<CardLine> lines;

    public static String loadJSONFromAsset(MainActivity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("jmg2.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<Card> loadCards(String cardsString){


      List<Card> cards = new ArrayList<Card>();
      try {
          // New structure Cards are in a Data object.
          JSONObject dataObject = new JSONObject(cardsString);

          JSONArray cardsArray = dataObject.getJSONArray("data");
          // new JSONArray(cardsString);
          System.out.println("Cards found: " + cardsArray.length());
          for (int i = 0; i < cardsArray.length(); i++) {
              JSONObject cardObject = cardsArray.getJSONObject(i);
              JSONObject attributesObject = cardObject.getJSONObject("attributes");
              int id = cardObject.getInt("id");
              cards.add(parseCard(id, attributesObject));
          }
      }catch(JSONException exception){
          Log.e("error", exception.getMessage());
      }
      return cards;
    }

    public static Card parseCard(int id, JSONObject cardObject){
      Card card = new Card();
      card.card_id = get(cardObject, "card_id");

      // Is ID really needed ?
      card.id = id;// Integer.parseInt(get(cardObject, "id"));
      card.number_in_role = Integer.parseInt(get(cardObject, "number_in_role"));

      // Deck is in "deck" "data" "id" .
      // try{ card.deck =  cardObject.getInt("deck"); } catch(Exception e){}
      // All from the same deck...

      card.card_type = get(cardObject, "card_type");
      try{ card.card_role =  cardObject.getInt("card_role"); } catch(Exception e){}
      card.answer = get(cardObject, "answer");
      card.tip = get(cardObject, "tip");

      try {
          JSONArray linesArray = cardObject.getJSONObject("lines").getJSONArray("data");
          card.lines = new ArrayList<CardLine>();
          for (int i = 0; i < linesArray.length(); i++) {
              JSONObject lineObject = linesArray.getJSONObject(i);
              card.lines.add(new CardLine(lineObject.getString("line"), card));
          }
      }catch(Exception e){
          System.out.println("No lines or impossible to parse lines");
      }

      System.out.println("Parsed card " + card.toString());
      // card.published_at, created_at, updated_at, updated_by;
      // useless
      // title;
        return card;
    }

    public String getText() {
        return lines.stream()
                .map(line -> line.line)
                .collect(Collectors.joining("\n"));
    }


    public String toString(){
        return "Card: " + this.card_id + " (" + this.id + "), " +
                this.card_role + " " +
                this.number_in_role + " " +
                "answer: " + this.answer + " " +
                "tip : " + this.tip +  " ";
    }

    private static String get(JSONObject cardObject, String name){
      try{
        return cardObject.getString(name);
      }catch(Exception e){
        return "";
      }
    }

}
