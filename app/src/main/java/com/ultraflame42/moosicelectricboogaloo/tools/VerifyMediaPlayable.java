package com.ultraflame42.moosicelectricboogaloo.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class VerifyMediaPlayable {
    public interface OnMediaVerificationListener {
        /**
         * Called when the media has been verified to be playable.
         *
         * @param playable whether the media is playable by media player
         */
        void onMediaVerified(boolean playable, int songLength);
    }

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

            MediaPlayer mp = new MediaPlayer();
            try {

                UsefulStuff.setMediaPlayerDataSource(mp, fileLink, ctx);

                mp.prepare();
                mp.setOnBufferingUpdateListener((mediaPlayer, i) -> {
                    Log.d("VerifyMediaPlayable: MediaPlayer", "Buffering: " + i);
                });
                mp.start();
            } catch (IOException e) {
                Log.e("VerifyMediaPlayable", "Error: " + e.getMessage());
                return null;
            }

            Log.i("VerifyMediaPlayable", "Link: " + fileLink.toString() + " is playable.");
            return mp;
        }

        protected void onPostExecute(MediaPlayer result) {

            if (result != null) {
                callback.onMediaVerified(true, result.getDuration());
                result.release();
            } else {
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
