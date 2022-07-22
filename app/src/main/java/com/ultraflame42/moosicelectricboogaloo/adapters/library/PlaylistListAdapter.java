package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.PlaylistItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventCallbackListener;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryUpdateData;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistItemViewHolder> {
    Context ctx;
    RegistryItem<SongPlaylist>[] playlists;

    EventCallbackListener<RegistryUpdateData<SongPlaylist>> onPlaylistAddedListener;
    private EventFunctionCallback<Integer> OnPlaylistClickedCallback;
    PlaylistRegistry playlistRegistry;

    public PlaylistListAdapter(Context ctx, EventFunctionCallback<Integer> onPlaylistClickedCallback) {
        this.ctx = ctx;
        OnPlaylistClickedCallback = onPlaylistClickedCallback;
        playlistRegistry = PlaylistRegistry.getInstance();
        this.playlists = playlistRegistry.getAllItems();
        onPlaylistAddedListener = playlistRegistry.OnItemsUpdate.addListener(playlist -> {
            Log.d("PlaylistListAdapter", "SongRegistry playlists updated, updating list");
            playlists = playlistRegistry.getAllItems();
            notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public PlaylistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
        return new PlaylistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistItemViewHolder holder, int i) {
        SongPlaylist playlist = playlists[i].item;
        holder.setPlaylist(playlist, ctx);
        holder.getCardView().setOnClickListener(view -> {
            onItemClick(view,i);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.length;
    }



    public void onItemClick(View view, int position) {
        // TODO:IMPORTANT NOTE THIS SHOULD NOT PLAY THE PLAYLIST, BUT  OPEN THE PREVIEW PAGE FOR THE PLAYLIST
        RegistryItem<SongPlaylist> playlist = playlists[position];
        OnPlaylistClickedCallback.call(playlist.id);

    }

    //    @Override
//    public int getCount() {
//        return playlists.length;
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
//
//        if (inflater == null) {
//            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.playlist_listitem, null);
//        }
//        ImageView playlistImage = convertView.findViewById(R.id.playlistItem_image);
//        TextView playlistName = convertView.findViewById(R.id.playlist_title);
//        TextView playlistCreator = convertView.findViewById(R.id.playlist_creator);
//        TextView playlistSongCount = convertView.findViewById(R.id.playlist_songcount);
//        TextView playlistTotalLength = convertView.findViewById(R.id.playlist_totallength);
//
//        playlistName.setText(playlists[i].getTitle());
//        playlistCreator.setText(playlists[i].getCreator());
//        playlistSongCount.setText(ctx.getString(R.string.playlist_songcount_text)+" "+playlists[i].getSongCount());
//        playlistTotalLength.setText(ctx.getString(R.string.playlist_totallength_text)+" "+playlists[i].getLength());
//
//
//        return convertView;
//    }
}
