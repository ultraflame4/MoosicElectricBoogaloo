package com.ultraflame42.moosicelectricboogaloo.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class LibAddSongDialog extends DialogFragment {
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog, container, false);
        controller = NavHostFragment.findNavController(this);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view1 -> {
            navigateBack();
        });
        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        navigateBack();
    }

    private void navigateBack() {
        controller.navigateUp();
    }
}
