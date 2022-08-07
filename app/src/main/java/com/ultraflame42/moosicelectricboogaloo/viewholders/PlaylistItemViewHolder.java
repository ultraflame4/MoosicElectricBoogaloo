package com.ultraflame42.moosicelectricboogaloo.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;

public class PlaylistItemViewHolder extends RecyclerView.ViewHolder {
    private final ShapeableImageView playlistImg;
    private final TextView playlistName;
    private final TextView playlistCreator;
    private final TextView playlistSongCount;
    private final TextView playlistLength;
    private final CardView cardView;

    public PlaylistItemViewHolder(View view) {
        super(view);
        // Define click listener for the ViewHolder's View
        playlistImg = view.findViewById(R.id.playlistItem_image);
        playlistName = view.findViewById(R.id.playlist_title);
        playlistCreator = view.findViewById(R.id.playlist_creator);
        playlistSongCount = view.findViewById(R.id.playlist_songcount);
        playlistLength = view.findViewById(R.id.playlist_totallength);
        cardView = view.findViewById(R.id.playlist_itemcard);

    }


    public CardView getCardView() {
        return cardView;
    }

    public void setPlaylist(SongPlaylist playlist, Context ctx) {
        playlist.loadCoverIntoImageView(playlistImg);
        playlistName.setText(playlist.getTitle());
        playlistCreator.setText(playlist.getCreator());
        playlistSongCount.setText(ctx.getString(R.string.playlist_songcount_text) + " " + playlist.getSongCount());
        playlistLength.setText(ctx.getString(R.string.playlist_totallength_text) + " " + playlist.getLengthFormatted());
    }
}
