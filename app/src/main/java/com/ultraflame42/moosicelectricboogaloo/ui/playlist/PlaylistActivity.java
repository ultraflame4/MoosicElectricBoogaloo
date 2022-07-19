package com.ultraflame42.moosicelectricboogaloo.ui.playlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.Playlist.PlaylistRecylerViewAdapter;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class PlaylistActivity extends AppCompatActivity {

    private int playlistId = -1;
    private ToggleButton favBtn;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScroll;

    private static final int yOffsetThreshold = 1010; // threshold before the nested scrollview stops scrolling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playlistId = extras.getInt("playlistId");
        } else {
            Toast.makeText(this, "Error: Could not get playlist index", Toast.LENGTH_SHORT).show();
        }

        favBtn = findViewById(R.id.favouriteBtn);
        favBtn.setSaveEnabled(false);
        favBtn.setOnClickListener(view -> {
            handleFavourite();
        });

        recyclerView = findViewById(R.id.scollCtn);
        recyclerView.setAdapter(new PlaylistRecylerViewAdapter());

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

    }

    public void handleBack(View view) {
        finish();
    }

    public void handleMore(View view) {
        Log.d("PlaylistActivity", "Opening more options menu");

    }

    public void handleFavourite() {
        Log.d("PlaylistActivity", "Toggling favourite for playlistId " + playlistId);
    }

    public void handleAddSongs(View view) {
        Log.d("PlaylistActivity", "Opening add songs menu");
    }
}