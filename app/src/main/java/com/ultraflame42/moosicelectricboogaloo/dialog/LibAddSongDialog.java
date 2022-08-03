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
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog, container, false);
        controller = NavHostFragment.findNavController(this);

        songLocationInput = view.findViewById(R.id.songUriInput);
        // On Next Button
        Button nxtButton = view.findViewById(R.id.nextButton);
        nxtButton.setOnClickListener(view1 -> {
            String s = songLocationInput.getText().toString();
            if (s.length() < 1) {
                Toast.makeText(getContext(), "Song url  / file link Cannot be empty!", Toast.LENGTH_SHORT).show();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("songMediaLink",s.trim()); // also trim leading and trailing white space :)
                navigateBack();
                controller.navigate(R.id.action_libraryFragment_to_libAddSongDialogB,bundle);
            }
        });

        // Object to open file picker
        OpenFileDialogIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data == null) {
                Log.d("LibAddSongDialog", "File picking canceled");
                return;
            }
            Uri uri = data.getData();

            Log.d("LibAddSongDialog", "Uri picked: " + uri.toString());
            songLocationInput.setText(uri.toString());
        });

        Button browseFilesBtn = view.findViewById(R.id.browseFileBtn);
        browseFilesBtn.setOnClickListener(view1 -> {
            // open the file picker
            OpenFilePicker();
        });




        // On Cancel
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

    public void OpenFilePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        OpenFileDialogIntentLauncher.launch(intent);
    }

    private void navigateBack() {
        // send message to back stack
        controller.navigateUp();
    }
}
