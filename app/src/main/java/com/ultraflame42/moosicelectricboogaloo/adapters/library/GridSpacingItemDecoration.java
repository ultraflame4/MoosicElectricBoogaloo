package com.ultraflame42.moosicelectricboogaloo.adapters.library;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private GridLayoutManager layoutManager;

    public GridSpacingItemDecoration(int spacing, GridLayoutManager layoutManager) {
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
        spanCount = layoutManager.getSpanCount();
    }

    /**
     * Returns the total span count for all items including the current position
     *
     * The resulting span count is can be used as the grid cell index.
     *
     * note the cell index will always be the last index of the cell the item takes up
     * Eg. item span size is 2 and takes up cell 1 & 2. the cell index wld be 2
     * @param position
     * @return
     */
    public int getSpanCountFromPosition(int position) {
        int spanCounter = 0;
        // loop through all previous positions
        for (int i = 0; i < position+1; i++) {
            // add their span to the span counter. 1 span = 1 cell. so in the end we get total number of cells used before + by the current item
            spanCounter += layoutManager.getSpanSizeLookup().getSpanSize(i);
        }
        // return it
        return spanCounter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // get current item position
        int position = parent.getChildAdapterPosition(view); // item position
        // calculate the column position.
        int colPos = getSpanCountFromPosition(position) % spanCount;
        // check if column the item is in is the rightest most column.
        if (colPos == spanCount-1) { // minus 1 because column position starts from 0
            // if not rightest most, add right spacing
            outRect.right = spacing;
        }

        outRect.bottom = spacing;

    }


}