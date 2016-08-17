package com.example.codingtest.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codingtest.MainActivity;
import com.example.codingtest.R;
import com.example.codingtest.beans.Fact;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter class that will create and bind json data to the list view.
 * It is using the latest RecyclerView.Adapter for better performance
 */

public class FactsListAdapter extends RecyclerView.Adapter<FactsListAdapter.FactsListObjectHolder> {

    private MainActivity mParentActivity;
    List<Fact> mDataSet;

    public FactsListAdapter(MainActivity context, List<Fact> dataSet) {
        mParentActivity = context;
        mDataSet = new ArrayList<>(dataSet);
    }

    public void setModels(List<Fact> dataSet) {
        mDataSet = new ArrayList<>(dataSet);
    }

    public class FactsListObjectHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        ImageView mThumbnailImageView;

        public FactsListObjectHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.item_description);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_thumbnail);
        }
    }

    @Override
    public FactsListObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mParentActivity).inflate(R.layout.list_item_layout, parent, false);
        FactsListObjectHolder objectHolder = new FactsListObjectHolder(itemView);
        return objectHolder;
    }

    @Override
    public void onBindViewHolder(FactsListObjectHolder holder, int position) {
        if (mDataSet != null && mDataSet.size() > 0) {
            Fact factsItem = mDataSet.get(position);
            holder.mTitleTextView.setText(factsItem.getTitle());
            holder.mDescriptionTextView.setText(factsItem.getDescription());
            if (factsItem.getImageHref() != null && factsItem.getImageHref().length() > 0) {
                holder.mThumbnailImageView.setImageURI(Uri.parse(factsItem.getImageHref()));
            } else {
                holder.mThumbnailImageView.setImageResource(R.drawable.no_image_available);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet != null)
            return mDataSet.size();

        return 0;
    }
}