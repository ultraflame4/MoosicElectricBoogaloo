package com.ultraflame42.moosicelectricboogaloo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SearchMenuGridItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SectionTitleViewHolder;

import java.util.HashMap;
import java.util.List;

public class SearchFragmentContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private HashMap<Integer,String> sectionHeaders;

    public SearchFragmentContentAdapter(Context context, HashMap<Integer,String> sectionHeaderPositions) {
        this.context = context;
        this.sectionHeaders = sectionHeaderPositions;
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionHeaders.containsKey(position)) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            // Return section title view holder
            return SectionTitleViewHolder.from(parent);
        } else {
            // return search menu grid item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_fragment_content_item, parent, false);
            return new SearchMenuGridItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchMenuGridItemViewHolder) {
            // Bind search menu grid item
            SearchMenuGridItemViewHolder searchMenuGridItemViewHolder = (SearchMenuGridItemViewHolder) holder;
            searchMenuGridItemViewHolder.getText().setText("Search Menu Grid Item  #" +position);
            searchMenuGridItemViewHolder.getBgImage().setImageResource(R.drawable.color_a_bg);
        } else if (holder instanceof SectionTitleViewHolder) {
            // Bind section title
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.setTitle(sectionHeaders.get(position));
        }
    }

    @Override
    public int getItemCount() {
        // 2 section title + 2 x section items (4)
        return 2+2*4;
    }
}
