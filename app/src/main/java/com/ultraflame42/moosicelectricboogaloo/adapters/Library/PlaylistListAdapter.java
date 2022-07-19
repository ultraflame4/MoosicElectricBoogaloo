package com.ultraflame42.moosicelectricboogaloo.adapters.Library;

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
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventCallbackListener;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryUpdateData;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {
    Context ctx;
    RegistryItem<SongPlaylist>[] playlists;
    LayoutInflater inflater;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView playlistImg;
        private final TextView playlistName;
        private final TextView playlistCreator;
        private final TextView playlistSongCount;
        private final TextView playlistLength;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            playlistImg = view.findViewById(R.id.playlistItem_image);
            playlistName = view.findViewById(R.id.playlist_title);
            playlistCreator = view.findViewById(R.id.playlist_creator);
            playlistSongCount = view.findViewById(R.id.playlist_songcount);
            playlistLength = view.findViewById(R.id.playlist_totallength);
            cardView = view.findViewById(R.id.playlist_itemcard);

        }

        public ShapeableImageView getPlaylistImg() {
            return playlistImg;
        }


        public TextView getPlaylistName() {
            return playlistName;
        }

        public TextView getPlaylistCreator() {
            return playlistCreator;
        }

        public TextView getPlaylistSongCount() {
            return playlistSongCount;
        }

        public TextView getPlaylistLength() {
            return playlistLength;
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    EventCallbackListener<RegistryUpdateData<SongPlaylist>> onPlaylistAddedListener;
    private EventFunctionCallback<Integer> OnPlaylistClickedCallback;

    public PlaylistListAdapter(Context ctx, EventFunctionCallback<Integer> onPlaylistClickedCallback) {
        this.ctx = ctx;
        OnPlaylistClickedCallback = onPlaylistClickedCallback;
        this.playlists = SongRegistry.playlists.getAllItems();
        onPlaylistAddedListener = SongRegistry.playlists.OnItemsUpdate.addListener(playlist -> {
            Log.d("PlaylistListAdapter", "SongRegistry playlists updated, updating list");
            playlists = SongRegistry.playlists.getAllItems();
            notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public PlaylistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
        return new PlaylistListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistListAdapter.ViewHolder holder, int i) {
        SongPlaylist playlist = playlists[i].item;
        holder.getPlaylistName().setText(playlist.getTitle());
        holder.getPlaylistCreator().setText(playlist.getCreator());
        holder.getPlaylistSongCount().setText(ctx.getString(R.string.playlist_songcount_text) + " " + playlist.getSongCount());
        holder.getPlaylistLength().setText(ctx.getString(R.string.playlist_totallength_text) + " " + playlist.getLength());
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
