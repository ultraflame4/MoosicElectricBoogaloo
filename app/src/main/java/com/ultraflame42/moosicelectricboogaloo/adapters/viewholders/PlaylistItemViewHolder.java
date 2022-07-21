package com.ultraflame42.moosicelectricboogaloo.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.ultraflame42.moosicelectricboogaloo.R;

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
