package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.data.Storage;
import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.VerifyMediaPlayable;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongRegistry extends Registry<Song> {
    private static SongRegistry instance = null;
    /**
     * This event is fired when the song registry wants to send a warning to ui
     */
    public CustomEvents<String> OnRegistryWarningsUI = new CustomEvents<>();
    private Context homeContext;

    public static SongRegistry getInstance() {
        if (instance == null) {
            Log.e("SongRegistry", "SongRegistry has no instance.");
            throw new IllegalStateException("SongRegistry has no instance.");
        }
        return instance;
    }

    /**
     * Constructor for song registry. For prefilled values loaded from storage
     * @param songs HashMap of songs loaded from storage
     * @param idCounter idCounter to start from.
     * @param context context to be used for verifying media playable.
     */
    public SongRegistry(HashMap<Integer, Song> songs, int idCounter,Context context) {
        songs.forEach((integer, song) -> {
            verifySong(song,true,context);
            items.put(integer, new RegistryItem<>(song, integer));
        });
        this.idCounter = idCounter;

    }


    public static void LoadFromData(Storage.LoadedData data, Context ctx) {
        instance=new SongRegistry(data.getSongs(),data.loadedNextSongId,ctx);
    }

    // Cache search names for faster lookup
    private List<SearchNameItem> searchNamesCache = new ArrayList<>();

    /**
     * Returns a new list of searchable names for the songs in the registry.
     * @return
     */
    public List<SearchNameItem> getSearchNames() {
        int totalSize = count() * 2;
        // Invalidate cache if size changed
        if (searchNamesCache.size() != totalSize) {
            // if invalidate, clear cache
            searchNamesCache.clear();
            // For each item in the song registry
            for (RegistryItem<Song> item : getAllItems()) {
                // Add the song name and artist name to the search names cache
                searchNamesCache.add(new SearchNameItem(ResultItemType.SONG, item.item.getTitle(), item.id));
                searchNamesCache.add(new SearchNameItem(ResultItemType.SONG, item.item.getArtist(), item.id));

            }
        }
        return new ArrayList<>(searchNamesCache);
    }

    /**
     * Return the liked songs playlist
     * @return
     */
    public RegistryItem<SongPlaylist> getLikedSongs() {
        return PlaylistRegistry.getInstance().get(0);
    }

    /**
     *
     * @param item
     * @param suppress supresses the info toast if true
     */
    public void add(Song item,boolean suppress) {
        // check if home context is null
        if (homeContext == null) {
            Log.e("SongRegistry", "Context is null");
            throw new NullPointerException("Context is null");
        }
        // vertify song is playable
        verifySong(item, suppress, homeContext);
        super.add(item);
    }

    /**
     *  @param item
     * @param suppress Suppresses the info toast if true
     * @param ctx
     */
    private void verifySong(Song item, boolean suppress, Context ctx) {
        // Verity media playable
        VerifyMediaPlayable.GetInfoAndVerifyMediaPlayable(ctx, item.getFileLink(), (playable, songLength) -> {
            // on verified no playable, push out warning event
            if (!playable) {
                OnRegistryWarningsUI.pushEvent("Warning. Song " + item.getTitle() + " unplayable." +
                        "\nThis may be due to:" +
                        "\n1. audio file is missing / broken" +
                        "\n2. The web link being invalid" +
                        "\n3. Network errors");
            } else {
                // else, set the runtime info for the song
                item.setRuntimeInfo(playable, songLength);
                // push out registry item update
                OnItemsUpdate.pushEvent(null);
                if (!suppress) {
                    OnRegistryWarningsUI.pushEvent(" Song " + item.getTitle() + " added successfully.");
                }
            }

        });
    }

    public void add(Song item) {
        // shortcut to add with suppress
        add(item,false);
    }

    public void setHomeContext(Context homeContext) {
        // sets the home context
        this.homeContext = homeContext;
    }

    public void removeHomeContext() {
        // removes the home context to prevent memory leaks
        this.homeContext = null;
    }

    /**
     * Warning:
     * Do not assign this value returned to any variable.
     * It will cause a memory leak
     */
    public Context getHomeContext() {
        // returns the home context
        return homeContext;
    }
}
