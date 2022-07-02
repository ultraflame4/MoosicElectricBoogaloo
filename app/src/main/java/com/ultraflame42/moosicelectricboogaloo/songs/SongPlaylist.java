package com.ultraflame42.moosicelectricboogaloo.songs;

import java.util.ArrayList;

public class SongPlaylist {
    private ArrayList<Integer> songs = new ArrayList<Integer>();
    private String creator;
    private String title;
    private int totalLength=0; // in seconds

    public SongPlaylist(String creator, String title) {
        this.creator = creator;
        this.title = title;

    }

    public void addSong(int songId) {
        songs.add(songId);
        totalLength+=SongRegistry.getSong(songId).getLength();
    }
    public void removeSongAtIndex(int index) {
        songs.remove(index);
        totalLength-=SongRegistry.getSong(songs.get(index)).getLength();
    }

    /**
     * Returns the ids of all songs in this playlist.
     */
    public ArrayList<Integer> getSongs() {
        return songs;
    }

    public String getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }


    /**
     * Returns the total length of the playlist in seconds
     */
    public int getLength() {
        return totalLength;
    }

}
