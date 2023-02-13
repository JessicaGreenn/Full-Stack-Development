package com.example.businesssearchapp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    private ArrayList<BookingsItem> bookings;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView rNT;
        private TextView rDT;
        private TextView rTT;
        private TextView rET;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.txtTitle);
            rNT = itemView.findViewById(R.id.resNameTxt);
            rDT = itemView.findViewById(R.id.resDateTxt);
            rTT = itemView.findViewById(R.id.resTimeTxt);
            rET = itemView.findViewById(R.id.resEmailTxt);
        }
    }

    public RecyclerViewAdapter(ArrayList<BookingsItem> data, Context context) {
        this.context = context;
        bookings = new ArrayList<BookingsItem>();
        for(int i = 0; i < data.size(); i++){
            String idTxt = data.get(i).getId();
            String nameTxt = data.get(i).getResNameTxt();
            String dateTxt = data.get(i).getResDateTxt();
            String timeTxt = data.get(i).getResTimeTxt();
            String emailTxt = data.get(i).getResEmailTxt();
            BookingsItem tmp = new BookingsItem(idTxt,nameTxt, dateTxt, timeTxt, emailTxt);
            bookings.add(tmp);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookingsview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(String.valueOf(position + 1));
        holder.rNT.setText(bookings.get(position).getResNameTxt());
        holder.rDT.setText(bookings.get(position).getResDateTxt());
        holder.rTT.setText(bookings.get(position).getResTimeTxt());
        holder.rET.setText(bookings.get(position).getResEmailTxt());
    }

    @Override
    public int getItemCount() {

        if(bookings == null){
            return 0;
        }else{
            return bookings.size();
        }

    }


    public void removeItem(int position) {
        bookings.remove(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("resData", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson bookingsGson = new Gson();

        editor.putString("bookings", bookingsGson.toJson(bookings));
        editor.commit();

        notifyItemRemoved(position);
    }

//    public void restoreItem(BookingsItem item, int position) {
//        bookings.add(position, item);
//        notifyItemInserted(position);
//    }

    public ArrayList<BookingsItem> getData() {
        return bookings;
    }
}

