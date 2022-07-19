package com.ultraflame42.moosicelectricboogaloo.adapters.Playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class PlaylistRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    final int itemPositionStartOffset = 2; // item positions start at #2

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View parent) {
            super(parent);
        }
    }

    public PlaylistRecylerViewAdapter() {
    }

    @NonNull
    @Override
    public PlaylistRecylerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.songlist_item, parent, false);

        return new PlaylistRecylerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 10; //todo change ltr
    }
}
