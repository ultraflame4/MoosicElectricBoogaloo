package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;

public class PlaylistListAdapter extends BaseAdapter {
    Context ctx;
    SongPlaylist[] playlists;
    LayoutInflater inflater;

    public PlaylistListAdapter(Context ctx, SongPlaylist[] playlists) {
        this.ctx = ctx;
        this.playlists = playlists;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.playlist_listitem, null);
        }


        return convertView;
    }
}
