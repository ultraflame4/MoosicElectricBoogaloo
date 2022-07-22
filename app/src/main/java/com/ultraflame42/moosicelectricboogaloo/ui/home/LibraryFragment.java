package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.FavouritesGridAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.GridSpacingItemDecoration;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.PlaylistListAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.SongsListAdapter;
import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.songs.PlaylistRegistry;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
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
        SearchActivityIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (data == null) {
                Log.w("LibraryFragment", "Search Activity did not return data");
                return;
            }

            int itemType = data.getIntExtra("itemType", -1);
            int itemId = data.getIntExtra("itemId", -1);
            if (itemType < 0 || itemId < 0) {
                Log.w("LibraryFragment", "WARNING Search activity returned invalid data");
                return;
            }

            if (itemType == ResultItemType.SONG.ordinal()) {
                SongPlayer.PlaySong(itemId);
            } else if (itemType == ResultItemType.PLAYLIST.ordinal()) {
                // playlist
                openPlaylist(itemId);
            }

        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        favouritesGridAdapter = new FavouritesGridAdapter(getContext());
        RecyclerView favGridView = view.findViewById(R.id.FavouritesGrid);
        favGridView.addItemDecoration(new GridSpacingItemDecoration(2, 16, false));
        favGridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
            songsListAdapter.updateData();
        });
        songsListAdapter.updateData();

        // Search button
        ImageButton libSearchBtn = view.findViewById(R.id.libSearchBtn);
        libSearchBtn.setOnClickListener(view1 -> handleSearchBtn());

        // AddToLib btn
        ImageButton addToLibBtn = view.findViewById(R.id.addToLibBtn);
        addToLibBtn.setOnClickListener(view1 -> handleAddToLibBtn());

        return view;
    }

    public void openPlaylist(int playlistId) {
        Intent intent = new Intent(getActivity(), PlaylistActivity.class);
        intent.putExtra("playlistId", playlistId);
        startActivity(intent);
    }

    public void handleSearchBtn() {
        SearchActivityIntentLauncher.launch(new Intent(getActivity(), SearchActivity.class));
    }

    public void handleAddToLibBtn() {
        final Dialog dialog = new Dialog(getContext());
        // Disable the default title bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Allow user to tap outside and cancel
        dialog.setCancelable(true);
        // Set layout
        dialog.setContentView(R.layout.library_add_dialog);
        // make bg transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //todo bind buttons

        dialog.show();
    }

    @Override
    public void onDestroy() {
        Log.d("LibraryFragment", "onDestroy: unsubbing all listeners");
        listenerGroup.unsubscribeAll();
        super.onDestroy();
    }
}