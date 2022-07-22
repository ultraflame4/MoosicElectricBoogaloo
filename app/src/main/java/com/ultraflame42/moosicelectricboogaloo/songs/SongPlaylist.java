package com.ultraflame42.moosicelectricboogaloo.songs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongPlaylist {
    private List<Integer> songs = new ArrayList<>();
    private String creator;
    private String title;
    private final boolean isAlbum; // if true, this playlist is an album.
    /**
     * if true, this playlist is created by the system and is should not be deletable by the user. eg. Liked Songs
     */
    public boolean isSystem = false;

    private int totalLength = 0; // in milliseconds

    SongRegistry songRegistry;

    /**
     * @param creator Playlist creator name
     * @param title   Playlis title
     * @param songs   initial list of songs to add to the playlist
     */
    public SongPlaylist(String creator, String title, Integer[] songs) {
        this.creator = creator;
        this.title = title;
        this.songs = Arrays.asList(songs);
        songRegistry = SongRegistry.getInstance();
        isAlbum = false;

        this.songs.forEach(integer -> {
            totalLength += songRegistry.get(integer).item.getLength();
        });
    }

    /**
     * @param creator Playlist creator name
     * @param title   Playlist title
     */
    public SongPlaylist(String creator, String title) {
        this.creator = creator;
        this.title = title;
        isAlbum = false;
        songRegistry = SongRegistry.getInstance();

    }

    /**
     * @param creator Playlist creator name
     * @param title   Playlist title
     * @param isAlbum True if this playlist is an album, false if it is a playlist.
     */
    public SongPlaylist(String creator, String title, boolean isAlbum) {
        this.creator = creator;
        this.title = title;
        this.isAlbum = isAlbum;
    }

    public void addSong(int songId) {
        songs.add(songId);
        // add to total Length
        totalLength += songRegistry.get(songId).item.getLength();
    }

    public void removeSongAtIndex(int index) {
        // remove from total Length
        totalLength -= songRegistry.get(songs.get(index)).item.getLength();
        // do this part aft else the index will be out of bounds.
        songs.remove(index);
    }

    public void removeSong(int songId) {
        removeSongAtIndex(songs.indexOf(songId));
    }

    public boolean hasSong(int songId) {
        return songs.contains(songId);
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
