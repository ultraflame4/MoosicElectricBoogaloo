package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventCallbackListener;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryUpdateData;

public class SongsListAdapter extends RecyclerView.Adapter<SongListItemViewHolder> {
    Context ctx;
    RegistryItem<Song>[] songs;
    LayoutInflater inflater;

    EventCallbackListener<RegistryUpdateData<Song>> onSongAddedListener;

    public SongsListAdapter(Context ctx) {
        this.ctx = ctx;
        songs = SongRegistry.songs.getAllItems();
        onSongAddedListener = SongRegistry.songs.OnItemsUpdate.addListener(song ->{
            Log.d("SongsListAdapter", "SongRegistry updated, updating list");
            songs = SongRegistry.songs.getAllItems();
            notifyDataSetChanged();
        });

    }


    @Override
    public int getItemCount() {
        return songs.length;
    }

    @NonNull
    @Override
    public SongListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
        return new SongListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListItemViewHolder holder, int position) {
        Song song = songs[position].item;
//        holder.getSongImg().setImageResource(song.getImage()); todo set image

        holder.getSongName().setText(song.getTitle());
        holder.getSongArtist().setText(song.getArtist());

        holder.getSongCount().setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLengthFormatted());

        holder.getCardView().setOnClickListener(view -> {
            onItemClick(view,position);
        });
    }

    public void onItemClick(View view, int position) {
        RegistryItem<Song> song = songs[position];
        SongPlayer.PlaySong(song.id);

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
