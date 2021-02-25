package com.universitedebordeaux.joue_maths_gie.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import java.util.List;

public class CardActivity extends AppCompatActivity {

    private List<CardWithLines> cardsWithLines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);

        fillCardsList();
        displayCard();
    }

    private void fillCardsList()
    {
        Bundle bundle = getIntent().getExtras();

        cardsWithLines = bundle.getParcelableArrayList(CameraActivity.cardsList);
    }

    private void displayCard()
    {
        CardWithLines card = cardsWithLines.get(0);
        TextView tvCode = findViewById(R.id.card_code);
        TextView tvTitle = findViewById(R.id.card_title);
        TextView tvText = findViewById(R.id.card_content);

        tvCode.setText(card.card.id);
        tvTitle.setText(card.card.title);
        tvText.setText(card.getText());
        // Image
        // Sound
    }
}
