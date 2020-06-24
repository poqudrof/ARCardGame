package com.universitedebordeaux.jumathsji.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
// Class (POJO) that matches to the Line table.
public class Line implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String cardId;

    @ColumnInfo
    public String contents;

    public Line(String cardId, String contents) {
        this.cardId = cardId;
        this.contents = contents;
    }

    protected Line(Parcel in) {
        id = in.readInt();
        cardId = in.readString();
        contents = in.readString();
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
        dest.writeString(contents);
    }
}
