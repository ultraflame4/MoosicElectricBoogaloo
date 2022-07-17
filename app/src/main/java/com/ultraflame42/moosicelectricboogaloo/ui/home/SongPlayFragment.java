package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongPlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongPlayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EventListenerGroup listenerGroup = new EventListenerGroup();

    public SongPlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongPlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongPlayFragment newInstance(String param1, String param2) {
        SongPlayFragment fragment = new SongPlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView songTitleView;
    TextView songArtistView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    public void updateSongInfo(Song song) {
        Log.d("SongPlayer (fragment)", "Updating song information");
        songTitleView.setText(song.getTitle());
        songArtistView.setText(song.getArtist());

    }


    private Handler seekbarUpdateHandler = new Handler();
    private SeekBar seekBar;
    private ToggleButton playStopBtn;
    private boolean isScrubbing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_play, container, false);
        songTitleView = view.findViewById(R.id.songPlayTitle);
        songArtistView = view.findViewById(R.id.songPlayArtist);

        int currentSong = SongPlayer.GetCurrentSong();

        if (currentSong >= 0) {
            updateSongInfo(SongRegistry.getSong(currentSong));
        }

        listenerGroup.subscribe(SongPlayer.OnSongPlayChange, song -> {
            updateSongInfo(song);
        });

        // Song progress bar
        seekBar = view.findViewById(R.id.songPlaySeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    SongPlayer.SetCurrentSongProgress(progress / 100f);
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

        // Update progress bar every half a second
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // When scrubbing, stop updating the progress bar
                if (!isScrubbing) {
                    seekBar.setProgress(Math.round(SongPlayer.GetCurrentSongProgress() * 100));
                }
                seekbarUpdateHandler.postDelayed(this, 500);
            }
        });
        // Play/stop button
        playStopBtn = view.findViewById(R.id.songPlay_PlayStopBtn);
        playStopBtn.setSaveEnabled(false); // Toggle button will save state, so disable it

        // If there is a currently playing song, set checked to true
        playStopBtn.setChecked(!SongPlayer.IsPaused() && SongPlayer.IsReady());

        // Detect check changes on the toggle button. Add listener before setting checked to true, to prevent callback loop
        playStopBtn.setOnCheckedChangeListener((compoundButton,isChecked) -> {
            if (SongPlayer.IsReady()) {
                // if is checked, music has been paused
                if (isChecked) {
                    SongPlayer.Resume();
                } else {
                    SongPlayer.Pause();
                }
            }
            else {
                SongPlayer.PlaySong(0);
            }

        });


        return view;
    }

    @Override
    public void onDestroy() {
        listenerGroup.unsubscribeAll();
        super.onDestroy();
    }
}