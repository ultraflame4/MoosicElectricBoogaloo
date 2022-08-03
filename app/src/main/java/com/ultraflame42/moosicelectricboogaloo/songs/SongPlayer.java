package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
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
    private static ExoPlayer exoPlayer;
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
    /*
     * This event fires when the Song Player tries to play a song that is not playable.
     * data contains error message for toast
     */
    public static CustomEvents<String> OnSongPlayError = new CustomEvents<>();


    // A copy of the current playlist., hence the name shadow
    private static List<Integer> currentPlaylistShadow = new ArrayList<>();

    public static void init(Context ctx) {
        exoPlayer = new ExoPlayer.Builder(ctx).build();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("SongPlayer", "ExoPlayer Error: " + error.getMessage(), error);
                OnSongPlayError.pushEvent(error.getMessage());
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    RegistryItem<Song> song = SongRegistry.getInstance().get(currentSong);
                    OnSongPlayChange.pushEvent(song);
                    OnSongPlayStateChange.pushEvent(true);
                } else if (playbackState == Player.STATE_ENDED) {
                    PlayNext();
                }

            }


        });
    }
//
//    public static MediaPlayer getNewMediaPlayer() {
//
//        MediaPlayer mp =new MediaPlayer();
//        mp.setOnCompletionListener(mediaPlayer1 -> {
//            mediaPlayer1.release();
//            PlayNext();
//        });
//
//        mp.setOnPreparedListener(mediaPlayer1 -> {
//            mediaPlayer.start();
//            RegistryItem<Song> song = SongRegistry.getInstance().get(currentSong);
//            OnSongPlayChange.pushEvent(song);
//            OnSongPlayStateChange.pushEvent(true);
//        });
//
//        mp.setOnErrorListener((mediaPlayer1, i, i2) -> {
//            String errMsg1 = "unknown error";
//            if (i==MediaPlayer.MEDIA_ERROR_UNKNOWN){
//                errMsg1 = "MediaPlayer.MEDIA_ERROR_UNKNOWN";
//            }
//            else if (i==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
//                errMsg1 = "MediaPlayer.MEDIA_ERROR_SERVER_DIED";
//            }
//
//            String errMsg2 = "unknown extra error";
//            if (i2==MediaPlayer.MEDIA_ERROR_IO){
//                errMsg2 = "MediaPlayer.MEDIA_ERROR_IO";
//            }
//            else if (i2==MediaPlayer.MEDIA_ERROR_MALFORMED){
//                errMsg2 = "MediaPlayer.MEDIA_ERROR_MALFORMED";
//            }
//            else if (i2==MediaPlayer.MEDIA_ERROR_UNSUPPORTED){
//                errMsg2 = "MediaPlayer.MEDIA_ERROR_MALFORMED";
//            }
//            else if (i2==MediaPlayer.MEDIA_ERROR_TIMED_OUT){
//                errMsg2 = "MediaPlayer.MEDIA_ERROR_TIMED_OUT";
//            }
//            else if (i2==-2147483648){
//                errMsg2 = "low-level system error.";
//            }
//
//            Log.e("SongPlayer", "Error playing song: " + errMsg1 + " " + errMsg2);
//            OnSongPlayError.pushEvent("This song cannot be played. This may be due to:" +
//                    "\n1. (If song source is url) Invalid url or bad internet connection." +
//                    "\n2. (If song source is file) File does not exist or is not readable or is not supported.9" );
//            PlayNext();
//            return true;
//        });
//        return mp;
//    }


    /**
     * For use internally to handle playing of a song.
     *
     * @param songId id of song to play
     */
    private static void playSong(int songId) {
        Log.d("SongPlayer", "playing song id " + songId);
        currentSong = songId;
        RegistryItem<Song> song = SongRegistry.getInstance().get(currentSong);
        if (!song.item.isPlayable()) {
            OnSongPlayError.pushEvent("This Song is not playable. The url or file may be broken. Skipping to next song.");
            PlayNext();
            return;
        }
        try {
            isPaused = false;
            pausedPosition = 0;
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(song.item.getFileLink())));
            Log.d("SongPlayer", "A");
            exoPlayer.prepare();
            Log.d("SongPlayer", "D");
            exoPlayer.play();

        } catch (IllegalStateException e) {
            Log.e("SongPlayer", "Error while trying to play song", e);
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
     *
     * @param playlistId    The playlist id
     * @param startPosition Which song to start playing from.
     */
    public static void PlayPlaylist(int playlistId, int startPosition) {
        currentPlaylist = playlistId;
        SongPlaylist playlist = PlaylistRegistry.getInstance().getItem(playlistId);

        if (playlist.getLength() < 1) {
            Log.w("SongPlayer", "Playlist does not have sufficient song <1!");
            return;
        }

        currentPlaylistShadow = playlist.getSongs();
        if (isShuffle) {
            Log.d("SongPlayer", "Shuffling playlist");
            Collections.shuffle(currentPlaylistShadow);
        }
        // using collections rotate, shift the song at startPosition to first position.
        Collections.rotate(currentPlaylistShadow, startPosition);

        // now play the first song.
        playSong(currentPlaylistShadow.get(0));
    }

    /**
     * Plays a playlist.
     *
     * @param playlistId The playlist id
     */
    public static void PlayPlaylist(int playlistId) {
        PlayPlaylist(playlistId, 0);
    }

    /**
     * Returns the currently playing song id.
     * <p>
     * Returns -1 if there are no currently playing songs
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
        if (exoPlayer.isPlaying()) {
            return exoPlayer.getCurrentPosition() / (float) exoPlayer.getDuration();
        } else if (isPaused) {
            return pausedPosition / (float) exoPlayer.getDuration();
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
        int calculatedPosition = Math.round(exoPlayer.getDuration() * progressPercent);
        if (exoPlayer.isPlaying()) {
            exoPlayer.seekTo(calculatedPosition);
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
        exoPlayer.play();

        isPaused = false;
        OnSongPlayStateChange.pushEvent(true);
    }

    /**
     * Pauses playing of song
     */
    public static void Pause() {
        exoPlayer.pause();
        pausedPosition = (int) exoPlayer.getCurrentPosition();
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
        exoPlayer.clearMediaItems();
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
            Log.d("SongPlayer", "Reached end of song/playlist");

            // If looping, replay playlist or song
            if (isLooping) {
                Log.d("SongPlayer", "Will loop current song or playlist");
                // if not playing playlist, replay song

                if (currentPlaylist < 0) {
                    // also check if currentSong is -1, if so, ignore else crash
                    if (currentSong < 0) {
                        Log.w("SongPlayer", "Unable to loop song because there is no current song");
                        return;
                    }
                    playSong(currentSong);
                } else {
                    // Replay current playlist
                    PlayPlaylist(currentPlaylist);
                }

            }
            else{
                OnSongPlayChange.pushEvent(null);
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
        return exoPlayer.isPlaying() || isPaused;
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
