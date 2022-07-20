package com.ultraflame42.moosicelectricboogaloo.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.ultraflame42.moosicelectricboogaloo.R;

public class SongListItemViewHolder extends RecyclerView.ViewHolder {


    private final ShapeableImageView songImg;
    private final TextView songName;
    private final TextView songArtist;
    private final TextView songCount;
    private final CardView cardView;


    public SongListItemViewHolder(View view) {
        super(view);
        cardView = view.findViewById(R.id.songlist_itemcard);
        songImg = view.findViewById(R.id.songList_itemImage);
        songName = view.findViewById(R.id.songList_itemTitle);
        songArtist = view.findViewById(R.id.songList_itemArtist);
        songCount = view.findViewById(R.id.songList_itemLength);
    }

    public ShapeableImageView getSongImg() {
        return songImg;
    }

    public TextView getSongName() {
        return songName;
    }

    public TextView getSongArtist() {
        return songArtist;
    }

    public TextView getSongCount() {
        return songCount;
    }

    public CardView getCardView() {
        return cardView;
    }
}
