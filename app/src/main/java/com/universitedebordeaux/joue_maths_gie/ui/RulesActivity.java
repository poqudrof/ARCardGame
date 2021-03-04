package com.universitedebordeaux.joue_maths_gie.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.universitedebordeaux.joue_maths_gie.R;
import java.io.*;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        setData();
        readText();
    }

    private void readText() {
        TextView txtView = findViewById(R.id.rules_text);
        StringBuilder text = new StringBuilder();

        try {
            InputStream inputStream = getAssets().open("rules.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            bufferedReader.close();

        } catch (final IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
            return;
        }

        txtView.setText(text.toString());
    }

    private void setData() {
        FloatingActionButton returnTopButton = findViewById(R.id.rules_return_button);

        returnTopButton.setOnClickListener(this::onReturnButtonClick);
    }

    private void onReturnButtonClick(View view) {
        onBackPressed();
    }
}
