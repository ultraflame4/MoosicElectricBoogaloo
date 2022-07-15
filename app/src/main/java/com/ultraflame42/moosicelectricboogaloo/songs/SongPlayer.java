package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class SongPlayer {
    // If -1, no playlist is playing.
    private static int currentPlaylist = -1;
    private static int currentSong = -1;
    private static Context ctx;
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    // A copy of the current playlist., hence the name shadow
    private static ArrayList<Integer> currentPlaylistShadow = new ArrayList<Integer>();

    public static void PlaySong(int songId) {
        currentPlaylist = -1;

    }


}
