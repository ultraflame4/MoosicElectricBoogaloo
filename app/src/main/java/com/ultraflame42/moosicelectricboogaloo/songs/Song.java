package com.ultraflame42.moosicelectricboogaloo.songs;


import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.tools.OnMediaVerificationListener;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Song {


    private int length = -1; // In milliseconds
    private String title;
    private String artist;
    private String album;
    private String fileLink; //todo change to id registry system for fileLink.
    private String[] tags;
    private boolean playable=false; // if false, link is broken and song is not playable
    /**
     * This event fires when the Song information is updated.
     *
     * All listeners are removed after each firing
     */
    public DefaultEvent OnSongInfoUpdate = new DefaultEvent();    /**
     * This event fires when the Song is verified
     *
     * called before info update
     */
    public DefaultEvent OnSongVerified = new DefaultEvent();

    /**
     * Constructor for a song.
     *
     * @param title    Song title
     * @param artist   Song artist
     * @param fileLink Song file link
     */
    public Song(String title, String artist, String fileLink) {
        this.title = title;
        this.artist = artist;
        this.album = title;
        this.fileLink = fileLink;
    }

    /**
     * Constructor for a song.
     *
     * @param title    Song title
     * @param artist   Song artist
     * @param album    Album song belongs to.
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
     * Updates and retrieve vital song information such as song duration
     *
     * also checks if the song is playable.
     */
    public void updateAndRetrieveSongInfo() {

        UsefulStuff.GetInfoAndVerifyMediaPlayable(fileLink, new OnMediaVerificationListener() {
            @Override
            public void onMediaVerified(boolean playable_) {
                playable=playable_;
                Log.d("Song", "Title:"+title+" Verified Playable: " + playable);
                OnSongVerified.pushEvent(null);
                OnSongVerified.clearListeners();
            }

            @Override
            public void setSongInfo(int songDuration) {
                length=songDuration;
                OnSongInfoUpdate.pushEvent(null);
                OnSongInfoUpdate.clearListeners();
            }
        });

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

    public boolean isPlayable() {
        return playable;
    }
}
