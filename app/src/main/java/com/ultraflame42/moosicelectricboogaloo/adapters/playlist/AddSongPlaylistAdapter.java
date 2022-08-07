package com.ultraflame42.moosicelectricboogaloo.adapters.playlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.HashSet;

public class AddSongPlaylistAdapter extends RecyclerView.Adapter<AddSongPlaylistAdapter.ViewHolder> {
    private Context ctx;
    private RegistryItem<Song>[] songs;

    private SongRegistry songRegistry;

    private final HashSet<Integer> checkedSongs;

    public static class ViewHolder extends SongListItemViewHolder {
        private final ToggleButton checkedToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // get the toggle button view
            checkedToggle = itemView.findViewById(R.id.checkedToggle);
        }

        public ToggleButton getCheckedToggle() {
            // return the toggle button
            return checkedToggle;
        }
    }

    public AddSongPlaylistAdapter(Context ctx) {
        // Set the context the recycler view is in using the constructor parameter for it
        this.ctx = ctx;
        // Get song registry
        songRegistry = SongRegistry.getInstance();
        // initialsie empty array
        songs = new RegistryItem[0];
        // initialize empty checked songs hashset
        checkedSongs = new HashSet<>();
    }

    public void updateData() {
        // Log stuff
        Log.d("SongsListAdapter", "SongRegistry updated, updating list");
        // update the song list
        songs = songRegistry.getAllItems();
        // notify adapter to update
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout view for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_song_playlist_item, parent, false);
        // retuirn new view holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get song for current position
        RegistryItem<Song> song = songs[position];
        // Set song information
        holder.setSong(song.item, ctx);
        // Get toggle button
        ToggleButton t = holder.getCheckedToggle();
        // turn off save
        t.setSaveEnabled(false);
        // get card view
        CardView card = holder.getCardView();
        // set on item click listener
        card.setOnClickListener(view -> {
            // toggle the toggle button on click
            t.toggle();
        });
        // On toggle button change
        t.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // On checked
                if (!checkedSongs.contains(song.id) ){
                    // if chcked Songs does not already contain the song, add it
                    checkedSongs.add(song.id);
                }
            } else {
                // On unchecked
                if (checkedSongs.contains(song.id) ){
                    // if chcked Songs does contain the song, remove it
                    checkedSongs.remove(song.id);
                }
            }
        });
    }

    public HashSet<Integer> getCheckedSongs() {
        // return checked songs
        return checkedSongs;
    }

    @Override
    public int getItemCount() {
        return songs.length;
    }

}
