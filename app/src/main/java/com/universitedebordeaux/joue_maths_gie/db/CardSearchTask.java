package com.universitedebordeaux.joue_maths_gie.db;

/*
import android.util.Log;
import com.universitedebordeaux.joue_maths_gie.SecondActivity;

import java.lang.ref.WeakReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Asynchronous task used to retrieve a complete card.
// By complete, we mean, which contains its text so a CardWithLines.
public class CardSearchTask  {

    private final WeakReference<SecondActivity> mRefActivity;

    public CardSearchTask(SecondActivity activity) {
        mRefActivity = new WeakReference<>(activity);
    }

    public CardWithLines doInBackground(String id) {
        SecondActivity activity = mRefActivity.get();

        Log.d(CardSearchTask.class.getSimpleName(), "Searching cards.");
        if (activity == null) {
            return null;
        }
        return AppDatabase.db.cardDao().getCardWithLines(id);
    }

    public void onPostExecute(CardWithLines cardWithLines) {
        SecondActivity activity = mRefActivity.get();

        if (cardWithLines != null && activity != null) {
            activity.fillListCard(Stream.of(cardWithLines).collect(Collectors.toList()));
        }
    }
}
*/