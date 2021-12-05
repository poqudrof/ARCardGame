package com.universitedebordeaux.joue_maths_gie.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
// Class that matches to the Card table in the .db file.
public class Cards implements Parcelable {

    @ColumnInfo
    // Example : "e3dcm1q_1"
    public String card_id;

    @PrimaryKey
    // Example : "1"
    public int id;

    // Example : 1
    public Integer number_in_role;

    @ColumnInfo
    public int deck;

    @ColumnInfo
    // Example : "question"
    public String card_type;

    @ColumnInfo
    // Example : 1
    public int card_role;

    @ColumnInfo
    // Example : "La réponse est 30."
    public String answer;

    @ColumnInfo
    // Example : "Ceci est un message d'aide."
    public String tip;

    // These are not used, but in the database
    public String published_at, created_at, updated_at, updated_by;
    // useless
    public String title;

// Card constructor.
    public Cards(@NonNull int id, @NonNull String card_id, @NonNull String card_type, @NonNull int card_role,
                 @NonNull String answer, @NonNull Integer number_in_role) {
        this.id = id;
        this.card_id = card_id;
        this.card_type = card_type;
        this.card_role = card_role;
        this.answer = answer;
        this.number_in_role = number_in_role;
    }

    protected Cards(Parcel in) {
        id = in.readInt(); 
        card_id = in.readString();
        card_type = in.readString();
        card_role = in.readInt();
        answer = in.readString();
        number_in_role = in.readInt();
        tip = in.readString();
    }

    public static final Creator<Cards> CREATOR = new Creator<Cards>() {
        @Override
        public Cards createFromParcel(Parcel in) {
            return new Cards(in);
        }

        @Override
        public Cards[] newArray(int size) {
            return new Cards[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    // TODO: Used?
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(card_id);
        dest.writeString(card_type);
        dest.writeInt(card_role);
        dest.writeString(answer);
        dest.writeInt(number_in_role);
        dest.writeString(tip);
    }
}