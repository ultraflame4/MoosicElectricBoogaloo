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
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.HashSet;
import java.util.List;

public class AddSongPlaylistAdapter extends RecyclerView.Adapter<AddSongPlaylistAdapter.ViewHolder> {
    private Context ctx;
    private RegistryItem<Song>[] songs;

    private SongRegistry songRegistry;

    private final HashSet<Integer> checkedSongs;

    public static class ViewHolder extends SongListItemViewHolder {
        private final ToggleButton checkedToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkedToggle = itemView.findViewById(R.id.checkedToggle);
        }

        public ToggleButton getCheckedToggle() {
            return checkedToggle;
        }
    }

    public AddSongPlaylistAdapter(Context ctx) {
        this.ctx = ctx;
        songRegistry = SongRegistry.getInstance();
        songs = new RegistryItem[0];
        checkedSongs = new HashSet<>();
    }

    public void updateData() {
        Log.d("SongsListAdapter", "SongRegistry updated, updating list");
        songs = songRegistry.getAllItems();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_song_playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistryItem<Song> song = songs[position];
        holder.setSong(song.item, ctx);
        ToggleButton t = holder.getCheckedToggle();
        t.setSaveEnabled(false);
        CardView card = holder.getCardView();
        card.setOnClickListener(view -> {
            t.toggle();
        });
        t.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (!checkedSongs.contains(song.id) ){
                    checkedSongs.add(song.id);
                }
            } else {
                if (checkedSongs.contains(song.id) ){
                    checkedSongs.remove(song.id);
                }
            }
        });
    }

    public HashSet<Integer> getCheckedSongs() {
        return checkedSongs;
    }

    @Override
    public int getItemCount() {
        return songs.length;
    }

}
