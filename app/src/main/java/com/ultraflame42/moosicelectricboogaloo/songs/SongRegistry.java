package com.ultraflame42.moosicelectricboogaloo.songs;

import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongRegistry extends Registry<Song> {
    private static SongRegistry instance = new SongRegistry();
    /**
     * This event is fired when the song registry wants to send a warning to ui
     */
    public CustomEvents<String> OnRegistryWarningsUI=new CustomEvents<>();

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

    @Override
    public void add(Song item) {
        item.OnSongVerified.addListener(data -> {
            if (!item.isPlayable()){
                OnRegistryWarningsUI.pushEvent("Warning. Song " + item.getTitle() +" unplayable." +
                        "\nThis may be due to:" +
                        "\n1. audio file is missing / broken" +
                        "\n2. The web link being invalid" +
                        "\n3. Network errors");
            }
            else{
                OnRegistryWarningsUI.pushEvent("Warning. Song " + item.getTitle() +" added successfully");
            }
            OnItemsUpdate.pushEvent(null);
        });
        item.updateAndRetrieveSongInfo();
        super.add(item);
    }
}
