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
        for (int i = 0; i < position+1; i++) {
            spanCounter += layoutManager.getSpanSizeLookup().getSpanSize(i);
        }
        return spanCounter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int colPos = getSpanCountFromPosition(position) % spanCount + 1;
        Log.d("ITEM DECO", "Position " + position + " Cell" + getSpanCountFromPosition(position) + " Col" + colPos);
        if (colPos == spanCount) {
            outRect.right = spacing;
        }

        outRect.bottom = spacing;

    }


}