package com.ultraflame42.moosicelectricboogaloo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.HashMap;
import java.util.List;

/**
 * 6
 * Main Interface to interact with stored data
 * Manages the download and retrieval of data from links.
 */
public class Storage {

    private static Storage instance;

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    /**
     * Returns a new instance of gson with the registered serializers and deserializers
     * @return
     */
    public static Gson getGson() {
        GsonBuilder g = new GsonBuilder();
        g.registerTypeAdapter(Song.class, new SongJsonAdapter());
        g.registerTypeAdapter(SongPlaylist.class, new PlaylistJsonAdapter());
        return g.create();
    }

    public void Save(Context ctx) {
        Log.i("Storage", "Saving data...");
        // get gson object
        Gson gson = getGson();

        // Get registry instances
        SongRegistry songRegistry = SongRegistry.getInstance();
        PlaylistRegistry playlistRegistry = PlaylistRegistry.getInstance();

        // Save general data like favorites, and liked songs
        SharedPreferences generalData = ctx.getSharedPreferences("general", Context.MODE_PRIVATE);
        SharedPreferences.Editor generalEditor = generalData.edit();
        // save favourites
        generalEditor.putString("favorites", gson.toJson(playlistRegistry.getFavourites()));
        generalEditor.putInt("songRegistryNextId", songRegistry.getNextId());
        generalEditor.putInt("playlistRegistryNextId", playlistRegistry.getNextId());
        generalEditor.apply();


        SaveSongRegistry(ctx);

        SavePlaylistRegistry(ctx);
    }

    private void SaveSongRegistry(Context ctx) {
        Gson gson = getGson();
        // get registry instance
        SongRegistry songRegistry = SongRegistry.getInstance();
        // get shared preference
        SharedPreferences songRegistryData = ctx.getSharedPreferences("songRegistry", Context.MODE_PRIVATE);
        // get the editor
        SharedPreferences.Editor songRegDataEditor = songRegistryData.edit();

        for (RegistryItem<Song> registeredSong : songRegistry.getAllItems()) {
            songRegDataEditor.putString(registeredSong.id + "", gson.toJson(registeredSong.item));
        }
        songRegDataEditor.apply();
    }


    private void SavePlaylistRegistry(Context ctx) {
        Gson gson = getGson();
        // get registry instance
        PlaylistRegistry playlistRegistry = PlaylistRegistry.getInstance();
        // get shared preference
        SharedPreferences playlistRegistryData = ctx.getSharedPreferences("playlistRegistry", Context.MODE_PRIVATE);
        // get the editor
        SharedPreferences.Editor playlistRegDataEditor = playlistRegistryData.edit();

        for (RegistryItem<SongPlaylist> registeredPlaylist : playlistRegistry.getAllItems()) {
            playlistRegDataEditor.putString(registeredPlaylist.id + "", gson.toJson(registeredPlaylist.item));
        }
        playlistRegDataEditor.apply();
    }

}
