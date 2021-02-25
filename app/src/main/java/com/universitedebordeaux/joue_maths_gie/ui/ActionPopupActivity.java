package com.universitedebordeaux.joue_maths_gie.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.universitedebordeaux.joue_maths_gie.R;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class ActionPopupActivity {

    public interface ActionPopupButtonHandler {
        void onClick(@NotNull String editTextResult);
    }

    private TextView tvTitle;
    private EditText etField;
    private Button button;

    private final WeakReference<Activity> activity;
    private final ActionPopupButtonHandler buttonHandler;
    private Dialog dialog;

    public ActionPopupActivity(String title, String hintText, Activity activity,
                               ActionPopupButtonHandler buttonHandler) {

        this.activity = new WeakReference<>(activity);
        this.buttonHandler = buttonHandler;

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
        dialog = new Dialog(activity.get());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_activity);
    }

    private void findViews() {
        tvTitle = dialog.findViewById(R.id.popup_title);
        etField = dialog.findViewById(R.id.popup_text_field);
        button = dialog.findViewById(R.id.popup_ok_bouton);
    }

    private void setData(String title, String hintText) {
        tvTitle.setText(title);
        etField.setText(hintText);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        buttonHandler.onClick(etField.getText().toString());
    }
}
