package com.universitedebordeaux.joue_maths_gie.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universitedebordeaux.joue_maths_gie.R;

public class ShareFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShareViewModel shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}