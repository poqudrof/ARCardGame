package com.universitedebordeaux.jumathsji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.universitedebordeaux.jumathsji.download.UpdateDBTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Launch game button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::startJumathsji);

        // Download db button
        ImageButton downloadJumathsji = findViewById(R.id.downloadButton);
        downloadJumathsji.setOnClickListener(view -> updateDatabase(getResources().getString(R.string.database_url)));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updateDatabase(String uri) {
        Context context = this;
        UpdateDBTask updater = new UpdateDBTask(getApplicationContext(), mf -> {
            // String[] names = new File(context.getExternalFilesDir(mf.name), "data").list();
            // StringBuilder show = new StringBuilder();
            TextView tv = findViewById(R.id.textView);

            // if (names == null || names.length == 0) {
            //    show.append("BDD Vide !");
            // } else {
            //     show.append("La BDD est composée des fichiers :\n");
            //    for (String file : names) {
            //        show.append(file).append("\n");
            //    }
            //}
            // tv.setText(show.toString());
            tv.setText("La BDD est OK");
        });

        updater.execute(uri);
    }

    public void startJumathsji(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
