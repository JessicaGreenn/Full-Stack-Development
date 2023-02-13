package com.example.businesssearchapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.coordinatorlayout.widget.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BookingsActivity extends AppCompatActivity {
    private TextView noBks;
//    private TextView em;
//    private TextView ti;
//    private TextView da;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ArrayList<BookingsItem> currBookList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        noBks = findViewById(R.id.noBookings);
//        em = findViewById(R.id.email);
//        da = findViewById(R.id.date);
//        ti = findViewById(R.id.time);

        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        populateRecyclerView(BookingsActivity.this);
        enableSwipeToDeleteAndUndo();

//        Toast myToast = Toast.makeText(this, "bookings length:" + sP.contains("bookings") + "!", Toast.LENGTH_LONG);
//        myToast.show();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Yelp");

        actionBar.setDisplayHomeAsUpEnabled(true);

//        SharedPreferences sharedpreferences = getSharedPreferences("resData",
//                Context.MODE_PRIVATE);
//        if(sharedpreferences.contains("email")){
//            noBks.setVisibility(View.INVISIBLE);
//        Toast myToast = Toast.makeText(this, "email is " + sharedpreferences.getString("email",""), Toast.LENGTH_SHORT);
//        myToast.show();
//            em.setText(sharedpreferences.getString("email",""));
//            da.setText(sharedpreferences.getString("date",""));
//            ti.setText(sharedpreferences.getString("time",""));
//            em.setVisibility(View.VISIBLE);
//            da.setVisibility(View.VISIBLE);
//            ti.setVisibility(View.VISIBLE);
//        }else{
//            noBks.setVisibility(View.VISIBLE);
//            em.setVisibility(View.INVISIBLE);
//            da.setVisibility(View.INVISIBLE);
//            ti.setVisibility(View.INVISIBLE);
//         }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void populateRecyclerView(Context context) {
//        stringArrayList.add("Item 1");
//        stringArrayList.add("Item 2");
//        stringArrayList.add("Item 3");
//        stringArrayList.add("Item 4");
//        stringArrayList.add("Item 5");
//        stringArrayList.add("Item 6");
//        stringArrayList.add("Item 7");
//        stringArrayList.add("Item 8");
//        stringArrayList.add("Item 9");
//        stringArrayList.add("Item 10");
        sP = context.getSharedPreferences("resData", 0);
        SharedPreferences.Editor edit = sP.edit();
        String currBookings = sP.getString("bookings", "");
        if(currBookings.equals("") || currBookings.equals("[]")){
            noBks.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else {
            noBks.setVisibility(View.INVISIBLE);

            Gson bookingsGson = new Gson();
            currBookList = bookingsGson.fromJson(currBookings, new TypeToken<ArrayList<BookingsItem>>(){}.getType());
//            Toast myToast = Toast.makeText(this, currBookList+" is the bookings", Toast.LENGTH_SHORT);
//            myToast.show();

            recyclerView.setLayoutManager(new LinearLayoutManager(BookingsActivity.this));
            mAdapter = new RecyclerViewAdapter(currBookList, BookingsActivity.this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }



    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final BookingsItem item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);
                String currBookings = sP.getString("bookings", "");
                if(currBookings.equals("") || currBookings.equals("[]")){
                    noBks.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyItemRemoved(position);


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Removing Existing Reservation", Snackbar.LENGTH_LONG);
                snackbar.show();
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        mAdapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });
//
//                snackbar.setActionTextColor(Color.YELLOW);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


}