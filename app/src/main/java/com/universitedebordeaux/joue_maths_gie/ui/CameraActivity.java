package com.universitedebordeaux.joue_maths_gie.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;
import com.universitedebordeaux.joue_maths_gie.ocr.SurfaceHolderCallback;
import com.universitedebordeaux.joue_maths_gie.ocr.TextAnalyzer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// Recognition and preview activity.
public class CameraActivity extends AppCompatActivity {

    public static final String cardsList = "CARDS_LIST";
    public static final int requestCameraPermissionID = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        // startCamera();
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

    private void startCamera()
    {
        SurfaceView cameraView = findViewById(R.id.camera_view);
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

    // Send and send the recognition result to the card activity.
    public void doOnResult(List<CardWithLines> cardsWithLines) {
        Intent intent = new Intent(this, CardActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(cardsList, CardWithLines.toParcelableList(cardsWithLines));

        intent.putExtras(bundle);
        startActivity(intent);
    }
}