package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Utility class to verify that a media file is playable.
 */
public class VerifyMediaPlayable {
    public interface OnMediaVerificationListener {
        /**
         * Called when the media has been verified to be playable.
         *
         * @param playable whether the media is playable by media player
         */
        void onMediaVerified(boolean playable, int songLength);
    }

    // Use async task so that it runs in the background and does not obstruct the ui
    private static class VerifyMediaPlayableTask extends AsyncTask<Integer, Integer, MediaPlayer> {
        private Context ctx;
        private OnMediaVerificationListener callback;
        private String fileLink;

        public VerifyMediaPlayableTask(Context ctx, OnMediaVerificationListener callback, String fileLink) {
            this.ctx = ctx;
            this.callback = callback;
            this.fileLink = fileLink;
        }

        @Override
        protected MediaPlayer doInBackground(Integer... args) {
            // try to set up media player to lay the thing
            MediaPlayer mp = new MediaPlayer();
            try {

                UsefulStuff.setMediaPlayerDataSource(mp, fileLink, ctx);

                mp.prepare();
                mp.start();
                mp.stop(); // if it can play, stop it. prevent audio
            } catch (IOException e) {
                // If error return null
                Log.e("VerifyMediaPlayable", "Error: " + e.getMessage());
                return null;
            }

            Log.d("VerifyMediaPlayable", "Link: " + fileLink.toString() + " is playable.");
            return mp;
        }

        protected void onPostExecute(MediaPlayer result) {

            if (result != null) {
                // If result is not null, media is playable
                callback.onMediaVerified(true, result.getDuration());
                result.release();
            } else {
                // If result is null, media is not playable
                callback.onMediaVerified(false, -1);
            }

        }
    }

    /**
     * Verifies that a media is playable by media player
     *
     * @param fileLink
     */
    public static void GetInfoAndVerifyMediaPlayable(Context ctx, String fileLink, OnMediaVerificationListener callback) {
        Log.d("GetInfoAndVerifyMediaPlayable", "Verifying media: " + fileLink);
        VerifyMediaPlayableTask task = new VerifyMediaPlayableTask(ctx, callback, fileLink);
        task.execute(0);

    }

}
