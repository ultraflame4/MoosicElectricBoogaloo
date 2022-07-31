package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ultraflame42.moosicelectricboogaloo.songs.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsefulStuff {
    /**
     * This method exists solely to make my life easier.
     * <p>
     * Removes title bar from activity.
     *
     * @param activity
     */
    public static void setupActivity(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getSupportActionBar().hide();
    }

    /**
     * This method exists solely to make my life easier.
     * <p>
     * Makes dialog window background transparent. and titleless.
     * <p>
     * (So i can have rounded corners)
     *
     * @param fragment
     */
    public static void setupDialogFragment(DialogFragment fragment) {
        fragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public static String formatMilliseconds(Integer time) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        return minutes + " : " + seconds;
    }

    public static void setMediaPlayerDataSource(MediaPlayer mediaPlayer, String source, Context context) throws IOException {
        mediaPlayer.setDataSource(context, Uri.parse(source));

    }
}
