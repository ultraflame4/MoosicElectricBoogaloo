package com.ultraflame42.moosicelectricboogaloo.ui.home.Library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class FavouritesGridAdapter extends RecyclerView.Adapter<FavouritesGridAdapter.ViewHolder> {

    Context ctx;
    String[] favouriteItemNames;
    LayoutInflater inflater;
    //todo refrence from https://stackoverflow.com/questions/33337301/nested-scrollview-inside-gridview-place-is-taking-only-somepart#:~:text=You%20can%20put%20a%20GridView,for%20everything%20to%20display%20properly.
    //todo refrence from https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the

    /*
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imgBtn;
        private final TextView titleText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imgBtn = view.findViewById(R.id.favItemImageBtn);
            titleText = view.findViewById(R.id.favItemText);

        }

        public ImageButton getImgBtn() {
            return imgBtn;
        }

        public TextView getTitleText() {
            return titleText;
        }
    }

    public FavouritesGridAdapter(Context ctx, String[] favouriteItemNames) {
        this.ctx = ctx;
        this.favouriteItemNames = favouriteItemNames;
    }

    @NonNull
    @Override
    public FavouritesGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_griditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesGridAdapter.ViewHolder holder, int position) {
        holder.getTitleText().setText(favouriteItemNames[position]);
    }

    @Override
    public int getItemCount() {
        return favouriteItemNames.length;
    }

    //    @Override
//    public int getCount() {
//        return favouriteItemNames.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    // method to update data
//    public void updateData(String[] favouriteItemNames) {
//        this.favouriteItemNames = favouriteItemNames;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        if (inflater == null) {
//            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//        if (convertView == null){
//            convertView = inflater.inflate(R.layout.favourites_griditem, null);
//        }
//
//
//        //todo set image for fav item btn
//        ImageButton favGridItemBtn = convertView.findViewById(R.id.favItemImageBtn);
//        TextView favGridItemText = convertView.findViewById(R.id.favItemText);
//
//        favGridItemText.setText(favouriteItemNames[i]);
//
//        return convertView;
//    }
}
