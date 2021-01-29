package com.universitedebordeaux.jumathsji.ui.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universitedebordeaux.jumathsji.R;

public class SendFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SendViewModel sendViewModel = new ViewModelProvider(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}