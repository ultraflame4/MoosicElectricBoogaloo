package com.ultraflame42.moosicelectricboogaloo.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class LibCreatePlaylistDialog extends DialogFragment {
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.create_playlist_dialog, container, false);

        controller = NavHostFragment.findNavController(this);
        Button createPlaylistBtn = view.findViewById(R.id.createPlaylistBtn);
        createPlaylistBtn.setOnClickListener(view1 -> {
            controller.navigateUp();
            // todo navigate to add song activity.
        });

        EditText playlistNameInput = view.findViewById(R.id.playlistNameInput);
        

        return view;
    }
}
