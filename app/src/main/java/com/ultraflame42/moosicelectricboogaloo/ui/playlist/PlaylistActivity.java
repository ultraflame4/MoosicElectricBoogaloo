package com.ultraflame42.moosicelectricboogaloo.ui.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class PlaylistActivity extends AppCompatActivity {

    private int playlistId = -1;
    private View favBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playlistId = extras.getInt("playlistId");
        }
        else{
            Toast.makeText(this, "Error: Could not get playlist index", Toast.LENGTH_SHORT).show();
        }

        favBtn = findViewById(R.id.favouriteBtn);
        favBtn.setSaveEnabled(false);
        favBtn.setOnClickListener(view -> {
            handleFavourite();
        });

    }

    public void handleBack(View view){
        finish();
    }

    public void handleMore(View view) {
        Log.d("PlaylistActivity", "Opening more options menu");

    }

    public void handleFavourite() {
        Log.d("PlaylistActivity", "Toggling favourite for playlistId " + playlistId);
    }

    public void handleAddSongs(View view){
        Log.d("PlaylistActivity", "Opening add songs menu");
    }
}