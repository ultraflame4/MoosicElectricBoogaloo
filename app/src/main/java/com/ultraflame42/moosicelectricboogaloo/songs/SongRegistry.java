package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.VerifyMediaPlayable;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.List;

public class SongRegistry extends Registry<Song> {
    private static SongRegistry instance = new SongRegistry();
    /**
     * This event is fired when the song registry wants to send a warning to ui
     */
    public CustomEvents<String> OnRegistryWarningsUI=new CustomEvents<>();
    private Context homeContext;

    public static SongRegistry getInstance() {
        return instance;
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


    public void add(Song item) {
        if (homeContext == null) {
            Log.e("SongRegistry", "Context is null");
            throw new NullPointerException("Context is null");
        }

        VerifyMediaPlayable.GetInfoAndVerifyMediaPlayable(homeContext, item.getFileLink(),(playable, songLength) -> {
            if (!playable) {
                OnRegistryWarningsUI.pushEvent("Warning. Song " + item.getTitle() +" unplayable." +
                        "\nThis may be due to:" +
                        "\n1. audio file is missing / broken" +
                        "\n2. The web link being invalid" +
                        "\n3. Network errors");
            }
            else{
                item.setRuntimeInfo(playable, songLength);
                OnItemsUpdate.pushEvent(null);
                OnRegistryWarningsUI.pushEvent(" Song " + item.getTitle() +" added successfully.");
            }

        });

        super.add(item);
    }

    public void setHomeContext(Context homeContext) {
        this.homeContext = homeContext;
    }

    public void removeHomeContext() {
        this.homeContext= null;
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
