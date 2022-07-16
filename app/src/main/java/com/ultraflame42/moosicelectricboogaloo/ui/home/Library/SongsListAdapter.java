package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.RegisteredSong;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventCallbackListener;

import java.time.Instant;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.ViewHolder> {
    Context ctx;
    RegisteredSong[] songs;
    LayoutInflater inflater;

    EventCallbackListener<RegisteredSong> onSongAddedListener;

    public SongsListAdapter(Context ctx) {
        this.ctx = ctx;
        songs = SongRegistry.getAllSongs();
        onSongAddedListener = SongRegistry.OnSongAdded.addListener(song ->{
            Log.d("SongsListAdapter", "SongRegistry updated, updating list");
            songs = SongRegistry.getAllSongs();
            notifyDataSetChanged();
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final ShapeableImageView songImg;
        private final TextView songName;
        private final TextView songArtist;
        private final TextView songCount;
        private final CardView cardView;


        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.songlist_itemcard);
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

        public CardView getCardView() {
            return cardView;
        }
    }

    @Override
    public int getItemCount() {
        return songs.length;
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

        holder.getSongCount().setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLengthFormatted());

        holder.getCardView().setOnClickListener(view -> {
            onItemClick(view,position);
        });
    }

    public void onItemClick(View view, int position) {
        RegisteredSong song = songs[position];
        SongPlayer.PlaySong(song.getId());

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
