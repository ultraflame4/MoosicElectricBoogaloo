package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class FavouritesGridAdapter extends BaseAdapter {

    Context ctx;
    String[] favouriteItemNames;
    LayoutInflater inflater;

    public FavouritesGridAdapter(Context ctx, String[] favouriteItemNames) {
        this.ctx = ctx;
        this.favouriteItemNames = favouriteItemNames;
    }


    @Override
    public int getCount() {
        return favouriteItemNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.favourites_griditem, null);
        }

        TextView favGridItem = convertView.findViewById(R.id.favItemText);
        ImageView favGridImage = convertView.findViewById(R.id.favItemImage);

        favGridItem.setText(favouriteItemNames[i]);

        return convertView;
    }
}
