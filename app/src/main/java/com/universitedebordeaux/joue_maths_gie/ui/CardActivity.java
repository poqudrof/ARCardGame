package com.universitedebordeaux.joue_maths_gie.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
        setData();
    }

    private void fillCardsList() {
        Bundle bundle = getIntent().getExtras();

        cardsWithLines = bundle.getParcelableArrayList(CameraActivity.cardsList);
    }

    private void setData() {
        CardWithLines card = cardsWithLines.get(0);
        TextView tvCode = findViewById(R.id.alert_popup_text);
        TextView tvTitle = findViewById(R.id.card_title);
        TextView tvText = findViewById(R.id.card_content);
        Button responseButton = findViewById(R.id.response_button);
        ImageButton cameraButton = findViewById(R.id.camera_button);
        ImageButton soundButton = findViewById(R.id.sound_button);
        Button helpButton = findViewById(R.id.card_help_button);

        tvCode.setText(card.card.id);
        tvTitle.setText(card.card.title);
        tvText.setText(card.getText());
        // TODO: Image
        responseButton.setText(responseButton.getText() + card.card.number.toString());
        responseButton.setOnClickListener(this::onResponseClick);
        cameraButton.setOnClickListener(this::onCameraClick);
        helpButton.setText(helpButton.getText() + card.card.number.toString());
        if (card.card.tip == null || card.card.tip.isEmpty()) {
            helpButton.setBackgroundColor(Color.GRAY);
            helpButton.setEnabled(false);
        } else {
            helpButton.setOnClickListener(this::onHelpClick);
        }
        soundButton.setOnClickListener(this::onSoundClick);
    }

    private void onResponseClick(View view) {
        CardWithLines card = cardsWithLines.get(0);
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.response),
                card.card.answer, this);

        alertPopupActivity.showDialog();
    }

    private void onCameraClick(View view) {
        Intent intent = new Intent(this, CameraActivity.class);

        finish();
        startActivity(intent);
    }

    private void onSoundClick(View view) {
        // TODO: Sound
    }

    private void onHelpClick(View view) {
        CardWithLines card = cardsWithLines.get(0);
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.help),
                card.card.tip, this);

        alertPopupActivity.showDialog();
    }
}
