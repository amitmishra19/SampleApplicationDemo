package com.example.codingtest.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.codingtest.MainActivity;
import com.example.codingtest.R;
import com.example.codingtest.beans.Row;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter class that will create and bind json data to the list view.
 * It is using the latest RecyclerView.Adapter for better performance
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataObjectHolder> {

    private MainActivity mParentActivity;
    List<Row> mDataSet;

    public DataAdapter(MainActivity context, List<Row> dataSet) {
        mParentActivity = context;
        mDataSet = new ArrayList<>(dataSet);
    }

    public void setModels(List<Row> dataSet) {
        mDataSet = new ArrayList<>(dataSet);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        ImageView mThumbnailImageView;

        public DataObjectHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.item_description);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_thumbnail);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mParentActivity).inflate(R.layout.list_item_layout, parent, false);
        DataObjectHolder objectHolder = new DataObjectHolder(itemView);
        return objectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if (mDataSet != null && mDataSet.size() > 0) {
            Row rowItem = mDataSet.get(position);
            holder.mTitleTextView.setText(rowItem.getTitle());
            holder.mDescriptionTextView.setText(rowItem.getDescription());
            if (rowItem.getImageHref() != null && rowItem.getImageHref().length() > 0) {
                holder.mThumbnailImageView.setImageURI(Uri.parse(rowItem.getImageHref()));
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