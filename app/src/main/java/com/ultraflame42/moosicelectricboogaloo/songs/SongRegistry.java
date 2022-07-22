package com.ultraflame42.moosicelectricboogaloo.songs;

import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongRegistry{
    public static Registry<Song> songs = new Registry<>();
    public static Registry<SongPlaylist> playlists = new Registry<>();

    public static Set<Integer> favouritePlaylists = new HashSet<>();
    public static Set<Integer> likedSongs = new HashSet<>();

    private static SearchNameItem[] searhNamesCache = new SearchNameItem[0];

    public static SearchNameItem[] GetSearchNames() {
        int totalSize = 2 * (playlists.count() + songs.count()); // each for loop has 2 entries
        if (searhNamesCache.length != totalSize){
            searhNamesCache = new SearchNameItem[totalSize];
            int counter = 0;

            for (RegistryItem<SongPlaylist> item : playlists.getAllItems()) {

                searhNamesCache[counter] = new SearchNameItem(ResultItemType.PLAYLIST, item.item.getTitle(), item.id);
                searhNamesCache[counter+1] = new SearchNameItem(ResultItemType.PLAYLIST, item.item.getCreator(), item.id);
                counter+=2;
            }

            for (RegistryItem<Song> item : songs.getAllItems()) {

                searhNamesCache[counter] = new SearchNameItem(ResultItemType.SONG, item.item.getTitle(), item.id);
                searhNamesCache[counter+1] = new SearchNameItem(ResultItemType.SONG, item.item.getArtist(), item.id);
                counter+=2;
            }
        }
        return searhNamesCache;
    }

}
