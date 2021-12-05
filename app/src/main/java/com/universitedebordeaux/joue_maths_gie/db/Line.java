package com.universitedebordeaux.joue_maths_gie.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Cards.class,
                parentColumns = "id",
                childColumns = "card",
                onDelete = ForeignKey.CASCADE)
)
// Class that matches to the Line table in the .db file.
public class Line implements Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "card", index = true)
    // Linked to the associated card.
    // Example : "2"
    public int card;

    @NonNull
    @ColumnInfo
    // Example : "Combien y-a-t-il de cubes"
    public String line;

    public Line(@NonNull Integer id, @NonNull int card, @NonNull String line) {
        this.id = id;
        this.card = card;
        this.line = line;
    }

    protected Line(Parcel in) {
        id = in.readInt();
        card = in.readInt();
        line = in.readString();
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(card);
        dest.writeString(line);
    }
}