package com.universitedebordeaux.joue_maths_gie.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import androidx.annotation.LayoutRes;

import java.lang.ref.WeakReference;

public abstract class AbstractPopup {

    protected final WeakReference<Activity> activity;
    protected Dialog dialog;

    public AbstractPopup(Activity activity, @LayoutRes int layout) {
        this.activity = new WeakReference<>(activity);
        setDialog(layout);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void setDialog(@LayoutRes int layout) {
        dialog = new Dialog(activity.get());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}