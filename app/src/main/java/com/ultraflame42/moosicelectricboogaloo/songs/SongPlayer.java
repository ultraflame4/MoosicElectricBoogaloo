package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.media.MediaPlayer;

import com.ultraflame42.moosicelectricboogaloo.data.DataManager;

import java.io.IOException;
import java.util.ArrayList;

public class SongPlayer {
    // If -1, no playlist is playing.
    private static int currentPlaylist = -1;
    private static int currentSong = -1;
    private static Context ctx;
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    // A copy of the current playlist., hence the name shadow
    private static ArrayList<Integer> currentPlaylistShadow = new ArrayList<Integer>();

    private boolean initialized = false;


    /**
     * For use internally to handle playing of a song.
     *
     * @param songId id of song to play
     */
    private static void playSong(int songId) {
        Song song = SongRegistry.getSong(songId);
        try {
            mediaPlayer.reset();

            mediaPlayer.setDataSource(DataManager.getMediaLocation(song.getPlayable()));

            mediaPlayer.prepare();

            mediaPlayer.start();

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


}
