package com.ultraflame42.moosicelectricboogaloo.adapters.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

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

    public CardView getCardView() {
        return cardView;
    }

    public void setSong(Song song, Context ctx){
//        songImg.setImage(song.getImage)
        UsefulStuff.LoadImageUriIntoImageView(song.getImageUriLink(), songImg);
        songName.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        songCount.setText(ctx.getString(R.string.songList_itemLength_text) + " " + song.getLengthFormatted());

    }
}
