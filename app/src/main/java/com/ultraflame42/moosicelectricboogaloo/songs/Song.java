package com.ultraflame42.moosicelectricboogaloo.songs;


public class Song {


    private int length; // In seconds
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
        this.length = 0;
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

        this.length = 0;
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

    public String getFileLink() {
        return fileLink;
    }
}
