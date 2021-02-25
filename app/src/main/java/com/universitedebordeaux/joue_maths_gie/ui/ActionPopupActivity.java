package com.universitedebordeaux.joue_maths_gie.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.StringRes;
import com.universitedebordeaux.joue_maths_gie.R;

public class ActionPopupActivity {

    private TextView tvTitle;
    private EditText etField;
    private Button button;

    private final Activity activity;
    private Dialog dialog;

    public ActionPopupActivity(@StringRes int title, @StringRes int hintText, Activity activity) {
        this.activity = activity;

        setDialog();
        findViews();
        setData(title, hintText);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void setDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_activity);
    }

    private void findViews() {
        tvTitle = dialog.findViewById(R.id.popup_title);
        etField = dialog.findViewById(R.id.popup_text_field);
        button = dialog.findViewById(R.id.popup_ok_bouton);
    }

    private void setData(@StringRes int title, @StringRes  int hintText) {
        tvTitle.setText(title);
        etField.setText(hintText);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        // TODO
    }
}
