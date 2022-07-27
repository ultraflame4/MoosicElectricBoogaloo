package com.ultraflame42.moosicelectricboogaloo.search;

import android.util.Log;

/**
 * This class represent the various names that can be searched for.
 *
 * Each instance will have a "target registry id" that points to a song or playlist.
 *
 */
public class SearchNameItem {
    /**
     * The type of item this is. isit a Song or playlist?
     */
    public final ResultItemType type;
    /**
     * Name of the item. This is what the search algorithm in the search adapter will use for searching.
     */
    public final String name;
    public final int targetRegId; // either pointer to song or playlist registry id

    public SearchNameItem(ResultItemType type, String name, int targetRegistryId) {
        this.type = type;
        this.name = name;
        this.targetRegId = targetRegistryId;
    }

    // Returns true if name points to a playlist
    public boolean IsPlaylist() {
        return type == ResultItemType.PLAYLIST;
   }
    // Returns true if name points to a song
    public boolean IsSong() {
        return type == ResultItemType.SONG;
    }

    /**
     * Use internally by the search activity.
     * @return Returns a string representation of the search item target registry id & type combined.
     *
     * Ensures that a song and playlist with the same name will show up as different results and not be mistaken as duplicates
     */
    public String getUniqueFilterTargetString() {
        return type.ordinal() + ":" + targetRegId;
    }

}
