package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.viewholders.PlaylistItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistItemViewHolder> {
    Context ctx;
    RegistryItem<SongPlaylist>[] playlists;

    private EventFunctionCallback<Integer> OnPlaylistClickedCallback;
    PlaylistRegistry playlistRegistry;

    public PlaylistListAdapter(Context ctx, EventFunctionCallback<Integer> onPlaylistClickedCallback) {
        // Set the context for which this adapter is used in
        this.ctx = ctx;
        // Set the callback for when a playlist is clicked
        OnPlaylistClickedCallback = onPlaylistClickedCallback;
        // Get the playlist registry
        playlistRegistry = PlaylistRegistry.getInstance();
        // Initiate a nwe empty array
        playlists= new RegistryItem[0];

    }

    public void updateData() {
        Log.d("PlaylistListAdapter", "SongRegistry playlists updated, updating list");
        // get all items from playlist registry and update data
        playlists = playlistRegistry.getAllItems();
        // notify adapter that data has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate view for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
        // return a new view holder
        return new PlaylistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistItemViewHolder holder, int i) {
        // get the playlist from position
        SongPlaylist playlist = playlists[i].item;
        // Set the playlist for view holder. view holder will take care of setting all views with the
        holder.setPlaylist(playlist, ctx);
        holder.getCardView().setOnClickListener(view -> {
            // On item click...
            onItemClick(view,i);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.length;
    }


    public void onItemClick(View view, int position) {
        // Get playlist that was clicked
        RegistryItem<SongPlaylist> playlist = playlists[position];
        // Call the callback with the id of playlist that was clicked
        OnPlaylistClickedCallback.call(playlist.id);
    }

}
