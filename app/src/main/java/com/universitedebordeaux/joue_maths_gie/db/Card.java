package com.universitedebordeaux.joue_maths_gie.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
// Class that matches to the Card table in the .db file.
public class Card implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    public String id;

    @NonNull
    @ColumnInfo
    public String type;

    @NonNull
    @ColumnInfo
    public String title;

    @NonNull
    @ColumnInfo
    public String answer;

    @NonNull
    @ColumnInfo
    public Integer number;

    @ColumnInfo
    public String tip;

    @ColumnInfo(name = "image_path")
    public String imagePath;

    @ColumnInfo(name = "sound_path")
    public String soundPath;

    public Card(@NonNull String id, @NonNull String type, @NonNull String title,
                @NonNull String answer, @NonNull Integer number) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.answer = answer;
        this.number = number;
    }

    protected Card(Parcel in) {
        id = in.readString();
        type = in.readString();
        title = in.readString();
        answer = in.readString();
        number = in.readInt();
        tip = in.readString();
        imagePath = in.readString();
        soundPath = in.readString();
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
        dest.writeInt(number);
        dest.writeString(tip);
        dest.writeString(imagePath);
        dest.writeString(soundPath);
    }
}