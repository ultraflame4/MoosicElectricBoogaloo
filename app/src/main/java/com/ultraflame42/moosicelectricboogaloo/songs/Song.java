package com.ultraflame42.moosicelectricboogaloo.songs;


public class Song {

    private int id;
    private int length; // In seconds
    private String title;
    private String artist;
    private String album;
    private String fileLink;
    private String[] tags;

    public Song(int id, int length, String title, String artist, String fileLink) {
        this.id = id;
        this.length = length;
        this.title = title;
        this.artist = artist;
        this.album= title;
        this.fileLink = fileLink;
    }

    public Song(int id, int length, String title, String artist, String album, String fileLink) {
        this.id = id;
        this.length = length;
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

    public int getId() {
        return id;
    }

    public float getLength() {
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
}
