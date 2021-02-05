package com.universitedebordeaux.jumathsji.ocr;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.universitedebordeaux.jumathsji.db.CardWithLines;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextAnalyzer implements Detector.Processor<TextBlock> {

    private final WeakReference<TextRecognitionActivity> mRefActivity;

    public TextAnalyzer(TextRecognitionActivity activity) {
        mRefActivity = new WeakReference<>(activity);
    }

    @Override
    public void release() {
    }

    @Override
    public void receiveDetections(@NonNull Detector.Detections<TextBlock> detections) {
        final SparseArray<TextBlock> items = detections.getDetectedItems();
        List<String> list = new LinkedList<>();

        if (items.size() != 0) {
            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                list.add(item.getValue());
            }
        }
        if (!list.isEmpty()) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(() -> {
                TextRecognitionActivity activity = mRefActivity.get();
                SearchTask task = new SearchTask();
                List<CardWithLines> cards;

                cards = task.doInBackground(list.toArray(new String[0]));
                activity.runOnUiThread(() -> {
                    if (cards != null && !cards.isEmpty()) {
                        activity.doOnResult(cards);
                    }
                });
            });
        }
    }
}