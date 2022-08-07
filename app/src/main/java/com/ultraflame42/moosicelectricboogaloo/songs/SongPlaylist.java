package com.ultraflame42.moosicelectricboogaloo.songs;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SongPlaylist {
    private ArrayList<Integer> songs = new ArrayList<>();
    private String creator;
    private String title;
    private final boolean isAlbum; // if true, this playlist is an album.
    /**
     * if true, this playlist is created by the system and is should not be deletable by the user. eg. Liked Songs
     */
    public boolean isSystem = false;


    /**
     * @param creator Playlist creator name
     * @param title   Playlis title
     * @param songs   initial list of songs to add to the playlist
     */
    public SongPlaylist(String creator, String title, Integer[] songs) {
        this.creator = creator;
        this.title = title;
        this.songs.addAll(Arrays.asList(songs));
        isAlbum = false;

    }

    /**
     * @param creator Playlist creator name
     * @param title   Playlist title
     */
    public SongPlaylist(String creator, String title) {
        this.creator = creator;
        this.title = title;
        isAlbum = false;
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

    }

    public void removeSongAtIndex(int index) {
        // Remove song
        songs.remove(index);
    }

    public void removeSong(int songId) {
        // find index of songid and remove
        removeSongAtIndex(songs.indexOf(songId));
    }

    public boolean hasSong(int songId) {
        return songs.contains(songId);
    }

    /**
     * Returns the ids of all songs in this playlist.
     */
    public List<Integer> getSongs() {
        // return a copy of the list so that wtv changes does not affect the original list
        return new ArrayList<>(songs);
    }

    public int getSongCount() {
        // return the size of the arraylist
        return songs.size();
    }

    public String getCreator() {
        // return the creator of playlist
        return creator;
    }

    public String getTitle() {
        // return the title of the playlist
        return title;
    }


    /**
     * Returns the total length of the playlist in milliseconds
     */
    public int getLength() {
        // sum the length of all songs in the playlist
        int length = songs.stream().mapToInt(integer -> SongRegistry.getInstance().getItem(integer).getLength()).sum();
        return length;
    }

    private String formattedLength = "";

    /**
     *  Returns formatted length of playlist in minutes and seconds
     * @return
     */
    public String getLengthFormatted() {
        // if formatted length is empty, format it
        if (formattedLength.equals("")) {
            // format the length of the playlist
            formattedLength = UsefulStuff.formatMilliseconds(getLength());
        }
        // Return the formmated length of the playlist
        return formattedLength;
    }

    /**
     * Loads the playlist cover/image into the specified image view.
     * @param imageView
     */
    public void loadCoverIntoImageView(ImageView imageView) {
        // If there are more than one song in the playlist
        if (songs.size() > 0) {
            // get the first song in the playlist, and load its cover into the image view
            Picasso.get().load(SongRegistry.getInstance().get(songs.get(0)).item.getImageUriLink())
                    // on error use default image
                    .error(R.drawable.color_a_bg)
                    .into(imageView);
        }
        else{
            // else if there are no songs in the playlist, use the default image
            Picasso.get().load(R.drawable.color_a_bg)
                    .into(imageView);
        }
    }
}
