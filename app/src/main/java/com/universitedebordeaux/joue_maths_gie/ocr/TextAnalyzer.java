package com.universitedebordeaux.joue_maths_gie.ocr;

import android.util.Log;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.universitedebordeaux.joue_maths_gie.db.CardWithLines;
import com.universitedebordeaux.joue_maths_gie.ui.CameraActivity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

// Receive and analyze different group of words than OCR could receive
public class TextAnalyzer implements Detector.Processor<TextBlock> {

    private final WeakReference<CameraActivity> mRefActivity;
    private final AtomicBoolean aBoolean;

    public TextAnalyzer(CameraActivity activity) {
        mRefActivity = new WeakReference<>(activity);
        aBoolean = new AtomicBoolean(true);
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
                CameraActivity activity = mRefActivity.get();
                SearchTask task = new SearchTask();
                List<CardWithLines> cards;

                for (final String str : list) {
                    Log.d(getClass().getSimpleName(), str);
                }
                cards = task.doInBackground(list.toArray(new String[0]));
                if (cards != null && !cards.isEmpty()) {
                    activity.runOnUiThread(() -> {
                        // Blocked any thread that attempts to start the card activity
                        // at the same time.
                        if (aBoolean.compareAndSet(true, false)) {
                            activity.doOnResult(cards);
                            // Release the TextAnalyzer class after successful result.
                            release();
                        }
                    });
                }
            });
        }
    }
}