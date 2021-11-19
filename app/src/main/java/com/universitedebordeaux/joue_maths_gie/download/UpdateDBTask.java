package com.universitedebordeaux.joue_maths_gie.download;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.universitedebordeaux.joue_maths_gie.R;
import com.universitedebordeaux.joue_maths_gie.db.AppDatabase;

import java.io.IOException;
import java.lang.ref.WeakReference;

// Task to manages the database update mechanism.
public class UpdateDBTask {

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

    // This function will check if any data is stored in the application and return true if the
    // data exists or false if not.
    private boolean checkStoredData() {
        Context context = mContext.get();
        AssetManager assetManager = context.getAssets();

        // This checks only if the file exists... ?
        try {
            assetManager.open(context.getString(R.string.db_name));
        } catch (IOException e) {
            Log.i(getClass().getSimpleName(), "Cannot open file: Got [" + e.getMessage() + "]");
            return false;
        }
        return true;
    }

    // Load the database by create it.
    private void loadDatabase() {
        Context context = mContext.get();

        AppDatabase.db = AppDatabase.create(context);
    }
}