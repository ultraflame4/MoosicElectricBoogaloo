package com.ultraflame42.moosicelectricboogaloo.songs;

import android.media.MediaPlayer;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongPlayer {
    // If -1, no playlist is playing.
    private static int currentPlaylist = -1;
    private static int currentSong = -1;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static int pausedPosition = 0;
    private static boolean isPaused = false;
    private static boolean isLooping = false;
    private static boolean isShuffle = false;


    /**
     * This event fires when a new song starts playing or there are no more song playing.
     */
    public static CustomEvents<RegistryItem<Song>> OnSongPlayChange = new CustomEvents<>();
    /**
     * This event fires when the Song Player play state is changed
     * True if the state has changed to playing
     * False if the state has changed to paused/stopped playing
     */
    public static CustomEvents<Boolean> OnSongPlayStateChange = new CustomEvents<>();


    // A copy of the current playlist., hence the name shadow
    private static List<Integer> currentPlaylistShadow = new ArrayList<>();

    public static void init() {
        mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
            PlayNext();
        });
    }


    /**
     * For use internally to handle playing of a song.
     *
     * @param songId id of song to play
     */
    private static void playSong(int songId) {

        RegistryItem<Song> song = SongRegistry.getInstance().get(songId);
        try {
            isPaused = false;
            pausedPosition = 0;
            mediaPlayer.reset();

            mediaPlayer.setDataSource(song.item.getFileLink());

            mediaPlayer.prepare();

            mediaPlayer.start();
            currentSong = songId;
            OnSongPlayChange.pushEvent(song);
            OnSongPlayStateChange.pushEvent(true);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Plays ONE song. To play a playlist, use PlayPlaylist()
     * May queue other songs to play after the current song.
     *
     * @param songId Id of song to play.
     */
    public static void PlaySong(int songId) {
        currentPlaylist = -1;
        playSong(songId);
    }

    /**
     * Plays a playlist.
     * @param playlistId The playlist id
     * @param startPosition Which song to start playing from.
     */
    public static void PlayPlaylist(int playlistId,int startPosition) {
        currentPlaylist = playlistId;
        currentPlaylistShadow = PlaylistRegistry.getInstance().get(playlistId).item.getSongs();
        if (isShuffle) {
            Log.d("SongPlayer", "Shuffling playlist");
            Collections.shuffle(currentPlaylistShadow);
        }
        // using collections rotate, shift the song at startPosition to first position.
        Collections.rotate(currentPlaylistShadow,startPosition);
        // now play the first song.
        playSong(currentPlaylistShadow.get(0));
    }
    /**
     * Plays a playlist.
     * @param playlistId The playlist id
     */
    public static void PlayPlaylist(int playlistId) {
        PlayPlaylist(playlistId,0);
    }

    /**
     * Returns the currently playing song
     *
     * @return
     */
    public static int GetCurrentSong() {
        return currentSong;
    }

    /**
     * Returns the current song progress in percentage
     *
     * @return
     */
    public static float GetCurrentSongProgress() {
        if (mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();
        } else if (isPaused) {
            return pausedPosition / (float) mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public static int getCurrentPlaylist() {
        return currentPlaylist;
    }

    /**
     * Sets the current progress of the playlist
     *
     * @param progressPercent 0-1
     */
    public static void SetCurrentSongProgress(float progressPercent) {
        int calculatedPosition = Math.round(mediaPlayer.getDuration() * progressPercent);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(calculatedPosition);
        } else if (isPaused) {
            // Change the paused position -> when un-paused will seek back to that position
            // And undo the scrubbing :(
            pausedPosition = calculatedPosition;
        }

    }

    /**
     * Resumes playing of song
     */
    public static void Resume() {
        mediaPlayer.start();
        mediaPlayer.seekTo(pausedPosition);
        isPaused = false;
        OnSongPlayStateChange.pushEvent(true);
    }

    /**
     * Pauses playing of song
     */
    public static void Pause() {
        mediaPlayer.pause();
        pausedPosition = mediaPlayer.getCurrentPosition();
        isPaused = true;
        OnSongPlayStateChange.pushEvent(false);
    }

    /**
     * Returns the next song in the playlist
     * <p>
     * -1 if there is no next song
     *
     * @return
     */
    public static int getNextSongInPlaylist() {
        // get current position of song in playlist
        int pos = currentPlaylistShadow.indexOf(currentSong);

        // if position is -1, tis current not in the playlist. Return start index (0)
        // Else + 1 to pos.
        // In either ways, we get what we want (-1+1=0)

        int nextPos = pos + 1;
        // If nextPos is more than playlist size, return -1

        if (nextPos > currentPlaylistShadow.size() - 1) {
            return -1;
        }
        return currentPlaylistShadow.get(nextPos);
    }

    public static int getPrevSongInPlaylist() {
        // get current position of song in playlist
        int pos = currentPlaylistShadow.indexOf(currentSong);

        int nextPos = pos - 1;
        // If nextPos is more than playlist size, return -1
        if (nextPos < 0) {
            return -1;
        }
        return currentPlaylistShadow.get(nextPos);
    }

    public static void resetState() {
        mediaPlayer.reset();
        pausedPosition = 0;

        OnSongPlayStateChange.pushEvent(false);
    }

    public static void PlayNext() {
        Log.d("SongPlayer", "Playing Next Song");
        // Play next song in playlist. If -1 no next song.
        int nxtSong = getNextSongInPlaylist();
        if (nxtSong >= 0) {

            playSong(nxtSong);
        } else {
            resetState();
            Log.d("SongPlayer: WARN but not really", "There is no next song");

            // If looping, replay playlist or song
            if (isLooping) {
                Log.d("SongPlayer", "Will loop current song or playlist");
                // if not playing playlist, replay song
                if (currentPlaylist < 0) {

                    playSong(currentSong);

                } else {
                    // Replay current playlist
                    PlayPlaylist(currentPlaylist);
                }

            }
        }
    }

    public static void PlayPrev() {
        int prevSong = getPrevSongInPlaylist();
        if (prevSong < 0) {
            // no prev song. Replay current song.
            playSong(currentSong);
        } else {
            playSong(prevSong);
        }

    }

    public static boolean IsPaused() {
        return isPaused;
    }

    /**
     * Returns true if paused or is playing
     *
     * @return
     */
    public static boolean IsReady() {
        return mediaPlayer.isPlaying() || isPaused;
    }

    public static boolean IsLooping() {
        return isLooping;
    }

    public static void SetLooping(boolean isLooping) {
        SongPlayer.isLooping = isLooping;
    }

    public static boolean IsShuffle() {
        return isShuffle;
    }

    public static void SetShuffle(boolean toShuffle) {
        SongPlayer.isShuffle = toShuffle;
    }
}
