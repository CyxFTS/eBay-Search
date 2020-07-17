package com.example.android.ebsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_ITEMID;
import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_NAME;
import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_PRICE;
import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_SHIPPINGINFO;
import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_SHIPPINGCOST;
import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private RequestQueue mRequestQueue;

    private LinearLayout spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(EXTRA_NAME));

//
//        ImageView imageView = findViewById(R.id.image_view_detail);
//        TextView textViewTitle = findViewById(R.id.text_view_name_detail);
//        TextView textViewPrice = findViewById(R.id.text_view_price_detail);
//
//        Picasso.with(this).load(imageURL).fit().centerInside().into(imageView);
//        textViewTitle.setText(title);
//        textViewPrice.setText("$ "+price);


        PictureArray = new JSONArray();
        Subtitle = new JSONArray();
        Brand = new JSONArray();
        Specifics = new JSONArray();
        Seller = new HashMap<String, Object>();
        ReturnPolicy = new HashMap<String, Object>();

        mRequestQueue = Volley.newRequestQueue(this);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        parseJSON();
        Log.v(TAG+"1", PictureArray.toString());
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.button_redirect) {
            // do something here
            String url = "http://www.example.com";
            Intent intent = getIntent();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra(EXTRA_URL))));
        }
        return super.onOptionsItemSelected(item);
    }

    private static String reqUrl = "https://yunxuanc-cs571-hw8.wl.r.appspot.com/reqItem?id=";

    private JSONArray PictureArray;
    private JSONArray Subtitle;
    private JSONArray Brand;
    private JSONArray Specifics;
    private HashMap<String, Object> Seller;
    private HashMap<String, Object> ReturnPolicy;

    private void parseJSON() {
        Intent intent = getIntent();
        String itemId = intent.getStringExtra(EXTRA_ITEMID);
        String url = reqUrl+itemId;
        Log.v(TAG, url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            PictureArray = response.getJSONArray("PictureURL");
                            if(!response.isNull("Subtitle"))
                                Subtitle = response.getJSONArray("Subtitle");
                            if(!response.isNull("Brand"))
                                Brand = response.getJSONArray("Brand");
                            if(!response.isNull("Specifics"))
                                Specifics = response.getJSONArray("Specifics");
                            if(!response.isNull("Seller"))
                                Seller = new Gson().fromJson(response.getJSONObject("Seller").toString(), HashMap.class);
                            if(!response.isNull("ReturnPolicy"))
                                ReturnPolicy = new Gson().fromJson(response.getJSONObject("ReturnPolicy").toString(), HashMap.class);

                            Log.v(TAG, PictureArray.toString());
//                            Log.v(TAG, Subtitle.toString());
//                            Log.v(TAG, Brand.toString());
//                            Log.v(TAG, Specifics.toString());
//                            Log.v(TAG, Seller.toString());
//                            Log.v(TAG, ReturnPolicy.toString());


//                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
//                            mItemAdapter = new ItemAdapter(DisplayMessageActivity.this, mItemList);
//                            mRecyclerView.setLayoutManager(gridLayoutManager);
//                            mRecyclerView.setAdapter(mItemAdapter);
//                            mItemAdapter.setOnItemClickListener(DisplayMessageActivity.this);
//                            Log.v(TAG, mItemList.toString());
//                            spinner.setVisibility(View.GONE);
                            TabLayout tabLayout = findViewById(R.id.tabBar);
                            TabItem tabProduct = findViewById(R.id.tab_product);
                            TabItem tabSellInfo = findViewById(R.id.tab_seller_info);
                            TabItem tabShipping = findViewById(R.id.tab_shipping);
                            final ViewPager viewPager = findViewById(R.id.viewPager);
                            Intent intent = getIntent();

                            HashMap<String, Object> shippingInfo = new Gson().fromJson(intent.getStringExtra(EXTRA_SHIPPINGINFO),HashMap.class);
                            String title = intent.getStringExtra(EXTRA_NAME);
                            String price = intent.getStringExtra(EXTRA_PRICE);
                            String shippingCost = intent.getStringExtra(EXTRA_SHIPPINGCOST);


                            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                                    new ItemDetail(title, price, shippingCost,PictureArray, Subtitle, Brand, Specifics, Seller, ReturnPolicy, shippingInfo));

                            viewPager.setAdapter(pagerAdapter);

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                            spinner.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mRequestQueue.add(request);
    }
}