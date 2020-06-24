package com.universitedebordeaux.jumathsji.ocr;

import android.content.res.AssetManager;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// This analyzer acquires the frames from the camera.
public class TextAnalyzer implements ImageAnalysis.Analyzer, OnSuccessListener<FirebaseVisionText> {
    // Google OCR.
    private FirebaseVisionTextRecognizer mDetector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

    private TextRecognitionActivity mActivity;
    private AtomicBoolean isBusy;

    private TfInterpreter mInterpreter;

    public TextAnalyzer(TextRecognitionActivity activity) {
        mActivity = activity;

        // Used to slow down the process.
        isBusy = new AtomicBoolean(false);

        // CNN classifier : MobileNet, uses a TensorFlow Lite model.
        mInterpreter = new TfInterpreter("model/e3dcm1.tflite", loadLabelsFromAsset(activity.getAssets(), "model/labels.txt"));
    }

    // Reads the label file and returns the list.
    private List<String> loadLabelsFromAsset(AssetManager assetManager, String filename) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(filename)));
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            Log.e("OCR", "Failed to load labels from file");
            e.printStackTrace();
        }

        // Hardcoded backup
        return Arrays.asList("e3dcm1q_1", "e3dcm1q_2", "e3dcm1q_3", "e3dcm1q_17");
    }

    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException("Rotation must be 0, 90, 180, or 270.");
        }
    }

    @Override
    // Get the frame and start the process.
    public void analyze(ImageProxy imageProxy, int degrees) {
        if (imageProxy == null || imageProxy.getImage() == null) return;

        // Send frame to the OCR one by one.
        if (isBusy.compareAndSet(false, true)) {
            Image mediaImage = imageProxy.getImage();
            FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, degreesToFirebaseRotation(degrees));

            // Always prepare the CNN classifier, we don't know if OCR will be enough.
            mInterpreter.prepareTensorImage(image.getBitmap());

            // Runs the OCR.
            mDetector.processImage(image)
                    .addOnSuccessListener(this)
                    .addOnCompleteListener(result -> isBusy.set(false))
                    .addOnFailureListener(result -> Log.e("OCR", "Text Recognition failed : " + result.getMessage()));
        }
    }

    @Override
    // The OCR found something.
    public void onSuccess(FirebaseVisionText result) {

        // Transforms line blocks into a single line list.
        String[] lines = result.getTextBlocks().stream()
                .map(FirebaseVisionText.TextBlock::getLines)
                .flatMap(List::stream)
                .map(line -> line.getText())
                .toArray(String[]::new);

        // Start the asynchronous recognition task.
        new SearchTask(mActivity, mInterpreter).execute(lines);
    }
}
