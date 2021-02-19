package com.universitedebordeaux.joue_maths_gie.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import java.util.List;

public class CardActivity extends AppCompatActivity {

    private List<CardWithLines> cardsWithLines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillCards();
    }

    private void fillCards()
    {
        Bundle bundle = getIntent().getExtras();

        cardsWithLines = bundle.getParcelableArrayList(CameraActivity.cardsList);
    }
}
