package com.universitedebordeaux.jumathsji.ocr;

import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

public class TextAnalyzer implements Detector.Processor<TextBlock> {

    @Override
    public void release() {
    }

    @Override
    public void receiveDetections(@NonNull Detector.Detections<TextBlock> detections) {
        final SparseArray<TextBlock> items = detections.getDetectedItems();

        if (items.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            Log.i("Result", stringBuilder.toString());
        }
    }
}