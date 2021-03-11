package com.universitedebordeaux.joue_maths_gie.ui;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CameraActivity.class);

        startActivity(intent);
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
        FloatingActionButton cameraButton = findViewById(R.id.card_camera_button);
        ImageButton soundButton = findViewById(R.id.sound_button);
        Button helpButton = findViewById(R.id.card_help_button);
        int color = getColorFromCardType(card.card.title);

        tvCode.setText(card.card.id);
        tvTitle.setText(card.card.title);
        tvTitle.setTextColor(color);
        tvText.setText(card.getText());
        // TODO: Image
        responseButton.setText(String.format("%s%s", responseButton.getText(),
                card.card.cardNumber.toString()));
        responseButton.setOnClickListener(this::onResponseClick);
        responseButton.setBackgroundColor(color);
        cameraButton.setOnClickListener(this::onCameraClick);
        helpButton.setText(String.format("%s%s", helpButton.getText(),
                card.card.cardNumber.toString()));
        helpButton.setBackgroundColor(color);
        if (card.card.tip == null || card.card.tip.isEmpty()) {
            helpButton.setBackgroundColor(Color.GRAY);
            helpButton.setEnabled(false);
        } else {
            helpButton.setOnClickListener(this::onHelpClick);
        }
        soundButton.setOnClickListener(this::onSoundClick);
    }

    @ColorInt
    private int getColorFromCardType(String type) {
        int color;

        Log.w(getClass().getSimpleName(), type);
        switch (type) {
            case "Espaces 3D":
                color = getColor(R.color.color_title_espaces_3D);
                break;
            case "English Maths":
                color = getColor(R.color.color_title_english_maths);
                break;
            case "Montagne de problème":
                color = getColor(R.color.color_title_montagne_de_problèmes);
                break;
            case "Plaine de 2D":
                color = getColor(R.color.color_title_plaine_de_2D);
                break;
            case "Vallée des nombres":
                color = getColor(R.color.color_title_vallée_des_nombres);
                break;
            default:
                color = getColor(R.color.cerise_red);
                break;
        }
        return color;
    }

    private void onResponseClick(View view) {
        CardWithLines card = cardsWithLines.get(0);
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.response),
                card.card.answer, this);

        alertPopupActivity.showDialog();
    }

    private void onCameraClick(View view) {
        onBackPressed();
    }

    private void onSoundClick(View view) {
        CardWithLines card = cardsWithLines.get(0);
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor afd = getAssets().openFd("sounds/" + card.card.id + ".mp3");

            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (final Exception e) {
            e.getStackTrace();
        }
    }

    private void onHelpClick(View view) {
        CardWithLines card = cardsWithLines.get(0);
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.help),
                card.card.tip, this);

        alertPopupActivity.showDialog();
    }
}