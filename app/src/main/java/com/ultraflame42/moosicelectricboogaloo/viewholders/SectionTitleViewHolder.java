package com.ultraflame42.moosicelectricboogaloo.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class SectionTitleViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    /**
     * Don't use this constructor directly. Use {@link #from(ViewGroup parent)} instead.
     * @param itemView
     */
    public SectionTitleViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.section_title);
    }

    /**
     * Creates returns a new instance of {@link SectionTitleViewHolder} from the given {@link ViewGroup}.
     *
     * Also inflates the layout for the SectionTitle.
     * @param parent
     * @return
     */
    public static SectionTitleViewHolder from(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_title, parent, false);
        return new SectionTitleViewHolder(view);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
