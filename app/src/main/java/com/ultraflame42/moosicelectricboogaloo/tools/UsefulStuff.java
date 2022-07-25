package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ultraflame42.moosicelectricboogaloo.songs.Song;

import java.io.IOException;
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

    private static class VerifyMediaPlayable extends AsyncTask<String, Integer, MediaPlayer> {
        private OnMediaVerificationListener callback;

        @Override
        protected MediaPlayer doInBackground(String... strings) {
            String media = strings[0];
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(media);
                mp.prepare();
                mp.setOnBufferingUpdateListener((mediaPlayer, i) -> {
                    Log.d("VerifyMediaPlayer", "Buffering: " + i);
                });
            } catch (IOException e) {
                Log.e("VerifyMediaPlayable", "Error: " + e.getMessage());
                return null;
            }
            return mp;
        }

        protected void onPostExecute(MediaPlayer result) {
            if (result != null) {
                callback.onMediaVerified(true);
                callback.setSongInfo(result.getDuration());
            } else {
                callback.onMediaVerified(false);
            }
        }

        public void setCallback(OnMediaVerificationListener callback) {
            this.callback = callback;
        }
    }

    /**
     * Verifies that a media is playable by media player
     *
     * @param medialocation
     */
    public static void GetInfoAndVerifyMediaPlayable(String medialocation, OnMediaVerificationListener callback) {
        Log.d("GetInfoAndVerifyMediaPlayable", "Verifying media: " + medialocation);
        VerifyMediaPlayable task = new VerifyMediaPlayable();
        task.setCallback(callback);
        task.execute(medialocation);

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

}
