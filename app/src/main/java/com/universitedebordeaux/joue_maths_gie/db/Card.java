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
    // Example : "e3dcm1q_1"
    public String id;

    @NonNull
    @ColumnInfo
    // Example : "question"
    public String type;

    @NonNull
    @ColumnInfo
    // Example : "Espaces 3D"
    public String title;

    @NonNull
    @ColumnInfo
    // Example : "La réponse est 30."
    public String answer;

    @NonNull
    @ColumnInfo(name = "card_number")
    // Example : 1
    public Integer cardNumber;

    @ColumnInfo
    // Example : "Ceci est un message d'aide."
    public String tip;

    @ColumnInfo(name = "image_path")
    // Example : "image/image.png"
    public String imagePath;

    @ColumnInfo(name = "sound_path")
    // Example : "sound/sound.mp4"
    public String soundPath;

    // Card constructor.
    public Card(@NonNull String id, @NonNull String type, @NonNull String title,
                @NonNull String answer, @NonNull Integer cardNumber) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.answer = answer;
        this.cardNumber = cardNumber;
    }

    protected Card(Parcel in) {
        id = in.readString();
        type = in.readString();
        title = in.readString();
        answer = in.readString();
        cardNumber = in.readInt();
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
        dest.writeInt(cardNumber);
        dest.writeString(tip);
        dest.writeString(imagePath);
        dest.writeString(soundPath);
    }
}