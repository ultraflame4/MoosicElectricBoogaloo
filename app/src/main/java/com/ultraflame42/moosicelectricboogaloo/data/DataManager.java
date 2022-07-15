package com.ultraflame42.moosicelectricboogaloo.data;

import android.content.Context;
import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.songs.RegisteredSong;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

import java.util.HashMap;

/**
 *  Main Interface to interact with stored data
 *  Manages the download and retrieval of data from links.
 *  
 */
public class DataManager {
    static HashMap<Integer, String> playables = new HashMap<>();

    public static void init(Context appCtx) {
        //todo load from storage
        try {
            YoutubeDL.getInstance().init(appCtx);
            FFmpeg.getInstance().init(appCtx);
            Log.d("DATA MANAGER", "Initialised youtubedl-android successfully :) yay!");
        } catch (YoutubeDLException e) {
            Log.e("DATA MANAGER", "failed to initialize youtubedl-android", e);
        }
    }

    public static int getPlayable(String link) {
        return 0;
    }
    public static String getMediaLocation(int playable) {
        return playables.get(playable);
    }
}
