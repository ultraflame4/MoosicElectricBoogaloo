package com.ultraflame42.moosicelectricboogaloo.songs;

public class RegisteredSong extends Song{
    private int id;
    /**
     * Constructor for a song.
     * @param id       Song id in registry
     * @param length   Song length in seconds
     * @param title    Song title
     * @param artist   Song artist
     * @param album    Album song belongs to.
     * @param fileLink Song file link
     */
    public RegisteredSong(int id, int length, String title, String artist, String album, String fileLink) {
        super(title, artist, album, fileLink);
        this.id = id;
    }
    public RegisteredSong(int id,Song song) {
        super(song.getTitle(), song.getArtist(), song.getAlbum(), song.getFileLink());
        this.id = id;
    }
    public int getId() {
        return id;
    }

}
