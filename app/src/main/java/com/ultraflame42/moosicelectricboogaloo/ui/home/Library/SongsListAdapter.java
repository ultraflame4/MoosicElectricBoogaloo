package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;

import org.w3c.dom.Text;

public class SongsListAdapter extends BaseAdapter {
    Context ctx;
    Song[] songs;
    LayoutInflater inflater;

    public SongsListAdapter(Context ctx, Song[] songs) {
        this.ctx = ctx;
        this.songs = songs;

    }

    @Override
    public int getCount() {
        return songs.length;
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
            convertView = inflater.inflate(R.layout.songlist_item, null);
        }

        ImageView songImg = convertView.findViewById(R.id.songList_itemImage);
        TextView songTitle = convertView.findViewById(R.id.songList_itemTitle);
        TextView songArtist = convertView.findViewById(R.id.songList_itemArtist);
        TextView songLength = convertView.findViewById(R.id.songList_itemLength);

        Song song = songs[i];

        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        songLength.setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLength());

        return convertView;
    }
}
