package com.ultraflame42.moosicelectricboogaloo.songs;


import android.net.Uri;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class Song{


    private int length = -1; // In milliseconds
    private String title;
    private String artist;
    private String album;
    private String fileLink; //todo change to id registry system for fileLink.
    private String imageUriLink; //either link or local internal file path.

    private boolean playable=false; // if false, link is broken and song is not playable
    //todo implement picasso for image loading

    /**
     * Constructor for a song.
     *  @param title    Song title
     * @param artist   Song artist
     * @param fileLink Song file link. can be either a url or a uri file path.
     * @param imageUriLink
     */
    public Song(String title, String artist, String fileLink, String imageUriLink) {
        this.title = title;
        this.artist = artist;
        this.album = title;
        this.fileLink = fileLink;
        this.imageUriLink = imageUriLink;
    }

    public void setRuntimeInfo(boolean playable,int length) {
        Log.d("Song","setRuntimeInfo for song: "+title + " playable: "+playable+" length: "+length);
        formattedLength = ""; // invalidate formatted length cache
        this.playable = playable;
        this.length = length;
    }

    /**
     * Caculates length of song using media player
     *
     * @return Returns length of song in milliseconds
     */
    public int getLength() {
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

    /**
     * Returns either URI or url link of song.
     * @return
     */
    public String getFileLink() {
        return fileLink;
    }


    private String formattedLength = "";
    /**
     * Returns formatted length of song in format minutes:seconds
     *
     * @return
     */
    public String getLengthFormatted() {
        if (formattedLength.equals("")) {
            formattedLength = UsefulStuff.formatMilliseconds(getLength());
        }
        return formattedLength;
    }

    public boolean isPlayable() {
        return playable;
    }

    public String getImageUriLink() {
        return imageUriLink;
    }
}
