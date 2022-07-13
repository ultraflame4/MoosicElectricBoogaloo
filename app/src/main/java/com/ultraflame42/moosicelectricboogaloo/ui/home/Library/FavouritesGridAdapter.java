package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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

    // method to update data
    public void updateData(String[] favouriteItemNames) {
        this.favouriteItemNames = favouriteItemNames;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.favourites_griditem, null);
        }


        //todo set image for fav item btn
        ImageButton favGridItemBtn = convertView.findViewById(R.id.favItemImageBtn);
        TextView favGridItemText = convertView.findViewById(R.id.favItemText);

        favGridItemText.setText(favouriteItemNames[i]);

        return convertView;
    }
}
