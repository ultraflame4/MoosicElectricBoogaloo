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
        // Setup the dialog and inflate the layout
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog2, container, false);
        // get the nav controller
        controller = NavHostFragment.findNavController(this);

        // Get the song location uri from previous dialog
        mediaLink = getArguments().getString("songMediaLink");

        // Get various views
        songTitleInput = view.findViewById(R.id.songTitleInput);
        songArtistInput = view.findViewById(R.id.songArtistInput);
        songImageInput = view.findViewById(R.id.songImageInput);

        finalAddSongBtn = view.findViewById(R.id.finalAddSongBtn);

        // On add song button clicked
        finalAddSongBtn.setOnClickListener(view1 -> {
            // handle add song
            HandleAddSong();
        });

        // Launcher for the File picker activity
        OpenFileDialogIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // on file picked...
            // Get data from the intent
            Intent data = result.getData();

            if (data == null) {
                // If the data is null, show a toast
                Log.d("LibAddSongDialogB", "File picking canceled");
                return;
            }
            // Else, get the uri from the data
            Uri uri = data.getData();

            Log.d("LibAddSongDialogB", "Uri picked: " + uri.toString());
            // Set the song image input field to the uri
            songImageInput.setText(uri.toString());
        });

        Button browseFilesBtn = view.findViewById(R.id.browseImgBtn);
        // On Browse files btn clicked listener
        browseFilesBtn.setOnClickListener(view1 -> {
            // open the file picker
            OpenFilePicker();
        });


        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view1 -> {
            // on click, go back to previous screen
            navigateBack();
            // navigate to previous dialog.
            controller.navigate(R.id.action_libraryFragment_to_libAddSongDialog);
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

    public void OpenFilePicker(){
        // Create the new intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Set any file type
        intent.setType("*/*");
        // Start the file picking activity
        OpenFileDialogIntentLauncher.launch(intent);
    }


    private void HandleAddSong() {
        // Get song detials from the input fields
        String songTitle = songTitleInput.getText().toString();
        String songArtist = songArtistInput.getText().toString();
        String songImageLink = songImageInput.getText().toString();

        // If title or song artist is empty, show toast
        if (songTitle.isEmpty() || songArtist.isEmpty()) {
            Toast.makeText(getContext(), "Song title or song artist empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set songuri to media link
        String songUri = mediaLink;
        if (Uri.parse(mediaLink).getScheme().equals("content")) {
            // If uri is in external storage, download to internal and get path
            songUri = Storage.getInstance().DownloadLocalSong(getContext(),mediaLink);
        }

        // set to songImageLnk
        String imageUriLink = songImageLink;
        // Check if image link is empty, so we dont get a null pointer exception
        if (!imageUriLink.isEmpty()){
            // If the user gave us a link, we need to download it/ move it to internal storage so we can access it anytime without permission issues
            if (Uri.parse(imageUriLink).getScheme().equals("content")) {
                imageUriLink = Storage.getInstance().DownloadLocalImage(getContext(),imageUriLink);
            }
        }
        else{
            imageUriLink = "";
        }

        // Add the new song to the song registry
        SongRegistry.getInstance().add(new Song(songTitle, songArtist, songUri, imageUriLink));
        // Go back to previous screen
        navigateBack();
    }
}
