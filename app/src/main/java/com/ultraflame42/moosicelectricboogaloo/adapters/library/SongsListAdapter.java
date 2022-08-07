package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

public class SongsListAdapter extends RecyclerView.Adapter<SongListItemViewHolder> {
    Context ctx;
    RegistryItem<Song>[] songs;

    SongRegistry songRegistry;

    public SongsListAdapter(Context ctx) {
        // Set the context for which this adapter is used in
        this.ctx = ctx;
        // Get the song registry instance
        songRegistry = SongRegistry.getInstance();
        // Initiate a new empty array
        songs=new RegistryItem[0];
    }


    public void updateData() {
        Log.d("SongsListAdapter", "SongRegistry updated, updating list");
        // get all items from song registry and update data
        songs = songRegistry.getAllItems();

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        // Return the number of items in the songs list so that recycler view know how many items to display
        return songs.length;
    }

    @NonNull
    @Override
    public SongListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate view for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
        // return a new view holder
        return new SongListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListItemViewHolder holder, int position) {
        // Get the song from current position
        Song song = songs[position].item;
        // Set the song for view holder. view holder will take care of setting all views with the information from the song
        holder.setSong(song, ctx);
        // Set onclick listener for each item
        holder.getCardView().setOnClickListener(view -> {
            // on item click..
            onItemClick(view, position);
        });
    }

    public void onItemClick(View view, int position) {
        // Get song from the position
        RegistryItem<Song> song = songs[position];
        // Play the song
        SongPlayer.PlaySong(song.id);
    }

}
