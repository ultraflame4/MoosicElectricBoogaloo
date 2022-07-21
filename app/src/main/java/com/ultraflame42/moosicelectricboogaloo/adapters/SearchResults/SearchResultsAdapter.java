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

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.viewholders.PlaylistItemViewHolder;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SearchNameItem[] searchNames;

    private SearchNameItem[] searchResults = new SearchNameItem[0];

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    Handler mHandler = new Handler(Looper.getMainLooper());
    private Activity activity;


    public SearchResultsAdapter(Activity activity) {
        this.activity = activity;
        searchNames = SongRegistry.GetSearchNames();
        updateQuery("");
    }

    private class SearchTask extends AsyncTask<String,Integer,SearchNameItem[]>{
        @Override
        protected SearchNameItem[] doInBackground(String... strings) {
            String query = strings[0];
            Log.d("SearchResultsAdapter", "Searching for " + query);
            List<SearchNameItem> results = Arrays.stream(searchNames)
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listitem, parent, false);
        return new PlaylistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return searchResults.length;
    }
}
