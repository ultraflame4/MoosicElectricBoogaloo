package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.FavouritesGridAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.GridSpacingItemDecoration;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.PlaylistListAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.SongsListAdapter;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.SearchTool;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;
import com.ultraflame42.moosicelectricboogaloo.ui.others.SearchActivity;
import com.ultraflame42.moosicelectricboogaloo.ui.others.PlaylistActivity;


public class LibraryFragment extends Fragment {

    private PlaylistRegistry playlistRegistry;
    private EventListenerGroup listenerGroup = new EventListenerGroup();
    private FavouritesGridAdapter favouritesGridAdapter;
    private PlaylistListAdapter playlistListAdapter;
    private SongsListAdapter songsListAdapter;
    private SongRegistry songRegistry;

    public LibraryFragment() {
        // Required empty public constructor
    }


    ActivityResultLauncher<Intent> SearchActivityIntentLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Called everytime the fragment is navigated to
        super.onCreate(savedInstanceState);
        playlistRegistry = PlaylistRegistry.getInstance();
        songRegistry = SongRegistry.getInstance();
        // create activity launcher for search activity
        SearchActivityIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // on results from search activity...
            Intent data = result.getData();
            if (data == null) {
                Log.w("LibraryFragment", "Search Activity did not return data");
                return;
            }
            int itemType = data.getIntExtra("itemType", -1);
            int itemId = data.getIntExtra("itemId", -1);
            SearchTool.getInstance().handleOnSearchItemSelected(itemType, itemId, getContext());

        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        favouritesGridAdapter = new FavouritesGridAdapter(getContext(), playlistIndex -> {
            // when item clicked open playlist preveiw
            openPlaylist(playlistIndex);
        });
        RecyclerView favGridView = view.findViewById(R.id.FavouritesGrid);
        // Add spacing for items using item decoration
        GridLayoutManager layout = new GridLayoutManager(getContext(), 2);
        favGridView.setLayoutManager(layout);
        favGridView.addItemDecoration(new GridSpacingItemDecoration( 16, layout));
        favGridView.setAdapter(favouritesGridAdapter);
        listenerGroup.subscribe(playlistRegistry.OnFavouritesUpdate, data -> {
            favouritesGridAdapter.updateData(playlistRegistry.getFavourites().toArray(new Integer[0]));
        });
        favouritesGridAdapter.updateData(playlistRegistry.getFavourites().toArray(new Integer[0]));

        // Playlist
        playlistListAdapter = new PlaylistListAdapter(getContext(), this::openPlaylist);
        RecyclerView playlistListView = view.findViewById(R.id.playlist_list);
        playlistListView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistListView.setAdapter(playlistListAdapter);
        listenerGroup.subscribe(playlistRegistry.OnItemsUpdate, data -> {
            playlistListAdapter.updateData();
        });
        playlistListAdapter.updateData();

        // Songlist
        songsListAdapter = new SongsListAdapter(getContext());
        RecyclerView songsListView = view.findViewById(R.id.songs_list);
        songsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsListView.setAdapter(songsListAdapter);
        listenerGroup.subscribe(songRegistry.OnItemsUpdate, data -> {
            Log.d("LibraryFragment", "Song list updating...");
            songsListAdapter.updateData();
        });

        songsListAdapter.updateData();

        // Search button
        ImageButton libSearchBtn = view.findViewById(R.id.libSearchBtn);
        libSearchBtn.setOnClickListener(view1 -> handleSearchBtn());

        // AddToLib btn
        ImageButton addToLibBtn = view.findViewById(R.id.addToLibBtn);
        addToLibBtn.setOnClickListener(view1 -> showAddToLibDialog());

        return view;
    }

    public void openPlaylist(int playlistId) {
        Log.d("LibraryFragment", "Opening playlist " + playlistId);
        Intent intent = new Intent(getActivity(), PlaylistActivity.class);
        intent.putExtra("playlistId", playlistId);
        startActivity(intent);
    }

    public void handleSearchBtn() {
        SearchActivityIntentLauncher.launch(new Intent(getActivity(), SearchActivity.class));
    }

    public void showAddToLibDialog() {
        NavHostFragment.findNavController(this).navigate(R.id.action_libraryFragment_to_libAddItemDialog);
    }

    @Override
    public void onDestroy() {
        Log.d("LibraryFragment", "onDestroy: unsubbing all listeners");
        listenerGroup.unsubscribeAll();
        super.onDestroy();
    }
}