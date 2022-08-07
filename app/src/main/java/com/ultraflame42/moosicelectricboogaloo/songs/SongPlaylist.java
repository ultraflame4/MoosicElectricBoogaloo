package com.ultraflame42.moosicelectricboogaloo.songs;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Returns the total length of the playlist in milliseconds
     */
    public int getLength() {
        int length = songs.stream().mapToInt(integer -> SongRegistry.getInstance().getItem(integer).getLength()).sum();
        return length;
    }

    private String formattedLength = "";

    /**
     *  Returns formatted length of playlist in minutes and seconds
     * @return
     */
    public String getLengthFormatted() {
        if (formattedLength.equals("")) {
            formattedLength = UsefulStuff.formatMilliseconds(getLength());
        }
        return formattedLength;
    }

    /**
     * Loads the playlist cover/image into the specified image view.
     * @param imageView
     */
    public void loadCoverIntoImageView(ImageView imageView) {
        if (songs.size() > 0) {
            Picasso.get().load(SongRegistry.getInstance().get(songs.get(0)).item.getImageUriLink())
                    // on error use default image
                    .error(R.drawable.color_a_bg)
                    .into(imageView);
        }
        else{
            Picasso.get().load(R.drawable.color_a_bg)
                    .into(imageView);
        }
    }
}
