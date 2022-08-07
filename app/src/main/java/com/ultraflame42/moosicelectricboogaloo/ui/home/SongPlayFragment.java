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

import com.google.android.material.imageview.ShapeableImageView;
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

import javax.annotation.Nullable;

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
    private TextView songCurrentTime;
    private TextView songTotalTime;
    private SongRegistry songRegistry;
    private ToggleButton shuffleBtn;
    private ToggleButton loopBtn;
    private ShapeableImageView songImage;

    private TextView songTitleView;
    private TextView songArtistView;

    public SongPlayFragment() {

        songRegistry = SongRegistry.getInstance();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistRegistry = PlaylistRegistry.getInstance();


    }

    public void updateSongInfo(@Nullable Song song) {
        Log.d("SongPlayer (fragment)", "Updating song information. is null? " + (song == null));
        if (song == null) {
            songTitleView.setText(getString(R.string.song_play_title_default));
            songArtistView.setText(getString(R.string.song_play_artist_default));
            songTotalTime.setText("0:0");
            UsefulStuff.LoadImageUriIntoImageView("",songImage);
            return;
        }
        songTotalTime.setText(song.getLengthFormatted());
        songTitleView.setText(song.getTitle());
        songArtistView.setText(song.getArtist());
        UsefulStuff.LoadImageUriIntoImageView(song.getImageUriLink(),songImage);

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
        // ----- Get the views and assign them to the respective variables -----
        // Song information-----
        songTitleView = view.findViewById(R.id.songPlayTitle);
        songArtistView = view.findViewById(R.id.songPlayArtist);
        songImage = view.findViewById(R.id.songImage);
        songCurrentTime = view.findViewById(R.id.songTimeCurrent);
        songTotalTime = view.findViewById(R.id.songTimeTotal);
        // Playback and other controls-----
        likedBtn = view.findViewById(R.id.likeSongBtn);
        skipBtn = view.findViewById(R.id.songPlay_SkipBtn);
        prevBtn = view.findViewById(R.id.songPlay_PrevBtn);

        // Playback controls-----
        // song liked button
        // disable save state cuz it caused alot of problems
        likedBtn.setSaveEnabled(false);
        likedBtn.setOnClickListener((v) -> {
            onSongLikeBtnClicked();
        });

        skipBtn.setOnClickListener(v -> {
            SongPlayer.PlayNext();
        });

        prevBtn.setOnClickListener(v -> {
            SongPlayer.PlayPrev();
        });

        // Set when SongPlayer PlayState changes listener
        listenerGroup.subscribe(SongPlayer.OnSongPlayStateChange, song -> {
            updatePlayStopBtn();
        });


        // get the current song
        int currentSong = SongPlayer.GetCurrentSong();

        if (currentSong >= 0) {
            // if current song not -1 , then update the song info
            updateSongInfo(SongRegistry.getInstance().getItem(currentSong));
        }

        // add on play change listener so that it updates whenever the song changes
        listenerGroup.subscribe(SongPlayer.OnSongPlayChange, regItem -> {
            if (regItem!=null) {
                // update to the next song information
                updateSongInfo(regItem.item);
            }
            else{
                // the no next song, update to the default song info
                updateSongInfo(null);
            }
        });


        // Song progress bar
        seekBar = view.findViewById(R.id.songPlaySeekBar);

        // set the seekbar progress change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // when seekbar is being dragged
                if (fromUser) {
                    // if change is by user, then skip ahead to the new position/time in SongPlayer
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

        // The set "resolution" of the seekbar. the higher the better because it looks more smoot
        seekBar.setMax(seekBarProgressResolution);

        // Update progress bar every half a second.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // When scrubbing, stop updating the progress bar
                float progress = SongPlayer.GetCurrentSongProgress();
                if (!isScrubbing) {
                    // When not scrubbing, update the progress bar
                    seekBar.setProgress(Math.round(progress * seekBarProgressResolution));
                }
                // get current song id
                int songId = SongPlayer.GetCurrentSong();
                // if current song is not -1, then update time stamp
                if (songId > -1) {
                    // Check if registry contains the song
                    if (songRegistry.contains(songId)) {
                        Song song = songRegistry.getItem(songId);

                        // Set timestamp for current time
                        songCurrentTime.setText(UsefulStuff.formatMilliseconds(Math.round(song.getLength() * progress)));
                    } else {
                        Log.w("SongPlayer (fragment)", "Song not found in registry! Song id " + songId);
                    }
                }
                // Set to run this every 100ms
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

        // Get shuffle and loop btn views
        shuffleBtn = view.findViewById(R.id.shuffleBtn);
        loopBtn = view.findViewById(R.id.loopBtn);

        // Disable save because they are toggle buttons
        shuffleBtn.setSaveEnabled(false);
        loopBtn.setSaveEnabled(false);

        // Update shuffle btn state
        updateShuffleBtn();
        // Set listener for shuffle btn
        shuffleBtn.setOnClickListener((v) -> {
            // set shuffle state
            SongPlayer.SetShuffle(shuffleBtn.isChecked());
            Log.d("SongPlayer (fragment)", "Shuffle set to " + SongPlayer.IsShuffle());
        });

        // Update loop btn state
        updateLoopBtn();
        // Set listener for loop btn
        loopBtn.setOnClickListener((v) -> {
            // set loop state
            SongPlayer.SetLooping(loopBtn.isChecked());
            Log.d("SongPlayer (fragment)", "Loop set to " + SongPlayer.IsLooping());
        });

        return view;
    }

    private void updatePlayStopBtn() {
        // Set the play/stop button to correct state
        Log.d("SongPlayer (fragment)", "Updating play/stop button");
        playStopBtn.setChecked(!SongPlayer.IsPaused() && SongPlayer.IsReady());
    }

    private void onSongLikeBtnClicked() {
        // On song toggle liked
        // get the song
        int currentSong = SongPlayer.GetCurrentSong();
        // if song is in registry, then continue
        if (currentSong < 0) {
            return;
        }
        // get liked songs playlist
        RegistryItem<SongPlaylist> likedSongs = playlistRegistry.get(0);

        if (!likedBtn.isChecked()) {
            // if not checked, then remove to liked songs
            Log.d("SongPlay (fragment)", "setting liked=false for songid " + currentSong);
            // check if in likedSongs first
            if (likedSongs.item.hasSong(currentSong)) {
                likedSongs.item.removeSong(currentSong);
            }
        } else {
            // if not checked, then add to liked songs
            Log.d("SongPlay (fragment)", "setting liked=true for songid " + currentSong);
            // check if alr in likedSongs
            if (!likedSongs.item.hasSong(currentSong)) {
                likedSongs.item.addSong(currentSong);
            }
        }
        // update the btn
        updateLikedBtn();
    }

    private void updateLikedBtn() {
        // get the current song
        int currentSong = SongPlayer.GetCurrentSong();
        // check if current song exists
        if (currentSong < 0) {
            return;
        }
        // check if song is in liked Songs
        boolean isLiked = playlistRegistry.get(0).item.hasSong(currentSong);
        Log.d("SongPlayer (fragment)", "Updating liked button. CurrentLiked: " + isLiked);
        // if is liked , then set checked to true
        likedBtn.setChecked(isLiked);
    }

    private void updateShuffleBtn() {
        // If shuffling, set to checked
        shuffleBtn.setChecked(SongPlayer.IsShuffle());
    }
    private void updateLoopBtn() {
        // If looping, set to checked
        loopBtn.setChecked(SongPlayer.IsLooping());
    }



    @Override
    public void onDestroy() {
        //  Prevent memory leaks by removing all listeners
        listenerGroup.unsubscribeAll();
        super.onDestroy();
    }
}