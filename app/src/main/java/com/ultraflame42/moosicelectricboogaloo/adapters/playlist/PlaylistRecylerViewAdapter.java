package com.ultraflame42.moosicelectricboogaloo.adapters.playlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

public class PlaylistRecylerViewAdapter extends RecyclerView.Adapter<SongListItemViewHolder> {


    Integer[] playlistSongs;
    private int playlistId;
    Context ctx;


    public PlaylistRecylerViewAdapter(int playlistId, Context ctx) {
        this.playlistId = playlistId;
        this.ctx = ctx;
        updatePlaylistInfo(true);
    }

    public void updatePlaylistInfo(boolean supressUpdate) {
        // Update playlist and songs information
        Log.d("PlaylistActivity", "Updating playlist and song information");
        // get playlist
        RegistryItem<SongPlaylist> playlist = PlaylistRegistry.getInstance().get(playlistId);
        // update the songslist
        playlistSongs = playlist.item.getSongs().toArray(new Integer[0]);
        if (!supressUpdate) {
            // update list
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public SongListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view from layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
        return new SongListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListItemViewHolder holder, int position) {
        // Get the song index using position from the playlist. Then get the song from the registry
        Song song = SongRegistry.getInstance().get(playlistSongs[position]).item;
        holder.setSong(song, ctx);
        holder.getCardView().setOnClickListener(view -> {
            // on item clicked
            onItemClick(position);
        });
    }

    public void onItemClick(int position) {
        // On item clicked, play the playlist, with the offsetted position.
        SongPlayer.PlayPlaylist(playlistId, position);
    }

    @Override
    public int getItemCount() {
        return playlistSongs.length;
    }
}
