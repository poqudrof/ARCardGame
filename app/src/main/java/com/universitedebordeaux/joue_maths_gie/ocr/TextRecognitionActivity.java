package com.universitedebordeaux.joue_maths_gie.ocr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
import com.universitedebordeaux.joue_maths_gie.MainActivity;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Recognition and preview activity.
public class TextRecognitionActivity extends AppCompatActivity {

    public static final int requestCameraPermissionID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CameraSource cameraSource;
        SurfaceView cameraView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        cameraView = findViewById(R.id.camera_view);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TextRecognitionActivity.class.getSimpleName(), "Detector dependencies are not yet available.");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolderCallback(this, cameraView, cameraSource));
            textRecognizer.setProcessor(new TextAnalyzer(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == requestCameraPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                recreate();
            else
                onBackPressed();
        }
    }

    // Send the recognition result to the activity who call this.
    public void doOnResult(List<CardWithLines> cardsWithLines) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("CARD_LIST", CardWithLines.toParcelableList(cardsWithLines));

        data.putExtras(bundle);
        setResult(CommonStatusCodes.SUCCESS, data);
        finish();
    }

    //Override the behaviour of the back button in the activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}