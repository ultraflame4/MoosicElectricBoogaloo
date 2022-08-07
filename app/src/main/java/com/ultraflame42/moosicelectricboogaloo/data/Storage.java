package com.ultraflame42.moosicelectricboogaloo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Main Interface to interact with stored data
 * Manages the download and retrieval of data from links.
 */
public class Storage {

    private static Storage instance;

    private static final String IMAGE_DOWNLOAD_DIRECTORY = "ImageDownloads";
    public static final String SONG_DOWNLOAD_DIRECTORY = "SongsDownloads";

    public static Storage getInstance() {
        // create instance if null
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    /**
     * Returns a new instance of gson with the registered serializers and deserializers
     *
     * @return
     */
    public static Gson getGson() {
        // Craete the gson object used to serialise and deserialize
        GsonBuilder g = new GsonBuilder();
        // Add the typeAdapters
        g.registerTypeAdapter(Song.class, new SongJsonAdapter());
        g.registerTypeAdapter(SongPlaylist.class, new PlaylistJsonAdapter());
        // return the gson object
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
        // Save id counters for the registries
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
        // For each song in the registry, serialise it to json and save it to shared preferences
        for (RegistryItem<Song> registeredSong : songRegistry.getAllItems()) {
            songRegDataEditor.putString(registeredSong.id + "", gson.toJson(registeredSong.item));
        }
        // apply changes
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
        // For each playlist in the registry, serialise it to json and save it to shared preferences
        for (RegistryItem<SongPlaylist> registeredPlaylist : playlistRegistry.getAllItems()) {
            playlistRegDataEditor.putString(registeredPlaylist.id + "", gson.toJson(registeredPlaylist.item));
        }
        // apply changes
        playlistRegDataEditor.apply();
    }

    /**
     * utility class to carry loaded data from shared preferences
     */
    public static class LoadedData {
        public final int loadedNextSongId;
        public final int loadedNextPlaylistId;

        public final HashSet<Double> favorites;

        private final SharedPreferences songRegistryData;
        private final SharedPreferences playlistRegistryData;


        private final Gson gson;

        public LoadedData(Context ctx) {
            gson = getGson();
            SharedPreferences generalData = ctx.getSharedPreferences("general", Context.MODE_PRIVATE);
            songRegistryData = ctx.getSharedPreferences("songRegistry", Context.MODE_PRIVATE);
            playlistRegistryData = ctx.getSharedPreferences("playlistRegistry", Context.MODE_PRIVATE);

            // load general stuff
            favorites = gson.fromJson(generalData.getString("favorites", "[]"), HashSet.class);
            loadedNextSongId = generalData.getInt("songRegistryNextId", 0);
            loadedNextPlaylistId = generalData.getInt("playlistRegistryNextId", 0);

        }

        public HashMap<Integer, Song> getSongs() {
            Log.i("Storage", "Loading songs from shared preferences...");

            HashMap<Integer, Song> songs = new HashMap<>();
            songRegistryData.getAll().forEach((key, value) -> {
                // get json data with empty string as default value
                String json = songRegistryData.getString(key, "");
                if (json.length() < 1) { // if string is empty skip
                    return;
                }
                // parse json data to song object and add to hashmap
                songs.put(Integer.parseInt(key), gson.fromJson(json, Song.class));
            });

            return songs;
        }

        public HashMap<Integer, SongPlaylist> getPlaylists() {
            HashMap<Integer, SongPlaylist> playlists = new HashMap<>();

            playlistRegistryData.getAll().forEach((key, value) -> {
                // get json data with empty string as default value
                String json = playlistRegistryData.getString(key, "");
                if (json.length() < 1) { // if string is empty skip
                    return;
                }
                // parse json data to song playlist object and add to hashmap
                SongPlaylist playlist = gson.fromJson(json, SongPlaylist.class);
                int id = Integer.parseInt(key);
                if (id == 0) { // if id is zer0. it is the liked songs playlist
                    playlist.isSystem = true;
                }
                playlists.put(id, playlist);
            });

            return playlists;
        }

    }

    public LoadedData Load(Context ctx) {
        return new LoadedData(ctx);
    }

    public String DownloadLocalSong(Context ctx, String uriString) {
        return DownloadLocalFile(ctx,uriString,Storage.SONG_DOWNLOAD_DIRECTORY);
    }


    public String DownloadLocalImage(Context context, String imageUriLink) {
        return DownloadLocalFile(context,imageUriLink,Storage.IMAGE_DOWNLOAD_DIRECTORY);
    }

    /**
     * Copies the local file to the external app-specific directory.
     * So that the app can access it in the future.
     *
     * @param ctx       context
     * @param uriString uri string of the file to copy the uri should be a content uri
     * @return a uri string containing the file's copy uri
     */
    public String DownloadLocalFile(Context ctx, String uriString,String directory) {
        Uri uri = Uri.parse(uriString);
        String fileName = UsefulStuff.getFileName(ctx, uri);
        Log.d("Storage", "Downloading file: " + fileName + " uri: " + uriString);
        // first make the file
        File songDir = new File(ctx.getFilesDir(), directory);
        songDir.mkdirs();

        // then get the file object used for copy
        File fileCopy = new File(
                ctx.getFilesDir() + "/" + directory,
                fileName
        );

        try {
            fileCopy.createNewFile();
        } catch (IOException err) {
            Log.e("Storage", "Could not create file: " + fileName, err);
            return "";
        }

        InputStream originalFileInputStream;
        try {
            originalFileInputStream = ctx.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.d("Storage", "File not found: " + uriString);
            return "";
        }
        InputStreamReader originalInputStream = new InputStreamReader(originalFileInputStream);

        // modified from https://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android

        try (OutputStream out = new FileOutputStream(fileCopy)) {
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = originalFileInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return fileCopy.toURI().toString();
    }

}
