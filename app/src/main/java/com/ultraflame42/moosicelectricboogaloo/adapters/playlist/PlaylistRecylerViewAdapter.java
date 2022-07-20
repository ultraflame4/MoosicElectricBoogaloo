package com.ultraflame42.moosicelectricboogaloo.adapters.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

public class PlaylistRecylerViewAdapter extends RecyclerView.Adapter<SongListItemViewHolder> {


    Integer[] playlistSongs;
    private int playlistId;
    Context ctx;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View parent) {
            super(parent);
        }
    }

    public PlaylistRecylerViewAdapter(int playlistId ,Context ctx) {
        this.playlistId = playlistId;
        this.ctx = ctx;
        updatePlaylistInfo(true);
    }

    public void updatePlaylistInfo(boolean supressUpdate) {
        RegistryItem<SongPlaylist> playlist = SongRegistry.playlists.get(playlistId);
        playlistSongs = playlist.item.getSongs().toArray(new Integer[0]);
        if (!supressUpdate) {
            notifyDataSetChanged();
        }
    }

    public void updatePlaylistInfo() {
        updatePlaylistInfo(true);
    }

    @NonNull
    @Override
    public SongListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);


        return new SongListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListItemViewHolder holder, int position) {

        Song song = SongRegistry.songs.get(position).item;
        holder.getSongName().setText(song.getTitle());
        holder.getSongArtist().setText(song.getArtist());

        holder.getSongCount().setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLengthFormatted());

        holder.getCardView().setOnClickListener(view -> {
            onItemClick(position);
        });
    }

    public void onItemClick(int position) {
        SongPlayer.PlayPlaylist(playlistId, position);
    }

    @Override
    public int getItemCount() {
        return playlistSongs.length;
    }
}