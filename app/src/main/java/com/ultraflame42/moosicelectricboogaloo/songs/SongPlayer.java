package com.ultraflame42.moosicelectricboogaloo.songs;

import java.util.ArrayList;

public class SongPlayer {
    // If -1, no playlist is playing.
    private static int currentPlaylist = -1;
    private static int currentSong = -1;

    // A copy of the current playlist., hence the name shadow
    private static ArrayList<Integer> currentPlaylistShadow = new ArrayList<Integer>();

}
