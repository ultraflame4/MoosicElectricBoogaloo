package com.ultraflame42.moosicelectricboogaloo.songs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongPlaylist {
    private List<Integer> songs = new ArrayList<>();
    private String creator;
    private String title;
    private final boolean isAlbum; // if true, this playlist is an album.

    private int totalLength=0; // in milliseconds

    /**
     *
     * @param creator Playlist creator name
     * @param title Playlis title
     * @param songs initial list of songs to add to the playlist
     */
    public SongPlaylist(String creator, String title,Integer[] songs) {
        this.creator = creator;
        this.title = title;
        this.songs= Arrays.asList(songs);
        isAlbum = false;
    }

    /**
     *
     * @param creator Playlist creator name
     * @param title Playlist title
     */
    public SongPlaylist(String creator, String title) {
        this.creator = creator;
        this.title = title;
        isAlbum = false;
    }

    /**
     *
     * @param creator Playlist creator name
     * @param title Playlist title
     * @param isAlbum True if this playlist is an album, false if it is a playlist.
     */
    public SongPlaylist(String creator, String title, boolean isAlbum) {
        this.creator = creator;
        this.title = title;
        this.isAlbum = isAlbum;
    }


    public void addSong(int songId) {
        songs.add(songId);
        totalLength+=SongRegistry.songs.get(songId).item.getLength();
    }
    public void removeSongAtIndex(int index) {
        songs.remove(index);
        totalLength-=SongRegistry.songs.get(songs.get(index)).item.getLength();
    }

    /**
     * Returns the ids of all songs in this playlist.
     */
    public List<Integer> getSongs() {
        return songs;
    }
    public int getSongCount() {
        return songs.size();
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
