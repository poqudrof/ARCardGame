package com.universitedebordeaux.jumathsji.db;

import java.util.List;
import java.util.stream.Collectors;

// Class pure POJO to match the YAML format to perfection.
public class CardYaml {
    public String id;

    public String type;

    public String title;

    public List<String> text;

    public String answer;

    public String tip;

    public int number;

    public boolean cnn;

    // Extracts and transforms CardYaml into a card.
    public Card toCard() {
        Card card = new Card(id);

        card.tip = tip;
        card.type = type;
        card.title = title;
        card.answer = answer;
        card.number = number;
        card.cnn = cnn;

        return card;
    }

    // Extracts and transforms text into lines.
    public List<Line> toLines() {
        return text.stream().map(contents -> new Line(id, contents)).collect(Collectors.toList());
    }

    // Static function to do it on multiple documents.
    public static List<Card> getCards(List<CardYaml> docs) {
        return docs.stream().map(CardYaml::toCard).collect(Collectors.toList());
    }

    public static List<Line> getLines(List<CardYaml> docs) {
        return docs.stream().map(CardYaml::toLines).flatMap(List::stream).collect(Collectors.toList());
    }
}
