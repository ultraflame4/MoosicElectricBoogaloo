package com.ultraflame42.moosicelectricboogaloo.songs;

import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistRegistry extends Registry<SongPlaylist> {
    private static PlaylistRegistry instance = null;

    private HashSet<Integer> favourites = new HashSet<>();
    public DefaultEvent OnFavouritesUpdate = new DefaultEvent();

    // Singleton pattern
    public static PlaylistRegistry getInstance() {
        if (instance == null) {
            instance = new PlaylistRegistry();
            // Liked songs will be stored as playlist
            SongPlaylist pl = new SongPlaylist("-", "Liked Songs");
            pl.isSystem = true;
            instance.add(pl);
            instance.addToFavourites(0);
        }
        return instance;
    }

    private List<SearchNameItem> searchNamesCache = new ArrayList<>();

    public List<SearchNameItem> getSearchNames() {
        int totalSize = count() * 2;
        if (totalSize != searchNamesCache.size()) {
            searchNamesCache.clear();
            for (RegistryItem<SongPlaylist> item : getAllItems()) {

                searchNamesCache.add(new SearchNameItem(ResultItemType.PLAYLIST, item.item.getTitle(), item.id));
                searchNamesCache.add(new SearchNameItem(ResultItemType.PLAYLIST, item.item.getCreator(), item.id));
            }
        }
        return searchNamesCache;
    }

    @Override
    public void remove(int itemId) {
        SongPlaylist s = get(itemId).item;
        if (s.isSystem) {
            Log.w("PlaylistRegistry", "Tried to remove system playlist id: " + itemId + " name: " + s.getTitle() + " creator:" + s.getCreator());
            return;
        }
        super.remove(itemId);
    }

    public HashSet<Integer> getFavourites() {
        return (HashSet<Integer>) favourites.clone();
    }

    public void addToFavourites(int playlistId) {
        favourites.add(playlistId);
        OnFavouritesUpdate.pushEvent(null);
    }
    public void removeFromFavourites(int playlistId) {
        favourites.remove(playlistId);
        OnFavouritesUpdate.pushEvent(null);
    }

    public boolean favouritesHas(int playlistId) {
        return favourites.contains(playlistId);
    }
}
