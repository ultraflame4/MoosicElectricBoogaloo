package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;

import org.w3c.dom.Text;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.ViewHolder>  {
    Context ctx;
    Song[] songs;
    LayoutInflater inflater;

    public SongsListAdapter(Context ctx, Song[] songs) {
        this.ctx = ctx;
        this.songs = songs;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final ShapeableImageView songImg;
        private final TextView songName;
        private final TextView songArtist;
        private final TextView songCount;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            songImg = view.findViewById(R.id.songList_itemImage);
            songName = view.findViewById(R.id.songList_itemTitle);
            songArtist = view.findViewById(R.id.songList_itemArtist);
            songCount = view.findViewById(R.id.songList_itemLength);
        }

        public ShapeableImageView getSongImg() {
            return songImg;
        }

        public TextView getSongName() {
            return songName;
        }

        public TextView getSongArtist() {
            return songArtist;
        }

        public TextView getSongCount() {
            return songCount;
        }
    }

    @NonNull
    @Override
    public SongsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
        return new SongsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs[position];
//        holder.getSongImg().setImageResource(song.getImage()); todo set image
        holder.getSongName().setText(song.getTitle());
        holder.getSongArtist().setText(song.getArtist());
        holder.getSongCount().setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLength());
    }

    @Override
    public int getItemCount() {
        return songs.length;
    }

    //    @Override
//    public int getCount() {
//        return songs.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        if (inflater == null) {
//            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.songlist_item, null);
//        }
//
//        ImageView songImg = convertView.findViewById(R.id.songList_itemImage);
//        TextView songTitle = convertView.findViewById(R.id.songList_itemTitle);
//        TextView songArtist = convertView.findViewById(R.id.songList_itemArtist);
//        TextView songLength = convertView.findViewById(R.id.songList_itemLength);
//
//        Song song = songs[i];
//
//        songTitle.setText(song.getTitle());
//        songArtist.setText(song.getArtist());
//        songLength.setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLength());
//
//        return convertView;
//    }
}
