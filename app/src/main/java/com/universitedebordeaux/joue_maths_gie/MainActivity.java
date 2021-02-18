package com.universitedebordeaux.joue_maths_gie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.universitedebordeaux.joue_maths_gie.download.UpdateDBTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        // Updating the database at the start.
        updateDatabase();
    }

    private void updateDatabase() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        UpdateDBTask updater = new UpdateDBTask(getApplicationContext());

        executorService.execute(() -> {
            boolean res = updater.doInBackground();

            runOnUiThread(() -> {
                if (res) {
                    Toast.makeText(this, R.string.data_restored_ok, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.no_data_stored, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}