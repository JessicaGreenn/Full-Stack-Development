package com.example.businesssearchapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsViewHolder extends RecyclerView.ViewHolder{
    TextView id;
    ImageView img;
    TextView name;
    TextView rating;
    TextView distance;

    public ResultsViewHolder(@NonNull View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.idNum);
        rating = itemView.findViewById(R.id.busiRating);
        img = itemView.findViewById(R.id.imgUrl);
        distance = itemView.findViewById(R.id.busiDistance);
        name = itemView.findViewById(R.id.busiName);


    }
}

