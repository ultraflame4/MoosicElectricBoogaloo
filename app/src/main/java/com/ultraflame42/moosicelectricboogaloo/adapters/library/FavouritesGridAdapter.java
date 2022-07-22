package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;

public class FavouritesGridAdapter extends RecyclerView.Adapter<FavouritesGridAdapter.ViewHolder> {

    Context ctx;
    Integer[] favourites;

    /*
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imgBtn;
        private final TextView titleText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imgBtn = view.findViewById(R.id.favItemImageBtn);
            titleText = view.findViewById(R.id.favItemText);

        }

        public ImageButton getImgBtn() {
            return imgBtn;
        }

        public TextView getTitleText() {
            return titleText;
        }
    }

    public FavouritesGridAdapter(Context ctx) {
        this.ctx = ctx;
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
        SongPlaylist playlist = PlaylistRegistry.getInstance().getItem(favourites[position]);
        // todo do for image also
        holder.getTitleText().setText(playlist.getTitle());
    }

    @Override
    public int getItemCount() {
        return favourites.length;
    }

//    }
}
