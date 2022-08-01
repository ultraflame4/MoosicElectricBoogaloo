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
            instance = new SongRegistry();
        }
        return instance;
    }

    public SongRegistry() {
    }


    public static void LoadFromData(Storage.LoadedData data, Context ctx) {

        instance.idCounter = data.loadedNextSongId;
        data.songs.forEach((integer, song) -> {
            instance.verifySong(song,true, ctx);
            instance.items.put(integer, new RegistryItem<>(song, integer));
        });
    }

    // Cache search names for faster lookup
    private List<SearchNameItem> searchNamesCache = new ArrayList<>();

    public List<SearchNameItem> getSearchNames() {
        int totalSize = count() * 2;
        // Invalidate cache if size changed
        if (searchNamesCache.size() != totalSize) {
            searchNamesCache.clear();

            for (RegistryItem<Song> item : getAllItems()) {

                searchNamesCache.add(new SearchNameItem(ResultItemType.SONG, item.item.getTitle(), item.id));
                searchNamesCache.add(new SearchNameItem(ResultItemType.SONG, item.item.getArtist(), item.id));

            }
        }
        return searchNamesCache;
    }

    public RegistryItem<SongPlaylist> getLikedSongs() {
        return PlaylistRegistry.getInstance().get(0);
    }

    /**
     *
     * @param item
     * @param suppress supresses the info toast if true
     */
    public void add(Song item,boolean suppress) {
        if (homeContext == null) {
            Log.e("SongRegistry", "Context is null");
            throw new NullPointerException("Context is null");
        }
        verifySong(item, suppress, homeContext);
        super.add(item);
    }

    /**
     *  @param item
     * @param suppress Suppresses the info toast if true
     * @param ctx
     */
    private void verifySong(Song item, boolean suppress, Context ctx) {
        VerifyMediaPlayable.GetInfoAndVerifyMediaPlayable(ctx, item.getFileLink(), (playable, songLength) -> {
            if (!playable) {
                OnRegistryWarningsUI.pushEvent("Warning. Song " + item.getTitle() + " unplayable." +
                        "\nThis may be due to:" +
                        "\n1. audio file is missing / broken" +
                        "\n2. The web link being invalid" +
                        "\n3. Network errors");
            } else {
                item.setRuntimeInfo(playable, songLength);
                OnItemsUpdate.pushEvent(null);
                if (!suppress) {
                    OnRegistryWarningsUI.pushEvent(" Song " + item.getTitle() + " added successfully.");
                }
            }

        });
    }

    public void add(Song item) {
        add(item,false);
    }

    public void setHomeContext(Context homeContext) {
        this.homeContext = homeContext;
    }

    public void removeHomeContext() {
        this.homeContext = null;
    }

    /**
     * Warning:
     * Do not assign this value returned to any variable.
     * It will cause a memory leak
     */
    public Context getHomeContext() {
        return homeContext;
    }
}
