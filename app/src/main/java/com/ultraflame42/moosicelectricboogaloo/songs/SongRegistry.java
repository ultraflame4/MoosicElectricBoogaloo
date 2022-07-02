package com.ultraflame42.moosicelectricboogaloo.songs;

import java.util.HashMap;

public class SongRegistry {
    static HashMap<Integer, Song> songs = new HashMap<Integer, Song>();

    public static void registerSong(Song song) {
        songs.put(song.getId(), song);
    }

    public static Song getSong(int id) {
        return songs.get(id);
    }


}
