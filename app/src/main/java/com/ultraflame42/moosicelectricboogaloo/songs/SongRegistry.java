package com.ultraflame42.moosicelectricboogaloo.songs;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

import java.util.HashMap;

public class SongRegistry {
    static HashMap<Integer, RegisteredSong> songs = new HashMap<>();
    public static CustomEvents<RegisteredSong> OnSongAdded = new CustomEvents<>();
    private static int idCounter = 0; //todo start id count from the one storage

    public static void registerSong(Song song) {
        RegisteredSong rSong = new RegisteredSong(idCounter, song);
        songs.put(SongRegistry.idCounter, rSong);
        idCounter++;
    }

    public static RegisteredSong getSong(int id) {
        return songs.get(id);
    }

    public static RegisteredSong[] getAllSongs() {
        return songs.values().toArray(new RegisteredSong[0]);
    }


}
