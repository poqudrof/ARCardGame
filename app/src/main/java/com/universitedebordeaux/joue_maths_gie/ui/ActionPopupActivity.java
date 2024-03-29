package com.universitedebordeaux.joue_maths_gie.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.universitedebordeaux.joue_maths_gie.R;
import org.jetbrains.annotations.NotNull;

// Popup to ask the user a text.
public class ActionPopupActivity extends AbstractPopup {

    public interface ActionPopupButtonHandler {
        void onClick(@NotNull String editTextResult);
    }

    private EditText textField;
    private final ActionPopupButtonHandler buttonHandler;

    // open popup when user click on action popup activity
    public ActionPopupActivity(String title, String hintText,
                               Activity activity,
                               ActionPopupButtonHandler buttonHandler) {
        super(activity, R.layout.action_popup_activity);
        this.buttonHandler = buttonHandler;
        setTitleHint(title, hintText);
    }

    public void setText(String editText) {
        textField.setText(editText);
    }

    // where the user can be write the code of a card
    private void setTitleHint(String title, String hintText) {
        TextView tvTitle = dialog.findViewById(R.id.action_popup_title);
        Button button = dialog.findViewById(R.id.action_popup_bouton);

        tvTitle.setText(title);
        textField = dialog.findViewById(R.id.action_popup_text_field);
        textField.setHint(hintText);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        buttonHandler.onClick(textField.getText().toString().trim());
    }
}