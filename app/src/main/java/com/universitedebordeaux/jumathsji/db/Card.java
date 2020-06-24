package com.universitedebordeaux.jumathsji.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
// Class (POJO) that matches to the Card table.
public class Card implements Parcelable {

    public Card(@NonNull String id) {
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo
    public String id;

    @ColumnInfo
    public String type;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String answer;

    @ColumnInfo
    public String tip;

    @ColumnInfo
    public int number;

    @ColumnInfo
    public boolean cnn;

    protected Card(Parcel in) {
        id = in.readString();
        type = in.readString();
        title = in.readString();
        answer = in.readString();
        tip = in.readString();
        number = in.readInt();
        cnn = in.readByte() != 0;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(answer);
        dest.writeString(tip);
        dest.writeInt(number);
        dest.writeByte((byte) (cnn ? 1 : 0));
    }
}
