package com.universitedebordeaux.joue_maths_gie.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.universitedebordeaux.joue_maths_gie.R;

// Activity to display the game's rules as an web page.
public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        readHtml();
    }

    // print the rules by a static html page
    @SuppressLint("SetJavaScriptEnabled")
    private void readHtml() {
        WebView webView = findViewById(R.id.rules_web_view);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/rules.html");
    }
}
