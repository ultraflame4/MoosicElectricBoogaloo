package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;

public class FavouritesGridAdapter extends RecyclerView.Adapter<FavouritesGridAdapter.ViewHolder> {

    Context ctx;
    Integer[] favourites;
    private EventFunctionCallback<Integer> callback;

    /*
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgBtn;
        private final TextView titleText;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imgBtn = view.findViewById(R.id.favItemImageBtn);
            titleText = view.findViewById(R.id.favItemText);
            cardView = view.findViewById(R.id.cardView);

        }

        public ImageView getImgBtn() {
            return imgBtn;
        }

        public TextView getTitleText() {
            return titleText;
        }

        public CardView getCardView() {
            return cardView;
        }
    }


    public FavouritesGridAdapter(Context ctx, EventFunctionCallback<Integer> callback) {
        this.ctx = ctx;
        this.callback = callback;
        favourites = new Integer[0];
    }

    public void updateData(Integer[] favourites) {
        this.favourites = favourites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_griditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesGridAdapter.ViewHolder holder, int position) {
        int playlistIndex = favourites[position];
        SongPlaylist playlist = PlaylistRegistry.getInstance().getItem(playlistIndex);
        // todo do for image also
        holder.getTitleText().setText(playlist.getTitle());
        holder.getCardView().setOnClickListener(view -> {

            callback.call(playlistIndex);
        });
    }

    @Override
    public int getItemCount() {
        return favourites.length;
    }
}
