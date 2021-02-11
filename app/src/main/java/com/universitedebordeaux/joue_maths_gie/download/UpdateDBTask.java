package com.universitedebordeaux.joue_maths_gie.download;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.universitedebordeaux.joue_maths_gie.MainActivity;
import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;

import java.io.IOException;
import java.lang.ref.WeakReference;

// Task to manages the database update mechanism.
public class UpdateDBTask {

    // private static final String MANIFEST = "manifest.yaml";
    // TODO: This link will be the temporary link where the manifest will be stored in the future.
    // private static final String MANIFEST_URL = "https://www.devlmx.fr/joue_maths_gie/manifest/" + MANIFEST;
    private static final String ASSETS_DIR_NAME = "database";

    // The Manifest file.
    /*
    public static class Manifest {
        public Manifest() {
            name = MANIFEST;
            version = "";
            data = "";
            figs = "";
        }

        public String name;
        public String version;
        public String data;
        public String figs;
    }
    */

    private final WeakReference<Context> mContext;

    public UpdateDBTask(Context context) {
        mContext = new WeakReference<>(context);
    }

    public boolean doInBackground() {
        if (!checkStoredData()) {
            return false;
        }
        loadDatabase();
        return true;
    }

    /*
    private void checkUpdate() {
        // TODO: When the link will be created, download the manifest and check if any update is available.
    }
    */

    /*
    private void downloadPackage(Manifest manifest) {
        // TODO: This function will download the package from the manifest and extract it.
        // It will be placed into assets/**Package Name**.
    }
    */

    // This function will check if any data is stored in the application and return true if the
    // data exists or false if not. We check the .db file in the Joue_Maths_Gie directory.
    private boolean checkStoredData() {
        Context context = mContext.get();
        AssetManager assetManager = context.getAssets();

        try {
            assetManager.open(AppDatabase.DB_NAME);
        } catch (IOException e) {
            Log.i(getClass().getSimpleName(), "Cannot open file: Got [" + e.getMessage() + "]");
            return false;
        }
        return true;
    }

    private void loadDatabase() {
        Context context = mContext.get();

        MainActivity.appDatabase = AppDatabase.create(context, AppDatabase.DB_NAME);
    }
}