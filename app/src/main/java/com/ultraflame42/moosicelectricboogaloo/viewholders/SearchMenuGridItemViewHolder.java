package com.ultraflame42.moosicelectricboogaloo.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class SearchMenuGridItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView text;
    private final ImageView bgImage;
    private final CardView cardView;

    public SearchMenuGridItemViewHolder(View view) {
        super(view);
        this.cardView = view.findViewById(R.id.cardView);
        this.text = view.findViewById(R.id.text);
        this.bgImage = view.findViewById(R.id.bgimage);
    }

    public ImageView getBgImage() {
        return bgImage;
    }

    public TextView getText() {
        return text;
    }

    public CardView getCardView() {
        return cardView;
    }
}
