package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SongPlayFragment extends Fragment {


    private EventListenerGroup listenerGroup = new EventListenerGroup();
    private ImageButton skipBtn;
    private ImageButton prevBtn;
    private ToggleButton likedBtn;
    private PlaylistRegistry playlistRegistry;
    private RegistryItem<SongPlaylist> likedSongs;
    private TextView songCurrentTime;
    private TextView songTotalTime;
    private SongRegistry songRegistry;

    public SongPlayFragment() {

        songRegistry = SongRegistry.getInstance();
    }


    TextView songTitleView;
    TextView songArtistView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistRegistry = PlaylistRegistry.getInstance();
        likedSongs = playlistRegistry.get(0);

    }

    public void updateSongInfo(Song song) {
        Log.d("SongPlayer (fragment)", "Updating song information");
        songTitleView.setText(song.getTitle());
        songArtistView.setText(song.getArtist());
        updateLikedBtn();

    }


    private Handler seekbarUpdateHandler = new Handler();
    private SeekBar seekBar;
    private ToggleButton playStopBtn;
    private boolean isScrubbing = false;
    int seekBarProgressResolution = 20000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_play, container, false);
        songTitleView = view.findViewById(R.id.songPlayTitle);
        songArtistView = view.findViewById(R.id.songPlayArtist);


        // song liked button
        likedBtn = view.findViewById(R.id.likeSongBtn);
        likedBtn.setSaveEnabled(false);
        likedBtn.setOnClickListener((v) -> {
            onSongLikeBtnClicked();
        });

        listenerGroup.subscribe(SongPlayer.OnSongPlayStateChange, song -> {
            updatePlayStopBtn();
        });

        skipBtn = view.findViewById(R.id.songPlay_SkipBtn);
        skipBtn.setOnClickListener(v -> {
            SongPlayer.PlayNext();
        });

        prevBtn = view.findViewById(R.id.songPlay_PrevBtn);
        prevBtn.setOnClickListener(v -> {
            SongPlayer.PlayPrev();
        });


        int currentSong = SongPlayer.GetCurrentSong();

        if (currentSong >= 0) {
            updateSongInfo(SongRegistry.getInstance().getItem(currentSong));
        }

        listenerGroup.subscribe(SongPlayer.OnSongPlayChange, regItem -> {
            updateSongInfo(regItem.item);
        });

        songCurrentTime = view.findViewById(R.id.songTimeCurrent);
        songTotalTime = view.findViewById(R.id.songTimeTotal);

        // Song progress bar
        seekBar = view.findViewById(R.id.songPlaySeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    SongPlayer.SetCurrentSongProgress(progress / (float) seekBarProgressResolution);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isScrubbing = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isScrubbing = false;
            }
        });


        seekBar.setMax(seekBarProgressResolution);
        // Update progress bar every half a second
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // When scrubbing, stop updating the progress bar
                float progress = SongPlayer.GetCurrentSongProgress();
                if (!isScrubbing) {
                    seekBar.setProgress(Math.round(progress * seekBarProgressResolution));
                }
                int songId = SongPlayer.GetCurrentSong();
                if (songId > -1) {
                    if (songRegistry.contains(songId)) {
                        Song song = songRegistry.getItem(songId);
                        // Set timestamp for total time
                        songTotalTime.setText(song.getLengthFormatted());
                        // Set timestamp for current time
                        songCurrentTime.setText(UsefulStuff.formatMilliseconds(Math.round(song.getLength() * progress)));
                    }
                    else{
                        Log.w("SongPlayer (fragment)", "Song not found in registry! Song id "+songId    );
                    }
                }

                seekbarUpdateHandler.postDelayed(this, 100);
            }
        });
        // Play/stop button
        playStopBtn = view.findViewById(R.id.songPlay_PlayStopBtn);
        playStopBtn.setSaveEnabled(false); // Toggle button will save state, so disable it

        // If there is a currently playing song, set checked to true
        updatePlayStopBtn();

        // Detect check changes on the toggle button. Add listener before setting checked to true, to prevent callback loop
        playStopBtn.setOnClickListener((v) -> {
            if (SongPlayer.IsReady()) {
                // if is checked, music has been paused
                if (playStopBtn.isChecked()) {
                    SongPlayer.Resume();
                } else {
                    SongPlayer.Pause();
                }
            } else {
                SongPlayer.PlaySong(0);
            }

        });

        return view;
    }

    private void updatePlayStopBtn() {
        Log.d("SongPlayer (fragment)", "Updating play/stop button");
        playStopBtn.setChecked(!SongPlayer.IsPaused() && SongPlayer.IsReady());
    }

    private void onSongLikeBtnClicked() {
        int currentSong = SongPlayer.GetCurrentSong();
        if (currentSong < 0) {
            return;
        }

        Log.d("SongPlay (fragment)", "Toggling liked for songid " + currentSong);
        if (!likedBtn.isChecked()) {
            Log.d("SongPlay (fragment)", "un-liked for songid " + currentSong);
            if (likedSongs.item.hasSong(currentSong)) {
                likedSongs.item.removeSong(currentSong);
            }
        } else {
            Log.d("SongPlay (fragment)", "liked for songid " + currentSong);
            if (!likedSongs.item.hasSong(currentSong)) {
                likedSongs.item.addSong(currentSong);
            }
        }
        updateLikedBtn();
    }

    private void updateLikedBtn() {
        int currentSong = SongPlayer.GetCurrentSong();
        boolean isLiked = likedSongs.item.hasSong(currentSong);
        Log.d("SongPlayer (fragment)", "Updating liked button. CurrentLiked: " + isLiked);
        if (currentSong < 0) {
            return;
        }
        likedBtn.setChecked(isLiked);
    }


    @Override
    public void onDestroy() {
        listenerGroup.unsubscribeAll();
        super.onDestroy();
    }
}