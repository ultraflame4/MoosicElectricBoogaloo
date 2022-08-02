package com.ultraflame42.moosicelectricboogaloo.songs;

import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.data.Storage;
import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistRegistry extends Registry<SongPlaylist> {
    public static final int LikedSongsPlaylistID = 0;

    private static PlaylistRegistry instance = null;

    // HashSet to store favourites playlists.
    private HashSet<Integer> favourites = new HashSet<>();
    // Event to be fired when a playlist is added or removed favourites.
    public DefaultEvent OnFavouritesUpdate = new DefaultEvent();

    // Singleton pattern
    public static PlaylistRegistry getInstance() {
        if (instance == null) {
            Log.e("PlaylistRegistry", "PlaylistRegistry has no instance");
            throw new IllegalStateException("PlaylistRegistry has no instance");
        }

        return instance;
    }


    /**
     * Constructor for playlist registry. For prefilled values loaded from storage
     * @param favourites HashSet of favourites playlists
     * @param playlists HashMap of playlists
     * @param idCounter idCounter to start from.
     */
    public PlaylistRegistry(HashSet<Double> favourites, HashMap<Integer, SongPlaylist> playlists, int idCounter) {
        favourites.forEach(aDouble -> {
            // have to do this cuz for some reason gson deserializes json to hashset<Double>
            this.favourites.add((int) Math.round(aDouble));
        });

        playlists.forEach((id, playlist) -> {
            RegistryItem<SongPlaylist> regItem = new RegistryItem<>(playlist, id);
            if (id == LikedSongsPlaylistID) { // if id is zero, it is liked songs playlist. set system to true
                regItem.item.isSystem = true;
            }
            this.items.put(id, regItem);
        });

        this.idCounter=idCounter;

        // check if there is a liked song playlist. if there isnt, create one
        if (!items.containsKey(LikedSongsPlaylistID)){
            Log.d("PlaylistRegistry", "Liked songs playlist does not exist. Creating one...");
            SongPlaylist pl = new SongPlaylist("-", "Liked Songs");
            pl.isSystem = true;
            items.put(LikedSongsPlaylistID, new RegistryItem<>(pl, LikedSongsPlaylistID));
            this.favourites.add(LikedSongsPlaylistID);
            this.idCounter++;
        }
    }


    public static void LoadFromData(Storage.LoadedData data) {
        instance = new PlaylistRegistry(data.favorites, data.getPlaylists(), data.loadedNextPlaylistId);

    }

    // Cache so we do not need to recreate the list of search names every time it used
    private List<SearchNameItem> searchNamesCache = new ArrayList<>();

    /**
     * Returns a list of SearchNameItems for use in the SearchResultsAdapter
     * @return
     */
    public List<SearchNameItem> getSearchNames() {
        int totalSize = count() * 2;
        // Invalidate cache if the total items is more than the names in the cache
        if (totalSize != searchNamesCache.size()) {
            // clear cache
            searchNamesCache.clear();
            // regenerate the cache
            for (RegistryItem<SongPlaylist> item : getAllItems()) {
                // Name of playlist so tat it shows up in the search results
                searchNamesCache.add(new SearchNameItem(ResultItemType.PLAYLIST, item.item.getTitle(), item.id));
                // Creator of playlist so tat it shows up in the search results
                searchNamesCache.add(new SearchNameItem(ResultItemType.PLAYLIST, item.item.getCreator(), item.id));
            }
        }
        // return cache. invalid cache would have been caught and regenerated above.
        return searchNamesCache;
    }

    /**
     * Remove a playlist from the registry
     *
     * Note: playlist with attribute isSystem = true will not be removed because it is a system playlist created by the app
     * @param itemId
     */
    @Override
    public void remove(int itemId) {
        // get playlist from registry
        SongPlaylist s = get(itemId).item;
        // Check if system
        if (s.isSystem) {
            Log.w("PlaylistRegistry", "Tried to remove system playlist id: " + itemId + " name: " + s.getTitle() + " creator:" + s.getCreator());
            return;
        }
        super.remove(itemId);
    }

    // Get the favourite playlists
    public HashSet<Integer> getFavourites() {
        // clone it so we don't modify the original
        return (HashSet<Integer>) favourites.clone();
    }
    // Add playlist to favourites
    public void addToFavourites(int playlistId) {
        favourites.add(playlistId);
        // push favourite update event
        OnFavouritesUpdate.pushEvent(null);
    }
    // Remove playlist from favourites
    public void removeFromFavourites(int playlistId) {
        favourites.remove(playlistId);
        // push favourite update event
        OnFavouritesUpdate.pushEvent(null);
    }
    // Check if playlist is in favourites
    public boolean favouritesHas(int playlistId) {
        return favourites.contains(playlistId);
    }

    @Override
    public void add(SongPlaylist item) {
        Log.i("PlaylistRegistry", "Adding playlist: " + item.getTitle() + " with id " + idCounter);
        super.add(item);
    }
}
