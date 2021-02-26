package com.universitedebordeaux.joue_maths_gie.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.universitedebordeaux.joue_maths_gie.R;

public class AlertPopupActivity extends AbstractPopup {

    public AlertPopupActivity(String title, String text, Activity activity) {
        super(activity, R.layout.alert_popup_activity);
        setData(title, text);
    }

    private void setData(String title, String text) {
        TextView tvTitle = dialog.findViewById(R.id.alert_popup_title);
        TextView tvContent = dialog.findViewById(R.id.alert_popup_text);
        Button button = dialog.findViewById(R.id.alert_popup_bouton);

        tvTitle.setText(title);
        tvContent.setText(text);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        dismiss();
    }
}
