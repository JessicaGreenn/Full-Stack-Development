package com.example.businesssearchapp;

import androidx.recyclerview.widget.RecyclerView;


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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;


public class CustomAdapter extends RecyclerView.Adapter<ResultsViewHolder> {
    Context context;
    List<Item> items;
    List<String> yelpLinks;
    List<Double> lat;
    List<Double> lng;

    public CustomAdapter(Context context, JSONArray response) throws JSONException {
        this.context = context;
        items = new ArrayList<Item>();
        yelpLinks = new ArrayList<String>();
        lat = new ArrayList<Double>();
        lng = new ArrayList<Double>();
        for(int i = 0; i < response.length(); i++){
            int idNum = response.getJSONObject(i).getInt("idNum");
            double dis = response.getJSONObject(i).getDouble("distance");
            double rating = response.getJSONObject(i).getInt("rating");
            String name = response.getJSONObject(i).getString("name");
            String imgUrl = response.getJSONObject(i).getString("image_url");
            String id = response.getJSONObject(i).getString("id");
            Item temp = new Item(idNum, imgUrl, name, rating, dis, id);
            items.add(temp);
            yelpLinks.add(response.getJSONObject(i).getString("url"));
            lat.add(response.getJSONObject(i).getJSONObject("coordinates").getDouble("latitude"));
            lng.add(response.getJSONObject(i).getJSONObject("coordinates").getDouble("longitude"));

        }


    }

    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultsViewHolder(LayoutInflater.from(context).inflate(R.layout.results_table, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(items.get(position).getBusiName());
        Integer idHolder = items.get(position).getIdNum();
        holder.id.setText(idHolder.toString());
        Double disHolder = items.get(position).getBusiDistance();
        holder.distance.setText(disHolder.toString());
        Picasso.get().load(items.get(position).getImgUrl())
                .resize(100, 100)
                .centerCrop()
                .into(holder.img);
        Double ratingHolder = items.get(position).getBusiRating();
        holder.rating.setText(ratingHolder.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TestActivity.class);

                intent.putExtra("businessId", items.get(position).getId());
                intent.putExtra("businessName", items.get(position).getBusiName());
                intent.putExtra("yelpLink", yelpLinks.get(position));
                intent.putExtra("lat", lat.get(position));
                intent.putExtra("lng", lng.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
