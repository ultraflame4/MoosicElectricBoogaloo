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
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class LibAddSongDialog extends DialogFragment {
    private NavController controller;
    private EditText songLocationInput;
    ActivityResultLauncher<Intent> OpenFileDialogIntentLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setup the dialog and inflate the layout
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog, container, false);
        // get the nav controller
        controller = NavHostFragment.findNavController(this);

        songLocationInput = view.findViewById(R.id.songUriInput);
        // On Next Button
        Button nxtButton = view.findViewById(R.id.nextButton);
        // On next button clicked listener
        nxtButton.setOnClickListener(view1 -> {
            // Get the song location from the input field
            String s = songLocationInput.getText().toString();
            // If the song location is empty, show a toast
            if (s.length() < 1) {
                Toast.makeText(getContext(), "Song url  / file link Cannot be empty!", Toast.LENGTH_SHORT).show();
            }
            else{
                // Else start navigating to the next dialog. (LibAddSongDialogB)

                Bundle bundle = new Bundle();
                // trim any whitespace from the song location and put in bundle for next dialog
                bundle.putString("songMediaLink",s.trim()); // also trim leading and trailing white space :)
                // Go back to prev screen first before starting next dialog
                navigateBack();
                controller.navigate(R.id.action_libraryFragment_to_libAddSongDialogB,bundle);
            }
        });

        // Object to open file picker
        OpenFileDialogIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // on file picked...
            // Get data from the intent
            Intent data = result.getData();
            if (data == null) {
                // If the data is null, show a toast
                Log.d("LibAddSongDialog", "File picking canceled");
                return;
            }
            // Else, get the uri from the data
            Uri uri = data.getData();

            Log.d("LibAddSongDialog", "Uri picked: " + uri.toString());
            // Set the song location input field to the uri
            songLocationInput.setText(uri.toString());
        });


        Button browseFilesBtn = view.findViewById(R.id.browseFileBtn);
        // On Browse files btn clicked listener
        browseFilesBtn.setOnClickListener(view1 -> {
            // open the file picker
            OpenFilePicker();
        });


        // On Cancel
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
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

    public void OpenFilePicker(){
        // Create the new intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Set any file type
        intent.setType("*/*");
        // Start the file picking activity
        OpenFileDialogIntentLauncher.launch(intent);
    }

    private void navigateBack() {

        controller.navigateUp();
    }
}
