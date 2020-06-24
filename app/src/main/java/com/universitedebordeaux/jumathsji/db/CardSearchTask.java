package com.universitedebordeaux.jumathsji.db;

import android.os.AsyncTask;
import android.util.Log;

import com.universitedebordeaux.jumathsji.SecondActivity;

import java.lang.ref.WeakReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Asynchronous task used to retrieve a complete card.
// By complete, we mean, which contains its text so a CardWithLines.
public class CardSearchTask extends AsyncTask<String, Void, CardWithLines> {
    private WeakReference<SecondActivity> mRefActivity;

    public CardSearchTask(SecondActivity activity) {
        mRefActivity = new WeakReference<>(activity);
    }

    @Override
    protected CardWithLines doInBackground(String... id) {
        SecondActivity activity = mRefActivity.get();
        Log.d("search card function", "In the search card function");
        if (activity == null) return null;

        AppDatabase db = AppDatabase.getInstance(activity.getApplicationContext());

        return db.cardDao().getCardWithLines(id[0]);
    }

    @Override
    protected void onPostExecute(CardWithLines cardWithLines) {
        SecondActivity activity = mRefActivity.get();

        if (cardWithLines != null && activity != null) {
            activity.fillListCard(Stream.of(cardWithLines).collect(Collectors.toList()));
        }
    }
}
