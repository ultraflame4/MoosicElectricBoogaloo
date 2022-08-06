package com.ultraflame42.moosicelectricboogaloo.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.SearchFragmentContentAdapter;
import com.ultraflame42.moosicelectricboogaloo.adapters.library.GridSpacingItemDecoration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        RecyclerView content = view.findViewById(R.id.content);
        // positions where the sectionHeaders are placed
        // value is title of sections
        HashMap<Integer, String> sectionHeaders = new HashMap<>();
        sectionHeaders.put(0, "Recent Searches");
        sectionHeaders.put(5, "Recommended");
//        sectionHeaders.put(10, "Genre"); feature removed due to time constrain

        // layout manager for recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        // set it so that in the section header positions, the span size of the items are 2. Taking up the full width
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (sectionHeaders.containsKey(position)) {
                    return 2;
                }
                return 1;
            }
        });

        content.setLayoutManager(layoutManager);
        content.addItemDecoration(new GridSpacingItemDecoration( 16, layoutManager));
        content.setAdapter(new SearchFragmentContentAdapter(getContext(), sectionHeaders));



    }
}