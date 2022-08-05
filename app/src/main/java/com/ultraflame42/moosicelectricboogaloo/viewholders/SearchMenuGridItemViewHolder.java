package com.ultraflame42.moosicelectricboogaloo.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class SearchMenuGridItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView text;
    private final ImageView bgImage;

    public SearchMenuGridItemViewHolder(View view) {
        super(view);
        this.text = view.findViewById(R.id.text);
        this.bgImage = view.findViewById(R.id.bgimage);
    }

    public ImageView getBgImage() {
        return bgImage;
    }

    public TextView getText() {
        return text;
    }
}
