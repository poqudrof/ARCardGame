package com.universitedebordeaux.joue_maths_gie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.universitedebordeaux.joue_maths_gie.download.UpdateDBTask;
import com.universitedebordeaux.joue_maths_gie.ui.CameraActivity;

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
        Toast updateToast;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        UpdateDBTask updater = new UpdateDBTask(getApplicationContext());

        updateToast = Toast.makeText(this, R.string.updating_data, Toast.LENGTH_LONG);
        updateToast.show();
        executorService.execute(() -> {
            boolean hasSavedData = updater.doInBackground();

            updateToast.cancel();
            runOnUiThread(() -> {
                if (hasSavedData) {
                    Toast.makeText(this, R.string.data_restored_ok, Toast.LENGTH_SHORT).show();
                } else {
                    showNoDataSavedToast();
                }
                // Now connecting all buttons.
                setData(hasSavedData);
            });
        });
    }

    private void showNoDataSavedToast() {
        Toast.makeText(this, R.string.no_data_stored, Toast.LENGTH_LONG).show();
    }

    private void setData(boolean hasSavedData) {
        Button scanButton = findViewById(R.id.play_button);

        if (hasSavedData) {
            scanButton.setOnClickListener(this::startScan);
        } else {
            scanButton.setOnClickListener(this::cannotStartScan);
        }
    }

    private void startScan(View view)
    {
        Intent intent = new Intent(this, CameraActivity.class);

        startActivity(intent);
    }

    private void cannotStartScan(View view) {
        showNoDataSavedToast();
    }
}