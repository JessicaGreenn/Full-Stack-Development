package com.example.businesssearchapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewViewHolder extends RecyclerView.ViewHolder{
    TextView nameView;
    TextView ratingView;
    TextView textView;
    TextView timeView;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.userName);
        ratingView = itemView.findViewById(R.id.userRating);
        textView = itemView.findViewById(R.id.userText);
        timeView = itemView.findViewById(R.id.userTime);

    }
}

