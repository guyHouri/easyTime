package com.example.easytime101;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class alarmAdapter extends RecyclerView.Adapter<alarmAdapter.alarmViewHolder> {
    private ArrayList<modelAlarm> mAlarmList;
    private OnAlarmClickListener mListener;

    public interface OnAlarmClickListener {
        void onAlarmClick(int position);
        void onRemoveAlarmClick(int position);
    }

    public void setOnAlarmClickListener(OnAlarmClickListener listener) {
        mListener = listener;
    }

    public static class alarmViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;
        TextView mTvName;
        TextView mTvDate;
        //boolean ison;
        Button mIsOn;
        public alarmViewHolder(@NonNull View itemView, final OnAlarmClickListener listener) {
            super(itemView);
            this.mIsOn = itemView.findViewById(R.id.switchh);
            this.mTvDate = itemView.findViewById(R.id.tvDateOfAlarm);
            this.mTvTime = itemView.findViewById(R.id.tvTime);
            this.mTvName = itemView.findViewById(R.id.tvNameInModel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAlarmClick(position);
                        }
                    }
                }
            });

            mIsOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveAlarmClick(position);
                        }
                    }
                }
            });
        }
    }

    public alarmAdapter(ArrayList<modelAlarm> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }

    @NonNull
    @Override
    public alarmAdapter.alarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelcardview, parent, false);
        alarmViewHolder ah = new alarmViewHolder(v, mListener);
        return ah;
    }

    @Override
    public void onBindViewHolder(@NonNull alarmAdapter.alarmViewHolder holder, int position) {
        modelAlarm modelAlarm = mAlarmList.get(position);
        holder.mTvTime.setText(mAlarmList.get(position).getTime());
        holder.mTvDate.setText(mAlarmList.get(position).getDate());
        holder.mTvName.setText(mAlarmList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mAlarmList!=null)
            return mAlarmList.size();
        return 0;
    }
}
