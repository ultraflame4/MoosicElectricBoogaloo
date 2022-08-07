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

public class LibAddItemDialog extends DialogFragment {
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setup the dialog and inflate the layout
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_dialog, container, false);
        // get the nav controller
        controller = NavHostFragment.findNavController(this);
        // get add item btn
        Button addSongBtn = view.findViewById(R.id.addSongBtn);

        // on click listener for add song btn
        addSongBtn.setOnClickListener(view1 -> {
            // on click, go back to previous screen
            navigateBack();
            // navigate to add song dialog
            controller.navigate(R.id.action_libraryFragment_to_libAddSongDialog);
        });

        // on click listener for create playlist btn
        Button createPlaylistBtn = view.findViewById(R.id.createPlaylistBtn);
        createPlaylistBtn.setOnClickListener(view1 -> {
            // on click, go back to previous screen
            controller.navigateUp();
            // Navigate to create playlist dialog
            controller.navigate(R.id.action_libraryFragment_to_createPlaylistDialog);
        });

        // get cancel btn
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        // on cancel
        cancelBtn.setOnClickListener(view1 -> {
            // on click, go back to previous screen
            navigateBack();
        });
        return view;

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        navigateBack();
    }

    private void navigateBack() {
        // on click, go back to previous screen
        controller.navigateUp();
    }
}

