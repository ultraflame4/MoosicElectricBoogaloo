package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        return playlists.length;
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
        Log.d("AA<", "ADWASDWAD");

        if (inflater == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.playlist_listitem, null);
        }
        ImageView playlistImage = convertView.findViewById(R.id.playlistItem_image);
        TextView playlistName = convertView.findViewById(R.id.playlist_title);
        TextView playlistCreator = convertView.findViewById(R.id.playlist_creator);
        TextView playlistSongCount = convertView.findViewById(R.id.playlist_songcount);
        TextView playlistTotalLength = convertView.findViewById(R.id.playlist_totallength);

        playlistName.setText(playlists[i].getTitle());
        playlistCreator.setText(playlists[i].getCreator());
        playlistSongCount.setText(R.string.playlist_songcount_text+" "+playlists[i].getSongCount());
        playlistTotalLength.setText(R.string.playlist_totallength_text+" "+playlists[i].getLength());


        return convertView;
    }
}
