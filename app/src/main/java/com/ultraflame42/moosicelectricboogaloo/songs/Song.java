package com.ultraflame42.moosicelectricboogaloo.songs;


public class Song {


    private int length; // In seconds
    private String title;
    private String artist;
    private String album;
    private int playable; //todo change to id registry system for fileLink.
    private String[] tags;

    /**
     * Constructor for a song.
     * @param title Song title
     * @param artist Song artist
     * @param playable Song file link
     */
    public Song(String title, String artist, int playable) {
        this.length = 0;
        this.title = title;
        this.artist = artist;
        this.album= title;
        this.playable = playable;
    }

    /**
     * Constructor for a song.
     * @param title Song title
     * @param artist Song artist
     * @param album Album song belongs to.
     * @param playable Song file link
     */
    public Song(String title, String artist, String album, int playable) {

        this.length = 0;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.playable = playable;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void addTags(String[] tags) {
        this.tags = tags;
    }



    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
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

    public int getPlayable() {
        return playable;
    }
}
