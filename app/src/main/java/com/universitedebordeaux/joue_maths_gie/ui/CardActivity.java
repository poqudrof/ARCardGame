package com.universitedebordeaux.joue_maths_gie.ui;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class CardActivity extends AppCompatActivity {

    private CardWithLines card;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);

        getCard();
        setData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CameraActivity.class);

        finish();
        startActivity(intent);
    }

    private void getCard() {
        Bundle bundle = getIntent().getExtras();
        List<CardWithLines> cardWithLines = bundle.getParcelableArrayList(CameraActivity.cardsList);

        card = cardWithLines.get(0);
    }

    private void setData() {
        FloatingActionButton cameraButton = findViewById(R.id.card_camera_button);
        int color = getColorFromCardType(card.card.title);

        mediaPlayer = new MediaPlayer();
        cameraButton.setOnClickListener(this::onCameraClick);
        setCardTexts(color);
        setCardImage();
        setCardButtons(color);
    }

    private void setCardTexts(@ColorInt int color) {
        TextView tvCode = findViewById(R.id.card_code);
        TextView tvTitle = findViewById(R.id.card_title);
        TextView tvText = findViewById(R.id.card_content);

        tvCode.setText(card.card.id);
        tvTitle.setText(card.card.title);
        tvTitle.setTextColor(color);
        tvText.setText(card.getText());
    }

    private void setCardImage() {
        ImageView imageView = findViewById(R.id.card_image);

        try {
            InputStream inputStream = getAssets().open("images/" + card.card.id + ".png");
            Drawable drawable = Drawable.createFromStream(inputStream, null);

            imageView.setImageDrawable(drawable);
        } catch (final IOException e) {
            imageView.setVisibility(View.GONE);
        }
    }

    private void setCardButtons(@ColorInt int color) {
        Button responseButton = findViewById(R.id.response_button);
        ImageButton soundButton = findViewById(R.id.sound_button);
        Button helpButton = findViewById(R.id.card_help_button);

        responseButton.setText(String.format("%s%s", responseButton.getText(),
                card.card.cardNumber.toString()));
        responseButton.setOnClickListener(this::onResponseClick);
        responseButton.setBackgroundColor(color);
        helpButton.setText(String.format("%s%s", helpButton.getText(),
                card.card.cardNumber.toString()));
        helpButton.setBackgroundColor(color);

        if (card.card.tip == null || card.card.tip.isEmpty()) {
            helpButton.setBackgroundColor(Color.GRAY);
            helpButton.setEnabled(false);
        } else {
            helpButton.setOnClickListener(this::onHelpClick);
        }

        try {
            AssetFileDescriptor afd = getAssets().openFd("sounds/" + card.card.id + ".mp3");

            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            soundButton.setOnClickListener(this::onSoundClick);
        } catch (final IOException e) {
            soundButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            soundButton.setEnabled(false);
        }
    }

    @ColorInt
    private int getColorFromCardType(String type) {
        HashMap<String, Integer> map = loadMap();
        Integer mapColor;
        int color;

        Log.w(getClass().getSimpleName(), "Current type: " + type);
        mapColor = map.getOrDefault(type, getColor(R.color.cerise_red));

        if (mapColor == null) {
            color = getColor(R.color.cerise_red);
        } else {
            color = mapColor;
        }
        return color;
    }

    private HashMap<String, Integer> loadMap() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put(getString(R.string.espaces_3d), getColor(R.color.color_title_espaces_3D));
        map.put(getString(R.string.vallee_des_nombres), getColor(R.color.color_title_vallée_des_nombres));
        map.put(getString(R.string.montagne_de_problemes), getColor(R.color.color_title_montagne_de_problèmes));
        map.put(getString(R.string.english_maths), getColor(R.color.color_title_english_maths));
        map.put(getString(R.string.plaine_2d), getColor(R.color.color_title_plaine_de_2D));
        return map;
    }

    private void onResponseClick(View view) {
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.response),
                card.card.answer, this);

        alertPopupActivity.showDialog();
    }

    private void onCameraClick(View view) {
        onBackPressed();
    }

    private void onSoundClick(View view) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void onHelpClick(View view) {
        AlertPopupActivity alertPopupActivity = new AlertPopupActivity(getString(R.string.help),
                card.card.tip, this);

        alertPopupActivity.showDialog();
    }
}