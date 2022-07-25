package com.ultraflame42.moosicelectricboogaloo.adapters.SearchResults;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.PlaylistItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.SongListItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.Song;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventFunctionCallback;
import com.ultraflame42.moosicelectricboogaloo.tools.registry.RegistryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        this.activity = activity;
        OnResultItemClicked = onResultItemClicked;
        searchNames = new ArrayList<>();
        if (INCLUDE_PLAYLIST) {
            searchNames = Stream.concat(searchNames.stream(), PlaylistRegistry.getInstance().getSearchNames().stream())
                    .collect(Collectors.toList());
        }
        if (INCLUDE_SONGS) {
            searchNames = Stream.concat(searchNames.stream(), SongRegistry.getInstance().getSearchNames().stream())
                    .collect(Collectors.toList());
        }
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
            HashSet<String> seen=new HashSet<>();
            results.removeIf(e->!seen.add(e.getUniqueFilterTargetString()));
            // return results that matches query
            return results.toArray(new SearchNameItem[0]);
        }

        @Override
        protected void onPostExecute(SearchNameItem[] searchNameItems) {
            OnSearchResults(searchNameItems);
        }
    }

    SearchTask previousTask = null;
    public void updateQuery(String query) {
        if(previousTask != null) {
            previousTask.cancel(true);
        }
        new SearchTask().execute(query);
    }

    public void OnSearchResults(SearchNameItem[] results) {
        searchResults= results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, parent, false);
            return new SongListItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
            return new PlaylistItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongListItemViewHolder) {
            SongListItemViewHolder viewHolder = (SongListItemViewHolder) holder;
            RegistryItem<Song> song = SongRegistry.getInstance().get(searchResults[position].targetRegId);

            viewHolder.setSong(song.item,activity);
            viewHolder.getCardView().setOnClickListener(v -> {
                OnResultItemClicked.call(searchResults[position]);
            });

        } else if (holder instanceof PlaylistItemViewHolder) {
            PlaylistItemViewHolder playlistItemViewHolder = (PlaylistItemViewHolder) holder;
            RegistryItem<SongPlaylist> playlist = PlaylistRegistry.getInstance().get(searchResults[position].targetRegId);

            playlistItemViewHolder.setPlaylist(playlist.item, activity);
            playlistItemViewHolder.getCardView().setOnClickListener(v -> {
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
