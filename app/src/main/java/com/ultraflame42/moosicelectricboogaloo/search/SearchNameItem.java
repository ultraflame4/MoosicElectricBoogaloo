package com.ultraflame42.moosicelectricboogaloo.search;

import android.util.Log;

/**
 * This class represent the various names that can be searched for.
 *
 * Each instance will have a "target registry id" that points to a song or playlist.
 *
 */
public class SearchNameItem {
    public final ResultItemType type;
    public final String name;
    public final int targetRegId; // either pointer to song or playlist registry id

    public SearchNameItem(ResultItemType type, String name, int targetRegistryId) {
        this.type = type;
        this.name = name;
        this.targetRegId = targetRegistryId;
    }

    public boolean IsPlaylist() {
        return type == ResultItemType.PLAYLIST;
   }
    public boolean IsSong() {
        return type == ResultItemType.SONG;
    }

    /**
     * Use internally by the search activity.
     * @return Returns a string representation of the search item target registry id & type combined.
     *
     * Ensures that a song and playlist with the same name will show up as different results.
     */
    public String getUniqueFilterTargetString() {
        return type.ordinal() + ":" + targetRegId;
    }

}
