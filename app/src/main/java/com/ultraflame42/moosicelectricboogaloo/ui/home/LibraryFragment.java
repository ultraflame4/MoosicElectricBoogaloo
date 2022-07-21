package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.FavouritesGridAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.GridSpacingItemDecoration;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.PlaylistListAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.SongsListAdapter;
import com.ultraflame42.moosicelectricboogaloo.ui.others.SearchActivity;
import com.ultraflame42.moosicelectricboogaloo.ui.playlist.PlaylistActivity;


public class LibraryFragment extends Fragment {

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Called everytime the fragment is navigated to
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // temp values for testing todo remove ltr
        String[] favItemNames = {"A","B","C","D"};
        FavouritesGridAdapter favouritesGridAdapter = new FavouritesGridAdapter(getContext(), favItemNames);
        RecyclerView favGridView = view.findViewById(R.id.FavouritesGrid);
        favGridView.addItemDecoration(new GridSpacingItemDecoration(2,16,false));
        favGridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favGridView.setAdapter(favouritesGridAdapter);


        PlaylistListAdapter playlistListAdapter = new PlaylistListAdapter(getContext(), this::openPlaylist);
        RecyclerView playlistListView = view.findViewById(R.id.playlist_list);

        playlistListView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistListView.setAdapter(playlistListAdapter);

        SongsListAdapter songsListAdapter = new SongsListAdapter(getContext());
        RecyclerView songsListView = view.findViewById(R.id.songs_list);
        songsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsListView.setAdapter(songsListAdapter);

        ImageButton imgBtn = view.findViewById(R.id.libSearchBtn);
        imgBtn.setOnClickListener(view1 -> handleSearchBtn());
        return view;
    }

    public void openPlaylist(int playlistId) {
        Intent intent = new Intent(getActivity(), PlaylistActivity.class);
        intent.putExtra("playlistId", playlistId);
        startActivity(intent);
    }

    public void handleSearchBtn() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

    }
}