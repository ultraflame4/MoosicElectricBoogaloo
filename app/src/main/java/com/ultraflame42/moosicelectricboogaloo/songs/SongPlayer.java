package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

import java.io.IOException;
import java.util.ArrayList;

public class SongPlayer {
    // If -1, no playlist is playing.
    private static int currentPlaylist = -1;
    private static int currentSong = -1;
    private static Context ctx;
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    //events
    /**
     * This event fires when a new song starts playing
     */
    public static CustomEvents<RegisteredSong> OnSongPlayChange = new CustomEvents<>();


    // A copy of the current playlist., hence the name shadow
    private static ArrayList<Integer> currentPlaylistShadow = new ArrayList<Integer>();

    private boolean initialized = false;


    /**
     * For use internally to handle playing of a song.
     *
     * @param songId id of song to play
     */
    private static void playSong(int songId) {
        RegisteredSong song = SongRegistry.getSong(songId);
        try {
            mediaPlayer.reset();

            mediaPlayer.setDataSource(song.getFileLink());

            mediaPlayer.prepare();

            mediaPlayer.start();
            currentSong = songId;
            OnSongPlayChange.pushEvent(song);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Plays ONE song. To play a playlist, use PlayPlaylist()
     * May queue other songs to play after the current song.
     *
     * @param songId Id of song to play.
     */
    public static void PlaySong(int songId) {
        currentPlaylist = -1;
        playSong(songId);
    }


    public static void PlayPlaylist() {
        //todo
    }

    /**
     * Returns the currently playing song
     * @return
     */
    public static int getCurrentSong() {
        return currentSong;
    }

}
