package com.example.businesssearchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TestActivity extends AppCompatActivity{

    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private View fragmentTestView;
    private String yelpLinkGlobal;
    private String busiNameGlobal;
    ViewPagerFragmentAdapter adapter;

    final String BACKEND_HEAD = "https://my-second-project-33725.wl.r.appspot.com";

    // array for tab labels
    private String[] labels = new String[]{"Business Details", "Map Location", "Reviews"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent myIntent = getIntent();
        // call function to initialize views
        init();
        String busiName = myIntent.getStringExtra(("businessName"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(busiName);
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        yelpLinkGlobal = myIntent.getStringExtra("yelpLink");
        busiNameGlobal = myIntent.getStringExtra(("businessName"));


        // bind and set tabLayout to viewPager2 and set labels for every tab
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(labels[position]);
        }).attach();


        // Get a handle to the fragment and register the callback.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        Toast myToast = Toast.makeText(this, busiId, Toast.LENGTH_LONG);
//        myToast.show();
        // set default position to 1 instead of default 0
//        viewPager2.setCurrentItem(1, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.facebook:
                Intent fbURL = new Intent(Intent.ACTION_VIEW);
                String fbStr = "https://www.facebook.com/sharer/sharer.php?u=" + yelpLinkGlobal;
                fbURL.setData(Uri.parse(fbStr));
                startActivity(fbURL);
//                Toast myToast = Toast.makeText(this, "share to facebook", Toast.LENGTH_LONG);
//                myToast.show();
                return true;

            case R.id.twitter:
                Intent twURL = new Intent(Intent.ACTION_VIEW);
                String twStr = "https://twitter.com/intent/tweet?text=Check Out " + busiNameGlobal + " on Yelp. " + yelpLinkGlobal;
                twURL.setData(Uri.parse(twStr));
                startActivity(twURL);
//                Toast myToast2 = Toast.makeText(this, "share to twitter", Toast.LENGTH_LONG);
//                myToast2.show();
                return true;

            default:
                onBackPressed();
                return true;

        }
    }

    private void init() {
        // initialize tabLayout
        tabLayout = findViewById(R.id.tabs);
        // initialize viewPager2
        viewPager2 = findViewById(R.id.viewPager2);
        // create adapter instance
        adapter = new ViewPagerFragmentAdapter(this);
        // set adapter to viewPager2
        viewPager2.setAdapter(adapter);

        // remove default elevation of actionbar
        getSupportActionBar().setElevation(0);

    }

//    // Get a handle to the GoogleMap object and display marker.
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));
//    }


    // create adapter to attach fragments to viewpager2 using FragmentStateAdapter
    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // return fragments at every position
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CardFragment(); // calls fragment
                case 1:
                    return new MapsFragment2(); // chats fragment
                case 2:
                    return new ReviewFragment(); // status fragment
            }
            return new CardFragment(); //chats fragment
        }

        // return total number of tabs in our case we have 3
        @Override
        public int getItemCount() {
            return labels.length;
        }
    }
}

