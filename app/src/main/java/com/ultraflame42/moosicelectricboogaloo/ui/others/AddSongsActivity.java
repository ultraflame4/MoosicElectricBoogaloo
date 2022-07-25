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

        String playlistName = getIntent().getExtras().getString("playlistName");

        RecyclerView recyclerViewSongList = findViewById(R.id.songlist);
        AddSongPlaylistAdapter songListAdapter = new AddSongPlaylistAdapter(this);
        recyclerViewSongList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongList.setAdapter(songListAdapter);
        songListAdapter.updateData();

        Button createPlaylisBtn = findViewById(R.id.finalCreatePlaylistBtn);
        createPlaylisBtn.setOnClickListener(v -> {
            PlaylistRegistry.getInstance().add(
                    new SongPlaylist(
                            "you",
                            playlistName,
                            songListAdapter.getCheckedSongs().toArray(new Integer[0])
                    )
            );
            finish();
        });
    }
}