package com.example.easytime101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public  class myModelCardViewHolder extends RecyclerView.ViewHolder {

    TextView mTvTime;
    TextView mTvName;
    TextView mTvDate;
    boolean ison;
    Button mIsOn;

    public myModelCardViewHolder(@NonNull View itemView, final myModelCardViewAdapter.OnItemClickListener listener) {
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
                        listener.onItemClick(position);
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
                        listener.onDeleteClick(position);
                    }
                }
            }
        });

    }
}

