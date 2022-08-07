package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        private final ImageView imgView;
        private final TextView titleText;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Set the variables to the respective views
            imgView = view.findViewById(R.id.favItemImageBtn);
            titleText = view.findViewById(R.id.favItemText);
            cardView = view.findViewById(R.id.cardView);

        }

        // Returns the view for the image
        public ImageView getImgView() {
            return imgView;
        }

        // Returns the view for the title text
        public TextView getTitleText() {
            return titleText;
        }

        // Returns the view for card view (the whole item)
        public CardView getCardView() {
            return cardView;
        }
    }


    public FavouritesGridAdapter(Context ctx, EventFunctionCallback<Integer> callback) {
        // Set the context variavle to the context passed in the constructor
        this.ctx = ctx;
        // Set the callback variable to the callback passed in the constructor
        this.callback = callback;
        // Set the favourites variable an empty array
        favourites = new Integer[0];
    }

    // UpdaTes data in the adapter
    public void updateData(Integer[] favourites) {
        // Set the favourites variable to the favourites array passed in the method
        this.favourites = favourites;
        // Update list
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_griditem, parent, false);
        return new ViewHolder(view); // return view holder for the view
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesGridAdapter.ViewHolder holder, int position) {
        // Get the playlist index for the current position
        int playlistIndex = favourites[position];
        // get the playlist from the registry using the playlist index
        SongPlaylist playlist = PlaylistRegistry.getInstance().getItem(playlistIndex);
        // Load the playlist cover image into the image view
        playlist.loadCoverIntoImageView(holder.getImgView());
        // Set the title text to the playlist title
        holder.getTitleText().setText(playlist.getTitle());
        // Set the on click listener for the card view
        holder.getCardView().setOnClickListener(view -> {
            // on item clicked, call the callback function with the playlist index
            callback.call(playlistIndex);
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the favourites array so that the recycler view knows how many items to display
        return favourites.length;
    }
}
