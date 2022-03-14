package com.universitedebordeaux.joue_maths_gie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;
import com.universitedebordeaux.joue_maths_gie.db.Card;
import com.universitedebordeaux.joue_maths_gie.db.CardDeck;
import com.universitedebordeaux.joue_maths_gie.download.UpdateDBTask;
import com.universitedebordeaux.joue_maths_gie.ui.CameraActivity;
import com.universitedebordeaux.joue_maths_gie.ui.RulesActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

// The main menu of the application. Also the starting point.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        updateDatabase();
    }

    @Override
    protected void onDestroy() {
        AppDatabase.removeDatabase(this);
        super.onDestroy();
    }

    // TODO: This could check for updates on a server.
    // Once the database is loaded, the button to start is activated.
    private void updateDatabase() {

        // in test
        String json = Card.loadJSONFromAsset(this);
        // Log.v("json: ", json);
        List<Card> cards = Card.loadCards(json);
        CardDeck.createMainDeck(cards);
        
        //Log.v("CARDS", String.valueOf(cards.size()));
        //Log.v("CARDS", cards.get(10).toString());
        //Log.v("CARDS", cards.get(11).toString());
        //Log.v("CARDS", cards.get(100).toString());

        Toast updateToast;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        UpdateDBTask updater = new UpdateDBTask(getApplicationContext());

        updateToast = Toast.makeText(this, R.string.updating_data, Toast.LENGTH_LONG);
        updateToast.show();
        executorService.execute(() -> {

            // TODO: update this with the new card loader
            //boolean hasSavedData = updater.doInBackground();
            boolean hasSavedData = true;

            updateToast.cancel();

            runOnUiThread(() -> {
                if (hasSavedData) {
                    showDataSavedToast();
                 } else {
                    showNoDataSavedToast();
                }
                setListeners(hasSavedData);
            });
        });


    }

    private void showDataSavedToast() {
        Toast.makeText(this, R.string.data_restored_ok, Toast.LENGTH_LONG).show();
    }
    private void showNoDataSavedToast() {
        Toast.makeText(this, R.string.no_data_stored, Toast.LENGTH_LONG).show();
    }

    private void setListeners(boolean hasSavedData) {
        Button scanButton = findViewById(R.id.play_button);
        Button rulesButton = findViewById(R.id.rules_button);

        // impossible to click if the data was not loaded
        if (hasSavedData) {
            scanButton.setOnClickListener(this::loadCameraView);
        } else {
            // When the button is pressed, the error toast is showed again.
            scanButton.setOnClickListener(this::cannotStartScan);
        }
        rulesButton.setOnClickListener(this::startRules);
    }

    private void loadCameraView(View view)
    {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void cannotStartScan(View view) {
        showNoDataSavedToast();
    }

    private void startRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
}