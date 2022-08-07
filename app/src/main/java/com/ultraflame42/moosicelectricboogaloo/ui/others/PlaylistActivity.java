package com.ultraflame42.moosicelectricboogaloo.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.playlist.PlaylistRecylerViewAdapter;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;

public class PlaylistActivity extends AppCompatActivity {

    private int playlistId = -1;
    private ToggleButton favBtn;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScroll;

    private static final int yOffsetThreshold = 1010; // threshold before the nested scrollview stops scrolling
    private ToggleButton playlistPlayBtn;

    private EventListenerGroup listenerGroup = new EventListenerGroup();
    private TextView playlistTitle;
    private TextView playlistCreator;

    PlaylistRegistry playlistRegistry;
    private ImageView playlistCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlistRegistry = PlaylistRegistry.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playlistId = extras.getInt("playlistId");
        } else {
            Toast.makeText(this, "Error: Could not get playlist index", Toast.LENGTH_SHORT).show();
            return; // If cannot get playlist index. STOP
        }


        SongPlaylist playlist = playlistRegistry.get(playlistId).item;

        playlistTitle = findViewById(R.id.playlistTitle);
        playlistCreator = findViewById(R.id.playlistCreator);
        playlistCover = findViewById(R.id.playlistCover);

        playlistTitle.setText(playlist.getTitle());
        playlistCreator.setText(playlist.getCreator());

        playlist.loadCoverIntoImageView(playlistCover);

        favBtn = findViewById(R.id.favouriteBtn);
        updateFavButton();
        favBtn.setSaveEnabled(false);
        favBtn.setOnClickListener(view -> {
            handleFavourite();
        });


        playlistPlayBtn = findViewById(R.id.playlistPlay);
        playlistPlayBtn.setSaveEnabled(false);
        updatePlayStopBtn(!SongPlayer.IsPaused());
        playlistPlayBtn.setOnClickListener(view -> {
            handlePlaylistPlayStop();
        });
        listenerGroup.subscribe(SongPlayer.OnSongPlayStateChange,this::updatePlayStopBtn);


        // -----UI Scrolling stuff---------

        recyclerView = findViewById(R.id.scollCtn);
        recyclerView.setAdapter(new PlaylistRecylerViewAdapter(playlistId,this));

        nestedScroll = findViewById(R.id.nestedScrollCtn);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = Math.round(UsefulStuff.getDisplayMetrics(this).heightPixels - UsefulStuff.convertDpToPx(this, 64 + 96));


        nestedScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = nestedScroll.getScrollY();

            if (scrollY > yOffsetThreshold) {
                nestedScroll.scrollTo(0, yOffsetThreshold + 1);
                // When sticky allow nested scrolling
                recyclerView.setNestedScrollingEnabled(true);
            } else {
                // When not sticky
                recyclerView.smoothScrollToPosition( 0);
                recyclerView.setNestedScrollingEnabled(false);
            }

        });

        // -----END---------


    }

    private void handlePlaylistPlayStop() {
        Log.d("PlaylistActivity","Playlist play button clicked");
        if (!playlistPlayBtn.isChecked()) {
            SongPlayer.Pause();
        } else {
            if (SongPlayer.getCurrentPlaylist() == playlistId) {
                SongPlayer.Resume();
            } else {
                SongPlayer.PlayPlaylist(playlistId, 0);
            }
        }
    }

    private void updatePlayStopBtn(Boolean isPlaying) {
        Log.d("PlaylistActivity","Updating Play/Stop button");
        playlistPlayBtn.setChecked(!SongPlayer.IsPaused()&&SongPlayer.getCurrentPlaylist()==playlistId);
    }

    public void handleBack(View view) {
        finish();
    }

    public void handleMore(View view) {
        Log.d("PlaylistActivity", "Opening more options menu");
        //todo playlist options menu
    }

    public void handleFavourite() {
        Log.d("PlaylistActivity", "Toggling favourite for playlistId " + playlistId);
        if (!favBtn.isChecked()) {
            Log.d("PlaylistActivity", "un-favourited for playlistId " + playlistId);
            if (playlistRegistry.favouritesHas(playlistId)) {
                playlistRegistry.removeFromFavourites(playlistId);
            }
        } else {
            Log.d("PlaylistActivity", "favourited for playlistId " + playlistId);
            if (!playlistRegistry.favouritesHas(playlistId)) {
                playlistRegistry.addToFavourites(playlistId);
            }
        }
        updateFavButton();
    }

    private void updateFavButton() {
        Log.d("PlaylistActivity", "Updating favourite button");
        favBtn.setChecked(playlistRegistry.favouritesHas(playlistId));
    }

    public void handleAddSongs(View view) {
        Log.d("PlaylistActivity", "Opening add songs menu");
        //todo playlist add songs menu
    }
}