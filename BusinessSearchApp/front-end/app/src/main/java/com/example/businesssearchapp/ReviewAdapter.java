package com.example.businesssearchapp;

import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.Collections;
//import java.util.List;

//public class CustomAdapter
//        extends RecyclerView.Adapter<resultsViewHolder> {
//
//    List<resultsData> list
//            = Collections.emptyList();
//
//    Context context;
//
////    ClickListiner listiner;
//
//    public CustomAdapter(List<resultsData> list,
//                                Context context)
//    {
//        this.list = list;
//        this.context = context;
////        this.listiner = listiner;
//    }
//
//    @Override
//    public resultsViewHolder
//    onCreateViewHolder(ViewGroup parent,
//                       int viewType)
//    {
//
//        Context context
//                = parent.getContext();
//        LayoutInflater inflater
//                = LayoutInflater.from(context);
//
//        // Inflate the layout
//
//        View photoView
//                = inflater
//                .inflate(R.layout.results_table,
//                        parent, false);
//
//        resultsViewHolder viewHolder
//                = new resultsViewHolder(photoView);
//        return viewHolder;
//    }
//
//    @Override

//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

//public void onBindViewHolder(final resultsViewHolder viewHolder,
//                     final int position)
//    {
//        final int index = viewHolder.getAdapterPosition();
//        viewHolder.examName
//                .setText(list.get(position).name);
//        viewHolder.examDate
//                .setText(list.get(position).date);
//        viewHolder.examMessage
//                .setText(list.get(position).message);
//
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return list.size();
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(
//            RecyclerView recyclerView)
//    {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//
//}
//
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    Context context;
    List<ReviewItem> items;

    public ReviewAdapter(Context context, JSONArray response) throws JSONException {
        this.context = context;
        items = new ArrayList<ReviewItem>();
        for (int i = 0; i < response.length(); i++){
            String username = response.getJSONObject(i).getString("name");
            int userrating = response.getJSONObject(i).getInt("rating");
            String usertext = response.getJSONObject(i).getString("text");
            String usertime = response.getJSONObject(i).getString("time");
            String[] userdate = usertime.split(" ");
            usertime = userdate[0];
            ReviewItem temp = new ReviewItem(username, userrating, usertext, usertime);
            items.add(temp);
        }

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.reviews_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(items.get(position).getUsername());
        String rate = "Rating: " + items.get(position).getUserrating() + "/5";
        holder.ratingView.setText(rate);
        holder.textView.setText(items.get(position).getUsertext());
        holder.timeView.setText(items.get(position).getUsertime());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}

