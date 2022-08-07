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

        // Get the playlist from index
        SongPlaylist playlist = playlistRegistry.get(playlistId).item;

        // Get the various views
        playlistTitle = findViewById(R.id.playlistTitle);
        playlistCreator = findViewById(R.id.playlistCreator);
        playlistCover = findViewById(R.id.playlistCover);

        // Set playlist title and creator
        playlistTitle.setText(playlist.getTitle());
        playlistCreator.setText(playlist.getCreator());
        // Load playlist cover
        playlist.loadCoverIntoImageView(playlistCover);
        // Get the fav toggle button
        favBtn = findViewById(R.id.favouriteBtn);
        // update the btn
        updateFavButton();
        // Disable save
        favBtn.setSaveEnabled(false);
        // SEt on btn click, handle favourite
        favBtn.setOnClickListener(view -> {
            handleFavourite();
        });

        // Get the playlist play button
        playlistPlayBtn = findViewById(R.id.playlistPlay);
        // Disable save
        playlistPlayBtn.setSaveEnabled(false);
        // update it
        updatePlayStopBtn(!SongPlayer.IsPaused());
        // On btn clicked, handle play/stop
        playlistPlayBtn.setOnClickListener(view -> {
            handlePlaylistPlayStop();
        });
        // SongPlayer play state is changed, update the btn
        listenerGroup.subscribe(SongPlayer.OnSongPlayStateChange,this::updatePlayStopBtn);


        // -----UI Scrolling stuff---------
        // get recycler view
        recyclerView = findViewById(R.id.scollCtn);
        recyclerView.setAdapter(new PlaylistRecylerViewAdapter(playlistId,this));

        nestedScroll = findViewById(R.id.nestedScrollCtn);

        // Change the recycler view height to match the screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = Math.round(UsefulStuff.getDisplayMetrics(this).heightPixels - UsefulStuff.convertDpToPx(this, 64 + 96));

        // Set it such that when recycler view is at the top, allow recycler view to scroll
        // So that the image cover is scrolled away before the recycler view can start scrolling
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
        // on play/stop btn clicked
        Log.d("PlaylistActivity","Playlist play button clicked");

        if (!playlistPlayBtn.isChecked()) {
            // If is not prev not checked, it was prev paused, so pause it
            SongPlayer.Pause();
        } else {
            // check if the current playlist is this
            if (SongPlayer.getCurrentPlaylist() == playlistId) {
                // if yes resume
                SongPlayer.Resume();
            } else {
                // else play from start
                SongPlayer.PlayPlaylist(playlistId, 0);
            }
        }
    }

    private void updatePlayStopBtn(Boolean isPlaying) {
        // update the play/stop button so it in the correct state
        Log.d("PlaylistActivity","Updating Play/Stop button");
        playlistPlayBtn.setChecked(!SongPlayer.IsPaused()&&SongPlayer.getCurrentPlaylist()==playlistId);
    }

    public void handleBack(View view) {
        finish();
    }

    public void handleMore(View view) {
        Log.d("PlaylistActivity", "Opening more options menu");
        //tod-o playlist options menu canceled
    }

    public void handleFavourite() {
        // On fav button clicked
        Log.d("PlaylistActivity", "Toggling favourite for playlistId " + playlistId);
        // if prev not checked, it was favourited
        if (!favBtn.isChecked()) {
            // So un favourite it
            Log.d("PlaylistActivity", "un-favourited for playlistId " + playlistId);
            // make sure the playlist is in the favourites list before removing it
            if (playlistRegistry.favouritesHas(playlistId)) {
                // remove it fro mfavourites
                playlistRegistry.removeFromFavourites(playlistId);
            }
        } else {
            // Else favourite it
            Log.d("PlaylistActivity", "favourited for playlistId " + playlistId);
            // make sure it is not alr favourited first
            if (!playlistRegistry.favouritesHas(playlistId)) {
                // add to favourites
                playlistRegistry.addToFavourites(playlistId);
            }
        }
        // update the button
        updateFavButton();
    }

    private void updateFavButton() {
        Log.d("PlaylistActivity", "Updating favourite button");
        // Set the state of the button to the correcct state
        favBtn.setChecked(playlistRegistry.favouritesHas(playlistId));
    }

    public void handleAddSongs(View view) {
        Log.d("PlaylistActivity", "Opening add songs menu");
        //~~tod o~~ playlist add songs menu canceled
    }
}