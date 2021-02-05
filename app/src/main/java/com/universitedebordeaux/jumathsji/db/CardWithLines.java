package com.universitedebordeaux.jumathsji.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Matches to a card with its text, in other words its Line list.
public class CardWithLines implements Parcelable {
    @Embedded
    public Card card;

    @Relation(parentColumn = "id", entityColumn = "card_id")
    public List<Line> lines;

    public String getText() {
        return lines.stream().map(line -> line.line).collect(Collectors.joining("\n"));
    }

    public CardWithLines() {
    }

    protected CardWithLines(Parcel in) {
        card = in.readParcelable(Card.class.getClassLoader());
        lines = in.createTypedArrayList(Line.CREATOR);
    }

    public static final Creator<CardWithLines> CREATOR = new Creator<CardWithLines>() {
        @Override
        public CardWithLines createFromParcel(Parcel in) {
            return new CardWithLines(in);
        }

        @Override
        public CardWithLines[] newArray(int size) {
            return new CardWithLines[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(card, flags);
        dest.writeTypedList(lines);
    }

    public static ArrayList<? extends Parcelable> toParcelableList(List<CardWithLines> cardWithLinesList) {
        return (ArrayList<? extends Parcelable>) cardWithLinesList;
    }
}