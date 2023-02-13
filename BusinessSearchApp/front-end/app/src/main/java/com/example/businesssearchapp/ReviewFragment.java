package com.example.businesssearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class ReviewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View reviewView = inflater.inflate(R.layout.fragment_review, container, false);
        String BACKEND_HEAD = "https://my-second-project-33725.wl.r.appspot.com";

        RecyclerView reviewLs = reviewView.findViewById(R.id.reviewList);

        Intent myIntent = getActivity().getIntent();
        String busiId = myIntent.getStringExtra("businessId");

        String reviewUrl = BACKEND_HEAD + "/yelpReview?id=" + busiId;
        RequestQueue reviewQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, reviewUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
//                        resultsTable.setText("Response: " + response.toString());

                        reviewLs.setLayoutManager(new LinearLayoutManager(getActivity()));
                        try {
//                            ReviewAdapter rAdapter = new ReviewAdapter(getActivity(),response);
                            reviewLs.setAdapter(new ReviewAdapter(getActivity(),response));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast myToast = Toast.makeText(getActivity(), "request error: "+error, Toast.LENGTH_SHORT);
                        myToast.show();
//                        resultsTable.setText("request error: "+error);
                    }
                });

        reviewQueue.add(jsonArrayRequest);

        return reviewView;
    }
}


//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.businesssearchapp.placeholder.PlaceholderContent;
//
///**
// * A fragment representing a list of Items.
// */
//public class ReviewFragment extends Fragment {
//
//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public ReviewFragment() {
//    }
//
//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static ReviewFragment newInstance(int columnCount) {
//        ReviewFragment fragment = new ReviewFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
//
//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new ReviewRecyclerViewAdapter(PlaceholderContent.ITEMS));
//        }
//        return view;
//    }
//}