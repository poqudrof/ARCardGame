package com.universitedebordeaux.joue_maths_gie.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;
import com.universitedebordeaux.joue_maths_gie.ocr.SurfaceHolderCallback;
import com.universitedebordeaux.joue_maths_gie.ocr.TextAnalyzer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Recognition and preview activity.
public class CameraActivity extends AppCompatActivity {

    public static final String cardsList = "CARDS_LIST";
    public static final int requestCameraPermissionID = 1001;

    private ActionPopupActivity popupActivity;
    private SurfaceView cameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        cameraView = findViewById(R.id.camera_view);
        popupActivity = new ActionPopupActivity(getString(R.string.code_title), getString(R.string.code_hint),
                this, this::onCodeResult);
        startCamera();
        setData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == requestCameraPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            }
            else {
                onBackPressed();
            }
        }
    }

    private void startCamera() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(CameraActivity.class.getSimpleName(), "Detector dependencies are not yet available.");
        } else {
            CameraSource cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolderCallback(this, cameraView, cameraSource));
            textRecognizer.setProcessor(new TextAnalyzer(this));
        }
    }

    private void setData() {
        FloatingActionButton returnTopButton = findViewById(R.id.return_button);
        Button codeButton = findViewById(R.id.code_button);

        returnTopButton.setOnClickListener(this::onReturnButtonClick);
        codeButton.setOnClickListener(this::onCodeButtonClick);
    }

    private void onCodeButtonClick(View view) {
        popupActivity.showDialog();
    }

    private void onReturnButtonClick(View view) {
        onBackPressed();
    }

    private void onCodeResult(@NotNull String editTextResult) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        popupActivity.dismiss();
        if (editTextResult.isEmpty()) {
            return;
        }
        executorService.execute(() -> {
            CardWithLines card = AppDatabase.db.cardDao().getCardWithLines(editTextResult);
            List<CardWithLines> cards = new ArrayList<>();

            runOnUiThread(() -> {
                if (card == null) {
                    Toast.makeText(this, R.string.card_not_found, Toast.LENGTH_SHORT).show();
                } else {
                    cards.add(card);
                    doOnResult(cards);
                }
            });
        });
    }

    // Send and send the recognition result to the card activity.
    public void doOnResult(List<CardWithLines> cardsWithLines) {
        Intent intent = new Intent(this, CardActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(cardsList, CardWithLines.toParcelableList(cardsWithLines));

        intent.putExtras(bundle);
        finish();
        startActivity(intent);
    }
}