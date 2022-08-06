package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.ui.others.PlaylistActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * A utility class for searching through the registries for items that match a query.
 */
public class SearchTool {
    //todo migrate alot of functionaility from the search adapter to here
    private final ArrayList<SearchNameItem> _recentSearches = new ArrayList<>();
    public static final int MAX_RECENT_SEARCHES = 4;

    private static SearchTool instance;

    public static SearchTool getInstance() {
        if (instance == null) {
            instance = new SearchTool();
        }
        return instance;
    }

    public SearchNameItem[] getRecentSearches() {
        ArrayList<SearchNameItem> copy = new ArrayList<SearchNameItem>(_recentSearches);
        Collections.reverse(copy);
        return copy.toArray(new SearchNameItem[0]);
    }


    public void handleOnSearchItemSelected(SearchNameItem item, Context ctx) {
        handleOnSearchItemSelected(item.type.ordinal(), item.targetRegId, ctx);
    }

    public void handleOnSearchItemSelected(int itemType, int itemId, Context ctx) {

        if (itemType < 0 || itemId < 0) {
            Log.w("LibraryFragment", "WARNING Search activity returned invalid data");
            return;
        }

        if (itemType == ResultItemType.SONG.ordinal()) {
            SongPlayer.PlaySong(itemId);
        } else if (itemType == ResultItemType.PLAYLIST.ordinal()) {
            // playlist
            Log.d("SearchTool", "Opening playlist " + itemId);
            Intent intent = new Intent(ctx, PlaylistActivity.class);
            intent.putExtra("playlistId", itemId);
            ctx.startActivity(intent);
        }

    }


    public void RemoveDuplicateSearchNames(List<SearchNameItem> names) {
        HashSet<String> seen=new HashSet<>();
        names.removeIf(e->!seen.add(e.getUniqueFilterTargetString()));
    }

    public void addToRecentSearch(SearchNameItem item) {
        if (_recentSearches.size() >= MAX_RECENT_SEARCHES+1) {
            _recentSearches.remove(0);
        }

        _recentSearches.add(item);
    }
}
