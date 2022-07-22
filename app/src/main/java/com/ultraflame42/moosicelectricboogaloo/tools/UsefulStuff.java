package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ultraflame42.moosicelectricboogaloo.songs.Song;

import java.io.IOException;

public class UsefulStuff {
    /**
     * This method exists solely to make my life easier.
     *
     * Removes title bar from activity.
     * @param activity
     */
    public static void setupActivity(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getSupportActionBar().hide();
    }

    /**
     * This method exists solely to make my life easier.
     *
     * Makes dialog window background transparent. and titleless.
     *
     * (So i can have rounded corners)
     * @param fragment
     */
    public static void setupDialogFragment(DialogFragment fragment) {
        fragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public static DisplayMetrics getDisplayMetrics(AppCompatActivity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
