package com.ultraflame42.moosicelectricboogaloo.songs;

import android.content.res.Resources;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.Registry;

import java.util.HashMap;

public class SongRegistry{
    public static Registry<Song> songs = new Registry<>();
    public static Registry<SongPlaylist> playlists = new Registry<>();

}
