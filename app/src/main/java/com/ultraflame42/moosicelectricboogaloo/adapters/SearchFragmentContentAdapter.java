package com.ultraflame42.moosicelectricboogaloo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.SearchTool;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SearchMenuGridItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SectionTitleViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SearchFragmentContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private HashMap<Integer, String> sectionHeaders;
    private List<SearchNameItem> recommendedSongs;

    public SearchFragmentContentAdapter(Context context, HashMap<Integer, String> sectionHeaderPositions) {
        this.context = context;
        this.sectionHeaders = sectionHeaderPositions;
        // generate recommened  songs

        // get all searchnames
        recommendedSongs = UsefulStuff.getAllSearchNames();
        // remove any duplicates
        SearchTool.getInstance().RemoveDuplicateSearchNames(recommendedSongs);
        // randomise it. To get "recommended" songs
        Collections.shuffle(recommendedSongs);

    }

    @Override
    public int getItemViewType(int position) {
        // iif is a section haeder, return 1
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

    private int recentItemCounter = 0;
    private int recommendedItemCounter = 0;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchMenuGridItemViewHolder) {
            // Bind search menu grid item
            SearchMenuGridItemViewHolder searchMenuGridItemViewHolder = (SearchMenuGridItemViewHolder) holder;
            // set b ackground to color a first.
            searchMenuGridItemViewHolder.getBgImage().setImageResource(R.drawable.color_a_bg);
            if ((position > 0) && (position <= SearchTool.MAX_RECENT_SEARCHES + 1)) {
                // if position is in range 1-recentSearches.length. is recent searches.
                SearchNameItem[] recentSearches = SearchTool.getInstance().getRecentSearches();
                Log.d("SearchFragmentContentAdapter", "Getting recent items " + recentSearches.length);
                // Check if the recentItemCounter is less than the total number of recenntItems.
                if (recentItemCounter <= recentSearches.length - 1) {
                    // Set tge recent item information
                    // set the text
                    searchMenuGridItemViewHolder.getText().setText(recentSearches[recentItemCounter].name);
                    // set the onclick
                    searchMenuGridItemViewHolder.getCardView().setOnClickListener(view -> {
                        SearchTool.getInstance().handleOnSearchItemSelected(recentSearches[recentItemCounter], context);
                    });
                } else {
                    // set default text
                    searchMenuGridItemViewHolder.getText().setText("n/a");
                }

                recentItemCounter++;

                // if not recent item, it is recommended items
            } else if (recommendedItemCounter <= recommendedSongs.size() - 1) {
                // if position is in range, then get recommended songs
                SearchNameItem searchNameItem = recommendedSongs.get(recommendedItemCounter);
                // set the text
                searchMenuGridItemViewHolder.getText().setText(searchNameItem.name);
                // set the onclick
                searchMenuGridItemViewHolder.getCardView().setOnClickListener(view -> {
                    SearchTool.getInstance().handleOnSearchItemSelected(searchNameItem, context);
                });
                recommendedItemCounter++;
            }
            else {
                // else set default text
                searchMenuGridItemViewHolder.getText().setText("n/a");
            }


        } else if (holder instanceof SectionTitleViewHolder) {
            // if section title
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            // set the title for the section
            sectionTitleViewHolder.setTitle(sectionHeaders.get(position));
        }
    }

    @Override
    public int getItemCount() {
        // 2 section title + recent searches + recommended
        return 2 + SearchTool.MAX_RECENT_SEARCHES + 6;
    }
}
