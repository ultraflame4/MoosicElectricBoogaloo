package com.ultraflame42.moosicelectricboogaloo.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.data.Storage;
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
    private ActivityResultLauncher<Intent> OpenFileDialogIntentLauncher;

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
        });

        OpenFileDialogIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data == null) {
                Log.d("LibAddSongDialogB", "File picking canceled");
                return;
            }
            Uri uri = data.getData();

            Log.d("LibAddSongDialogB", "Uri picked: " + uri.toString());
            songImageInput.setText(uri.toString());
        });

        Button browseFilesBtn = view.findViewById(R.id.browseImgBtn);
        browseFilesBtn.setOnClickListener(view1 -> {
            // open the file picker
            OpenFilePicker();
        });


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

    public void OpenFilePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        OpenFileDialogIntentLauncher.launch(intent);
    }

    private void HandleAddSong() {
        String songTitle = songTitleInput.getText().toString();
        String songArtist = songArtistInput.getText().toString();
        String songImageLink = songImageInput.getText().toString();


        if (songTitle.isEmpty() || songArtist.isEmpty()) {
            Toast.makeText(getContext(), "Song title or song artist empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String songUri = mediaLink;
        if (Uri.parse(mediaLink).getScheme().equals("content")) {
            songUri = Storage.getInstance().DownloadLocalSong(getContext(),mediaLink);
        }
        String imageUriLink = songImageLink;
        // Check if image link is empty, so we dont get a null pointer exception
        if (!imageUriLink.isEmpty()){
            // If the user gave us a link, we need to download it/ move it to itnernal storage so we can access it anytimes without pernission issues
            if (Uri.parse(imageUriLink).getScheme().equals("content")) {
                imageUriLink = Storage.getInstance().DownloadLocalImage(getContext(),imageUriLink);
            }
        }
        else{
            imageUriLink = "";
        }

        SongRegistry.getInstance().add(new Song(songTitle, songArtist, songUri, imageUriLink));
        navigateBack();
    }
}
