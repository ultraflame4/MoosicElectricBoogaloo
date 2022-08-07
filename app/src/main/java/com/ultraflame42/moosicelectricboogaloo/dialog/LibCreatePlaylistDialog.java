package com.ultraflame42.moosicelectricboogaloo.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.ui.others.AddSongsActivity;

public class LibCreatePlaylistDialog extends DialogFragment {
    private NavController controller;
    private EditText playlistNameInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setup the fragment
        UsefulStuff.setupDialogFragment(this);

        // inflate the dialog layout
        View view = inflater.inflate(R.layout.create_playlist_dialog, container, false);

        // Get the name edit text input
        playlistNameInput = view.findViewById(R.id.playlistNameInput);

        // get navigation controller
        controller = NavHostFragment.findNavController(this);

        // get the create button
        Button createPlaylistBtn = view.findViewById(R.id.createPlaylistBtn);
        // on button click... handle create playlist
        createPlaylistBtn.setOnClickListener(view1 -> {
            handleCreatePlaylist(playlistNameInput.getText().toString());
        });


        return view;
    }

    public void handleCreatePlaylist(String playlistName) {
        // If the playlist name is empty, show a toast and return
        if (playlistName.length() < 1) {
            Toast.makeText(getContext(), "Playlist name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Else,
        // 1. Go back to the previous screen,
        controller.navigateUp();
        // 2. start add songs activity
        Intent intent = new Intent(getContext(), AddSongsActivity.class);
        intent.putExtra("playlistName", playlistName);
        startActivity(intent);
        // note actual creation of playlist will be done in the AddSongsactivity.
    }
}
