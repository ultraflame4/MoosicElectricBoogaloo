package com.ultraflame42.moosicelectricboogaloo.songs;


import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Song {


    private int length = -1; // In milliseconds
    private String title;
    private String artist;
    private String album;
    private String fileLink; //todo change to id registry system for fileLink.
    private String[] tags;

    /**
     * Constructor for a song.
     * @param title Song title
     * @param artist Song artist
     * @param fileLink Song file link
     */
    public Song(String title, String artist, String fileLink) {
        this.title = title;
        this.artist = artist;
        this.album= title;
        this.fileLink = fileLink;
    }

    /**
     * Constructor for a song.
     * @param title Song title
     * @param artist Song artist
     * @param album Album song belongs to.
     * @param fileLink Song file link
     */
    public Song(String title, String artist, String album, String fileLink) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.fileLink = fileLink;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void addTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * Caculates length of song using media player
     * @return Returns length of song in milliseconds
     */
    public int getLength() {
        if (length == -1) {
            try {
                length = UsefulStuff.getSongDuration(this);
            } catch (IOException e) {
                Log.e("SongGetLength", "Fatal Error: Could not calculate song length: Mediaplayer Error", e);
            }
        }
        return length;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String[] getTags() {
        return tags;
    }

    public String getFileLink() {
        return fileLink;
    }

    /**
     * Returns formatted length of song in format minutes:seconds
     *
     * @return
     */
    private String formattedLength = "";
    public String getLengthFormatted() {
        if (formattedLength.equals("")) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(getLength());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(getLength());
            formattedLength = minutes + " : " + seconds;
        }
        return formattedLength;
    }
}
