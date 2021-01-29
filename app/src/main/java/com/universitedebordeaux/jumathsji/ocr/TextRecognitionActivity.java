package com.universitedebordeaux.jumathsji.ocr;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.universitedebordeaux.jumathsji.R;

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
                    .setRequestedFps(15.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolderCallback(this, cameraView, cameraSource));
            textRecognizer.setProcessor(new TextAnalyzer());
        }
    }
}