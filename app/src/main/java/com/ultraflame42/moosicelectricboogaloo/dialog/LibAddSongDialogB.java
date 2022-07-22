package com.ultraflame42.moosicelectricboogaloo.dialog;

import android.content.DialogInterface;
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
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class LibAddSongDialogB extends DialogFragment {
    private NavController controller;
    private EditText songTitleInput;
    private EditText songArtistInput;
    private EditText songImageInput;
    private Button finalAddSongBtn;
    private String mediaLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog2, container, false);
        controller = NavHostFragment.findNavController(this);

        mediaLink = getArguments().getString("songMediaLink");


        songTitleInput = view.findViewById(R.id.songTitleInput);
        songArtistInput = view.findViewById(R.id.songArtistInput);
        songImageInput = view.findViewById(R.id.songImageInput);

        finalAddSongBtn = view.findViewById(R.id.finalAddSongBtn);

        finalAddSongBtn.setOnClickListener(view1 -> {
            HandleAddSong();
            navigateBack();// done
        });
        // todo Add in browse local file for images

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view1 -> {
            navigateBack();
            controller.navigate(R.id.action_libraryFragment_to_libAddSongDialog);
        });
        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        navigateBack();
    }

    private void navigateBack() {
        // send message to back stack
        controller.navigateUp();
    }

    private void HandleAddSong() {
        String songTitle = songTitleInput.getText().toString();
        String songArtist = songArtistInput.getText().toString();
        String songImageLink = songImageInput.getText().toString();


        if (songTitle.isEmpty() || songArtist.isEmpty()) {
            Toast.makeText(getContext(), "Song title or song artist empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // todo Set the song image link
        SongRegistry.getInstance().add(new Song(songTitle,songArtist,mediaLink));
    }
}
