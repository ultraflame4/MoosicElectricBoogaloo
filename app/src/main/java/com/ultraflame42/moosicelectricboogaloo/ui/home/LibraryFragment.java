package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlaylist;
import com.ultraflame42.moosicelectricboogaloo.ui.home.Library.FavouritesGridAdapter;
import com.ultraflame42.moosicelectricboogaloo.ui.home.Library.PlaylistListAdapter;


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
        GridView favGridView = view.findViewById(R.id.FavouritesGrid);
        favGridView.setAdapter(favouritesGridAdapter);
        // temp values for testing todo remove ltr


        SongPlaylist[] tempPlaylists = {
                new SongPlaylist("Creator 1", "Title 1"),
                new SongPlaylist("Creator 2", "Title 2"),
                new SongPlaylist("Creator 3", "Title 3"),
        };
        PlaylistListAdapter playlistListAdapter = new PlaylistListAdapter(getContext(), tempPlaylists);
        ListView playlistListView = view.findViewById(R.id.playlist_list);
        playlistListView.setAdapter(playlistListAdapter);


        return view;
    }
}