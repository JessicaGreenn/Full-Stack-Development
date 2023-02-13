package com.example.businesssearchapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.Toast;

public class CardFragment extends Fragment {
    String busiId;
    String busiName;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cardView = inflater.inflate(R.layout.fragment_card, container, false);
        String BACKEND_HEAD = "https://my-second-project-33725.wl.r.appspot.com";

        TextView address = cardView.findViewById(R.id.address);
        TextView phone = cardView.findViewById(R.id.phone);
        TextView category = cardView.findViewById(R.id.category);
        TextView price = cardView.findViewById(R.id.price);
        TextView status = cardView.findViewById(R.id.status);
        TextView yelpLink = cardView.findViewById(R.id.yelpLink);
        ImageView photo1 = cardView.findViewById(R.id.photo1);
        ImageView photo2 = cardView.findViewById(R.id.photo2);
        ImageView photo3 = cardView.findViewById(R.id.photo3);


        Intent myIntent = getActivity().getIntent();
        busiId = myIntent.getStringExtra("businessId");
        String detailsUrl = BACKEND_HEAD + "/yelpDetails?id=" + busiId;
        RequestQueue detailsQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest detailsRequest = new JsonObjectRequest
                (Request.Method.GET, detailsUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray addArray = response.getJSONObject("location").getJSONArray("display_address");
                            String addressStr = addArray.getString(0) + " " + addArray.getString(1);
                            address.setText(addressStr);
                            phone.setText(response.getString("phone"));
                            category.setText(response.getString("category"));
                            price.setText(response.getString("price"));
                            boolean isOpen = response.getBoolean("status");
                            if(isOpen){
                                status.setText("Open Now");
                                status.setTextColor(Color.GREEN);
                            }else{
                                status.setText("Closed");
                                status.setTextColor(Color.RED);
                            }
                            String yelpLinkHtml = response.getString("yelpLink");
                            String yelpLinkStr = String.format("<a href ='%s' target='_blank' rel='noopener noreferrer' style = 'color: rgb(0,255,255);'>Business link</a>", yelpLinkHtml);
                            yelpLink.setText(Html.fromHtml(yelpLinkStr));
                            yelpLink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent yelpURL = new Intent(Intent.ACTION_VIEW);
                                    String yelpStr = yelpLinkHtml;
                                    yelpURL.setData(Uri.parse(yelpStr));
                                    startActivity(yelpURL);
                                }
                            });
                            Picasso.get().load(response.getJSONArray("photos").getString(0))
                                    .resize(100, 100)
                                    .centerCrop()
                                    .into(photo1);

                            Picasso.get().load(response.getJSONArray("photos").getString(1))
                                    .resize(100, 100)
                                    .centerCrop()
                                    .into(photo2);

                            Picasso.get().load(response.getJSONArray("photos").getString(2))
                                    .resize(100, 100)
                                    .centerCrop()
                                    .into(photo3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                        Toast myToast = Toast.makeText(this, "request error: "+error, Toast.LENGTH_LONG);
//                        myToast.show();
                    }
                });

        detailsQueue.add(detailsRequest);

        busiName = myIntent.getStringExtra("businessName");
        Button resButton = cardView.findViewById(R.id.reserve_button);
        resButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.reservation_form);

                // set the custom dialog components - text, image and button
                TextView title = (TextView) dialog.findViewById(R.id.resTitle);
                title.setText(busiName);

                TextView cancelText = dialog.findViewById(R.id.resCancel);
                // if button is clicked, close the custom dialog
                cancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // initiate the date picker and a button
                EditText date = dialog.findViewById(R.id.resDate);
                // perform click event on edit text
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // set day of month , month and year value in the edit text
                                        date.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();

                        TextView submitText = dialog.findViewById(R.id.resSubmit);
                        submitText.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                resSubmit(dialog);
                            }
                        });
                    }
                });

                EditText time = dialog.findViewById(R.id.resTime);
                // perform click event listener on edit text
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are getting the
                        // instance of our calendar.
                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting our hour, minute.
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        // on below line we are initializing our Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        // on below line we are setting selected time
                                        // in our text view.
                                        if(minute < 10){
                                            time.setText(hourOfDay + ":0" + minute);
                                        }else {
                                            time.setText(hourOfDay + ":" + minute);
                                        }
                                    }
                                }, hour, minute, false);
                        // at last we are calling show to
                        // display our time picker dialog.
                        timePickerDialog.show();

                    }
                });

                dialog.show();
            }
        });

        return cardView;
    }

    private void resSubmit(Dialog dialog){

        EditText emailView = dialog.findViewById(R.id.resEmail);
        EditText dateView = dialog.findViewById(R.id.resDate);
        EditText timeView = dialog.findViewById(R.id.resTime);
        String email = emailView.getText().toString();
        String date = dateView.getText().toString();
        String time = timeView.getText().toString();
        boolean isValidEmail = false;
        for(int i = 1; i < email.length() - 1; i++){
            if((email.charAt(i)) == '@'){
                isValidEmail = true;
            }
        }
        if(!isValidEmail){
            dialog.dismiss();
            Toast myToast = Toast.makeText(getActivity(), "InValid Email Address.", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }

        int hour = Integer.parseInt(time.split(":")[0]);
        if(hour < 10 || hour >= 17){
            dialog.dismiss();
            Toast myToast = Toast.makeText(getActivity(), "Time should be between 10AM AND 5PM", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("resData",
                Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String currBookStr = sharedpreferences.getString("bookings", "");
        Gson bookingsGson = new Gson();
        ArrayList<BookingsItem> currBookList = bookingsGson.fromJson(currBookStr, new TypeToken<ArrayList<BookingsItem>>(){}.getType());
        if(currBookStr.equals("")){
            currBookList = new ArrayList<BookingsItem>();
            BookingsItem item = new BookingsItem(busiId, busiName, date, time, email);
            currBookList.add(item);
        }else{
            boolean isThere = false;
            for(int i = 0; i < currBookList.size(); i++){
                if((currBookList.get(i).getId()).equals(busiId)){
                    isThere = true;
                    currBookList.get(i).setResDateTxt(date);
                    currBookList.get(i).setResTimeTxt(time);
                    currBookList.get(i).setResEmailTxt(email);
                }
            }
            if(!isThere){
                currBookList.add(new BookingsItem(busiId, busiName, date, time, email));
            }
        }

        editor.putString("bookings", bookingsGson.toJson(currBookList));
        editor.commit();
//        Toast myToast2 = Toast.makeText(getActivity(), busiId+" "+busiName+" "+date+ " "+time+ " " + email, Toast.LENGTH_SHORT);
//        myToast2.show();

        dialog.dismiss();

        Toast myToast = Toast.makeText(getActivity(), "Reservation Booked", Toast.LENGTH_SHORT);
        myToast.show();

    }

}


//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CardFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class CardFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private TextView idText;
//
//    public CardFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CardFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CardFragment newInstance(String param1, String param2) {
//        CardFragment fragment = new CardFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View cardView = inflater.inflate(R.layout.fragment_card, container,false);
//        idText = cardView.findViewById(R.id.idCode);
//
//        return cardView;
//    }
//}