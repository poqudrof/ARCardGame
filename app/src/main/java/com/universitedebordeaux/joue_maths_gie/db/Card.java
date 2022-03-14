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
            InputStream is = activity.getAssets().open("jmg.json");
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
          JSONArray cardsArray = new JSONArray(cardsString);
          for (int i = 0; i < cardsArray.length(); i++) {
              JSONObject cardObject = cardsArray.getJSONObject(i);
              cards.add(parseCard(cardObject));
          }
      }catch(JSONException exception){
          Log.e("error", exception.getMessage());
      }
      return cards;
    }

    public static Card parseCard(JSONObject cardObject){
      Card card = new Card();
      card.card_id = get(cardObject, "card_id");
      card.id = Integer.parseInt(get(cardObject, "id"));
      card.number_in_role = Integer.parseInt(get(cardObject, "number_in_role"));

      try{ card.deck =  cardObject.getInt("deck"); } catch(Exception e){}
      card.card_type = get(cardObject, "card_type");
      try{ card.card_role =  cardObject.getInt("card_role"); } catch(Exception e){}
      card.answer = get(cardObject, "answer");
      card.tip = get(cardObject, "tip");

      try {
          JSONArray linesArray = cardObject.getJSONArray("lines");
          card.lines = new ArrayList<CardLine>();
          for (int i = 0; i < linesArray.length(); i++) {
              JSONObject lineObject = linesArray.getJSONObject(i);
              card.lines.add(new CardLine(lineObject.getString("line"), card));
          }
      }catch(Exception e){}

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
