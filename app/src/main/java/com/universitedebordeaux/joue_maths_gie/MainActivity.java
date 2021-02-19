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
        Button button;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Connecting all buttons.
        button = findViewById(R.id.play_button);
        button.setOnClickListener(this::startScan);

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

    private void startScan(View view)
    {
        Intent intent = new Intent(this, CameraActivity.class);

        startActivity(intent);
    }
}