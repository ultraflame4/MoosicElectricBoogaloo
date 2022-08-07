package com.ultraflame42.moosicelectricboogaloo.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.playlist.AddSongPlaylistAdapter;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class AddSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_songs);

        // Get the playlist name from the intent
        String playlistName = getIntent().getExtras().getString("playlistName");

        // Setup the recycler view osnglist
        RecyclerView recyclerViewSongList = findViewById(R.id.songlist);
        AddSongPlaylistAdapter songListAdapter = new AddSongPlaylistAdapter(this);
        recyclerViewSongList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongList.setAdapter(songListAdapter);
        // update data
        songListAdapter.updateData();

        // Get create playlist btn
        Button createPlaylisBtn = findViewById(R.id.finalCreatePlaylistBtn);
        // on btn clicked...
        createPlaylisBtn.setOnClickListener(v -> {
            // create nwe playlist with the name and songs
            PlaylistRegistry.getInstance().add(
                    new SongPlaylist(
                            "you",
                            playlistName,
                            songListAdapter.getCheckedSongs().toArray(new Integer[0])
                    )
            );
            // Exit this activity
            finish();
        });
    }
}