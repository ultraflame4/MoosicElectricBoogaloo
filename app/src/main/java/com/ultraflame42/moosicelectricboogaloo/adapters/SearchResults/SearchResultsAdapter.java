package com.ultraflame42.moosicelectricboogaloo.adapters.SearchResults;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.SearchTool;
import com.ultraflame42.moosicelectricboogaloo.viewholders.PlaylistItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchNameItem> searchNames;

    private SearchNameItem[] searchResults = new SearchNameItem[0];

    private Activity activity;

    private EventFunctionCallback<SearchNameItem> OnResultItemClicked;

    public boolean INCLUDE_PLAYLIST = true;
    public boolean INCLUDE_SONGS = true;

    public SearchResultsAdapter(Activity activity, EventFunctionCallback<SearchNameItem> onResultItemClicked) {
        // Set the constructor variables and field
        this.activity = activity;
        OnResultItemClicked = onResultItemClicked;

        // initialise new empty array for search names
        searchNames = new ArrayList<>();
        // If includfe playlist, add playlist search names to search names arraylist
        if (INCLUDE_PLAYLIST) {
            searchNames = Stream.concat(searchNames.stream(), PlaylistRegistry.getInstance().getSearchNames().stream())
                    .collect(Collectors.toList());
        }
        // If include playlist, add songs search names to search names arraylist
        if (INCLUDE_SONGS) {
            searchNames = Stream.concat(searchNames.stream(), SongRegistry.getInstance().getSearchNames().stream())
                    .collect(Collectors.toList());
        }
        // empty query so that it list all songs
        updateQuery("");
    }

    private class SearchTask extends AsyncTask<String,Integer,SearchNameItem[]>{
        @Override
        protected SearchNameItem[] doInBackground(String... strings) {
            String query = strings[0];
            Log.d("SearchResultsAdapter", "Searching for " + query);
            List<SearchNameItem> results = searchNames.stream()
                    .filter(item ->
                            // substring matching
                            // force lowercase for case insensitivity
                            item.name.toLowerCase().contains(query.toLowerCase())
                    )
                    // turn to list
                    .collect(Collectors.toList());

            // Filter duplicate results. (Results that point to same target)
            SearchTool.getInstance().RemoveDuplicateSearchNames(results);
            // return results that matches query
            return results.toArray(new SearchNameItem[0]);
        }

        @Override
        protected void onPostExecute(SearchNameItem[] searchNameItems) {
            // call callback function with results
            OnSearchResults(searchNameItems);
        }
    }


    SearchTask previousTask = null;
    public void updateQuery(String query) {
        // on query update, cancel previous task
        if(previousTask != null) {
            previousTask.cancel(true);
        }
        // execute new task to search
        new SearchTask().execute(query);
    }

    public void OnSearchResults(SearchNameItem[] results) {
        // set search results to new results
        searchResults= results;
        // update list
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            // If view type is 0, inflate the songlist_item layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
            return new SongListItemViewHolder(view);
        } else {
            // else inflate the playlist item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
            return new PlaylistItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongListItemViewHolder) {
            // if is a song item
            // cast to correct type
            SongListItemViewHolder viewHolder = (SongListItemViewHolder) holder;
            RegistryItem<Song> song = SongRegistry.getInstance().get(searchResults[position].targetRegId);

            viewHolder.setSong(song.item,activity);
            viewHolder.getCardView().setOnClickListener(v -> {
                SearchTool.getInstance().addToRecentSearch(searchResults[position]);
                OnResultItemClicked.call(searchResults[position]);
            });

        } else if (holder instanceof PlaylistItemViewHolder) {
            // if is a playlist item
            // cast to correct type
            PlaylistItemViewHolder playlistItemViewHolder = (PlaylistItemViewHolder) holder;
            // get playlist from registry using current position
            RegistryItem<SongPlaylist> playlist = PlaylistRegistry.getInstance().get(searchResults[position].targetRegId);
            // set playlist for view holder, it should auto set the info for all the views
            playlistItemViewHolder.setPlaylist(playlist.item, activity);
            // set on item clicked listener
            playlistItemViewHolder.getCardView().setOnClickListener(v -> {
                // Add to recent searches on click
                SearchTool.getInstance().addToRecentSearch(searchResults[position]);
                // Call the callback function with the playlist
                OnResultItemClicked.call(searchResults[position]);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (searchResults[position].type) {
            case SONG:
                return 0;
            case PLAYLIST:
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.length;
    }
}
