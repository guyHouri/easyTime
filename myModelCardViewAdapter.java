package com.example.easytime101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class myModelCardViewAdapter extends RecyclerView.Adapter<myModelCardViewHolder>{

    Context c;
    ArrayList<modelAlarm> models;
    OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public myModelCardViewAdapter(Context c, ArrayList<modelAlarm> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public myModelCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // convert xml to view obj
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelcardview, null);
        return new myModelCardViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myModelCardViewHolder holder, int position) {
        // bind data to our views
        holder.mTvTime.setText(models.get(position).getTime());
        //holder.mIsOn.setChecked(models.get(position).getisOn());
        holder.mTvDate.setText(models.get(position).getDate());
        holder.mTvName.setText(models.get(position).getName());

        // animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        if (models!=null) {
            return models.size();
        }
        return 0;
    }
}