package com.universitedebordeaux.joue_maths_gie.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Card.class,
                parentColumns = "id",
                childColumns = "card_id",
                onDelete = ForeignKey.CASCADE)
)
// Class that matches to the Line table in the .db file.
public class Line implements Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "card_id", index = true)
    public String cardId;

    @NonNull
    @ColumnInfo
    public String line;

    public Line(@NonNull Integer id, @NonNull String cardId, @NonNull String line) {
        this.id = id;
        this.cardId = cardId;
        this.line = line;
    }

    protected Line(Parcel in) {
        id = in.readInt();
        cardId = in.readString();
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
        dest.writeString(cardId);
        dest.writeString(line);
    }
}