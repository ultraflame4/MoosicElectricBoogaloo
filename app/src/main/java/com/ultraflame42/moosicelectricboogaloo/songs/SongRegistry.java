package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.res.Resources;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;

import java.util.HashMap;

public class SongRegistry {
    static HashMap<Integer, RegisteredSong> songs = new HashMap<>();
    public static CustomEvents<RegisteredSong> OnSongAdded = new CustomEvents<>();
    /**
     * This event fires when the Song cannot be found. This can happen if the song is deleted or the song is not in the song registry.
     */
    public static DefaultEvent OnSongNotFound = new DefaultEvent();

    private static int idCounter = 0; //todo start id count from the one storage

    public static void registerSong(Song song) {
        RegisteredSong rSong = new RegisteredSong(idCounter, song);
        songs.put(SongRegistry.idCounter, rSong);
        idCounter++;
    }

    public static RegisteredSong getSong(int id) throws Resources.NotFoundException {
        if (!songs.containsKey(id)) {
            OnSongNotFound.pushEvent(null);
            throw new Resources.NotFoundException("Song not found");
        }
        return songs.get(id);
    }

    public static RegisteredSong[] getAllSongs() {
        return songs.values().toArray(new RegisteredSong[0]);
    }


}
