package com.example.easytime101;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleEventAdapter extends RecyclerView.Adapter<ExampleEventAdapter.ExampleEventViewHolder> {
    private ArrayList<ExampleItemEvent> mExampleList;
    private OnItemClickListener mListener;

    interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleEventViewHolder extends RecyclerView.ViewHolder {
        // bli kesher ladialog
        public TextView title;
        public ImageView mImageResource;
        public TextView date;
        public TextView startTime;
        public TextView place;
        public ImageView ivDeleteEvent;

        public ExampleEventViewHolder(@NonNull View itemView, final OnItemClickListener listener)  {
            super(itemView);
            mImageResource = itemView.findViewById(R.id.ivPicOfEvent);
            date = itemView.findViewById(R.id.tvDateOfEvent);
            startTime = itemView.findViewById(R.id.tvTimeStart);
            place = itemView.findViewById(R.id.tvPlacExampleEvent);
            title = itemView.findViewById(R.id.tvTitleOfEvent);
            ivDeleteEvent = itemView.findViewById(R.id.ivDeleteIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            ivDeleteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public ExampleEventAdapter(ArrayList<ExampleItemEvent> exampleList) {
        mExampleList=exampleList;
    }

    @NonNull
    @Override
    public ExampleEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_event, parent, false);
        ExampleEventViewHolder evh = new ExampleEventViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleEventViewHolder holder, int position) {
        ExampleItemEvent currentItem = mExampleList.get(position);
        holder.mImageResource.setImageResource(currentItem.getmImageResource());
        holder.title.setText(currentItem.getTitle());
        holder.startTime.setText(currentItem.getStartTime());
        holder.place.setText(currentItem.getPlace());
        holder.date.setText(currentItem.getDate());

    }

    @Override
    public int getItemCount() {
        if (mExampleList!=null)
           return mExampleList.size();
        return 0;
    }
}
