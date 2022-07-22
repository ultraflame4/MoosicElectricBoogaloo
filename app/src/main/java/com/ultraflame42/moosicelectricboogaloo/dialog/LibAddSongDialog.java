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
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class LibAddSongDialog extends DialogFragment {
    private NavController controller;
    private EditText txtLinkInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UsefulStuff.setupDialogFragment(this);
        View view = inflater.inflate(R.layout.library_add_song_dialog, container, false);
        controller = NavHostFragment.findNavController(this);

        txtLinkInput = view.findViewById(R.id.songTitleInput);
        // On Next Button
        Button nxtButton = view.findViewById(R.id.nextButton);
        nxtButton.setOnClickListener(view1 -> {
            String s = txtLinkInput.getText().toString();
            if (s.length() < 1) {
                Toast.makeText(getContext(), "Song url  / file link Cannot be empty!", Toast.LENGTH_SHORT).show();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("songMediaLink",s);
                navigateBack();
                controller.navigate(R.id.action_libraryFragment_to_libAddSongDialogB,bundle);
            }
        });

        // todo Add in browse local file for songs

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

    private void navigateBack() {
        // send message to back stack
        controller.navigateUp();
    }
}
