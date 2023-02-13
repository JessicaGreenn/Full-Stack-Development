package com.example.businesssearchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.businesssearchapp.databinding.FragmentFirstBinding;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;


public class FirstFragment extends Fragment {

//    private FragmentFirstBinding binding;
    private AutoCompleteTextView keyTextView;
    private EditText disTextView;
    private TextView noResults;

//    private TextView cateTextView;
    private Spinner spinnerCategories;
    private CheckBox autoLoc;
    private EditText locEdit;
    private ArrayAdapter<CharSequence> adapter;
    boolean isAllFieldsChecked = false;

    final double MILES_TO_METERS = 1609.344;
    final String ipinfoToken = "40d143f8bdfeb2";
    final String BACKEND_HEAD = "https://my-second-project-33725.wl.r.appspot.com";
//    private static final String[] COUNTRIES = new String[] {
//            "Belgium", "France", "Italy", "Germany", "Spain"
//    };

    private RecyclerView resultsTable;
    int logo = R.drawable.yelp_logo;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstView = inflater.inflate(R.layout.fragment_first, container, false);

        // Get the text view
        keyTextView = (AutoCompleteTextView) fragmentFirstView.findViewById(R.id.keyInput);
//        ArrayAdapter<String> keyAdapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        keyTextView.setAdapter(keyAdapter);
        TextWatcher keyWatcher = new TextWatcher() {
//            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (_ignore)
//                    return;
//
//                _ignore = true; // prevent infinite loop
                // Change your text here.
                String keyword = keyTextView.getText().toString();
                String autoCompUrl = BACKEND_HEAD + "/autoComplete?text=" + keyword;
                RequestQueue autoCompQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest autoCompRequest = new JsonArrayRequest
                        (Request.Method.GET, autoCompUrl, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {

                                try {
                                    int n = response.length();
                                    String[] autos = new String[n];
                                    for(int i = 0; i < n; i++){
                                        autos[i] = response.getString(i);
                                    }
                                    ArrayAdapter<String> keyAdapter = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_dropdown_item_1line, autos);
                                    keyTextView.setAdapter(keyAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
//                                Toast myToast = Toast.makeText(getActivity(), "request error: "+error, Toast.LENGTH_SHORT);
//                                myToast.show();
                            }
                        });

                autoCompQueue.add(autoCompRequest);

//                _ignore = false; // release, so the TextWatcher start to listen again.
            }
        };
        keyTextView.addTextChangedListener(keyWatcher);

        disTextView = fragmentFirstView.findViewById(R.id.disInput);
        spinnerCategories = fragmentFirstView.findViewById(R.id.cateInput);
        autoLoc = (CheckBox) fragmentFirstView.findViewById(R.id.autoLocating);
        locEdit = fragmentFirstView.findViewById(R.id.locInput);

        noResults = fragmentFirstView.findViewById(R.id.noResults);
        noResults.setVisibility(View.INVISIBLE);

        adapter =  ArrayAdapter.createFromResource(getActivity(), R.array.cateItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCategories.setAdapter(adapter);

        resultsTable = fragmentFirstView.findViewById(R.id.resultsTab);
        resultsTable.setNestedScrollingEnabled(true);


        return fragmentFirstView;
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//
//        });

        view.findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput(view);
//                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
//                myToast.show();
            }
        });

        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {

                    if (autoLoc.isChecked()) {
                        autoSearch(view);
                    } else {
                        inputSearch(view);
                    }
//                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
//                myToast.show();
                }
            }
        });

        view.findViewById(R.id.toolbar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookingsActivity.class);
                getContext().startActivity(intent);
            }
        });

        autoLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoLoc.isChecked()){
                    locEdit.setVisibility(View.INVISIBLE);
                }else{
                    locEdit.setVisibility(View.VISIBLE);
                    locEdit.setText("");
                }
            }
        });

    }


    private void clearInput(View view){
        keyTextView.setText("");
        disTextView.setText("");
        locEdit.setText("");
        spinnerCategories.setSelection(0);
        autoLoc.setChecked(false);
        locEdit.setVisibility(View.VISIBLE);
        resultsTable.setVisibility(View.INVISIBLE);
        noResults.setVisibility(View.INVISIBLE);

    }

    private void autoSearch(View view){

        RequestQueue ipQueue = Volley.newRequestQueue(getActivity());
        String ipUrl = "https://ipinfo.io/?token=" + ipinfoToken;
        JsonObjectRequest locInfoRequest = new JsonObjectRequest
                (Request.Method.GET, ipUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String[] locInfo = response.getString("loc").split(",");
                            String lat = locInfo[0];
                            String lng = locInfo[1];
                            String term = keyTextView.getText().toString();

//                            Toast myToast = Toast.makeText(getActivity(), lat + ", " + lng, Toast.LENGTH_SHORT);
//                            myToast.show();
                            String[] termList = term.split(" ");
                            if(termList.length == 1){
                                term = termList[0];
                            }else{
                                term = String.join("+", termList);
                            }


                            String radius = disTextView.getText().toString();
                            String category = spinnerCategories.getSelectedItem().toString();

                            String autoSearchUrl = BACKEND_HEAD + "/autoYelpParam?term=" + term + "&lat=" + lat + "&lng=" + lng + "&radius=" + radius + "&categories=" + category;
                            RequestQueue autoSearchQueue = Volley.newRequestQueue(getActivity());
                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                    (Request.Method.GET, autoSearchUrl, null, new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            if(response.length() == 0){
                                                resultsTable.setVisibility(View.INVISIBLE);
                                                noResults.setVisibility(View.VISIBLE);
                                            }else{
                                                noResults.setVisibility(View.INVISIBLE);
                                                resultsTable.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                try {
                                                    resultsTable.setAdapter(new CustomAdapter(getActivity(),response));
                                                    resultsTable.setVisibility(View.VISIBLE);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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

                            autoSearchQueue.add(jsonArrayRequest);


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

        ipQueue.add(locInfoRequest);

    }

    private void inputSearch(View view){
        String term = keyTextView.getText().toString();
        String locInfo = locEdit.getText().toString();
//        Toast myToast = Toast.makeText(getActivity(), term + ", " + locInfo, Toast.LENGTH_SHORT);
//        myToast.show();
        String[] termList = term.split(" ");
        if(termList.length == 1){
            term = termList[0];
        }else{
            term = String.join("+", termList);
        }

        String[] locList = locInfo.split(" ");
        if(locList.length == 1){
            locInfo = locList[0];
        }else{
            locInfo = String.join("+", locList);
        }

        String radius = disTextView.getText().toString();
        String category = spinnerCategories.getSelectedItem().toString();


//        Toast myToast = Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT);
//        myToast.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = BACKEND_HEAD + "/yelpParam?term=" + term + "&radius=" + radius + "&categories=" + category + "&locInfo=" + locInfo;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() == 0){
                            resultsTable.setVisibility(View.INVISIBLE);
                            noResults.setVisibility(View.VISIBLE);
                        }else{
                            noResults.setVisibility(View.INVISIBLE);
                            resultsTable.setLayoutManager(new LinearLayoutManager(getActivity()));
                            try {
                                resultsTable.setAdapter(new CustomAdapter(getActivity(),response));
                                resultsTable.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

        queue.add(jsonArrayRequest);


    }
    private boolean CheckAllFields() {
        if (keyTextView.length() == 0) {
            keyTextView.setError("This field is required");
            return false;
        }

        if (disTextView.length() == 0) {
            disTextView.setError("This field is required");
            return false;
        }

        if ((!autoLoc.isChecked()) && (locEdit.length() == 0)) {
            locEdit.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

}

