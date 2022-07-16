package com.ultraflame42.moosicelectricboogaloo.tools;

import android.media.MediaPlayer;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.ultraflame42.moosicelectricboogaloo.songs.Song;

import java.io.IOException;

public class UsefulStuff {

    public static void setupActivity(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getSupportActionBar().hide();
    }

    /**
     * Caculates the duration of the song using media player
     *
     * @param song
     * @return The length of the song in ms
     * @throws IOException MediaPlayer Exceptions
     */
    public static int getSongDuration(Song song) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(song.getFileLink());
        mp.prepare();
        return mp.getDuration();
    }
}
