package com.universitedebordeaux.jumathsji.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
import com.universitedebordeaux.jumathsji.MainActivity;
import com.universitedebordeaux.jumathsji.R;
import com.universitedebordeaux.jumathsji.db.CardWithLines;

import java.util.List;

// Recognition and preview activity.
public class TextRecognitionActivity extends AppCompatActivity {

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