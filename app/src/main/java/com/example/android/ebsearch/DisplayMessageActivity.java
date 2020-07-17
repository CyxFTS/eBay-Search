package com.example.android.ebsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayMessageActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    public static final String EXTRA_SHIPPINGINFO = "shippingInfo";
    public static final String EXTRA_SHIPPINGCOST = "shippingCost";
    public static final String EXTRA_NAME = "title";
    public static final String EXTRA_PRICE = "currentPrice";
    public static final String EXTRA_ITEMID = "itemId";
    public static final String EXTRA_URL = "url";

    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItemList;
    private RequestQueue mRequestQueue;

    private LinearLayout spinner;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String TAG = "DisplayMessageActivity";

    private static String reqUrl;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String min = intent.getStringExtra(MainActivity.EXTRA_MIN);
        String max = intent.getStringExtra(MainActivity.EXTRA_MAX);
        String conditionNew = intent.getStringExtra(MainActivity.EXTRA_NEW);
        String conditionUsed = intent.getStringExtra(MainActivity.EXTRA_USED);
        String conditionUnspecified = intent.getStringExtra(MainActivity.EXTRA_UNSPECIFIED);
        String sort = intent.getStringExtra(MainActivity.EXTRA_SORT);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
        String conditionStr = "";
        Log.v(TAG, conditionNew);
        Log.v(TAG, conditionUsed);
        Log.v(TAG, conditionUnspecified);
        if(conditionUnspecified.contains("true")) {
            conditionStr += "&Condition=Unspecified";
        }
        if(conditionNew.contains("true"))
            conditionStr += "&Condition=New";
        if(conditionUsed.contains("true"))
            conditionStr += "&Condition=Used";



        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mItemList = new ArrayList<>();

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                parseJSON();
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);
        reqUrl = "https://yunxuanc-cs571-hw8.wl.r.appspot.com/req?Keywords="+message+"&minPrice="+min+"&maxPrice="+max+conditionStr+"&Sort="+sort;
        Log.v(TAG, reqUrl);
        parseJSON();
    }

    private void parseJSON() {
        String url = reqUrl;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(Integer.parseInt(response.getString("totalEntities").toString()) < 1)
                            {
                               TextView nr = findViewById(R.id.no_records);
                               nr.setVisibility(View.VISIBLE);
                                Context context = getApplicationContext();
                                CharSequence text = "No Records";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else
                            {
                                TextView nr = findViewById(R.id.no_records);
                                nr.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("itemList");
                                mItemList.clear();
                                for (int i = 0; i< jsonArray.length();i++) {
                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String url = hit.getString("url");
                                    String name = hit.getString("title");
                                    String imageURL = hit.getString("image");
                                    String shippingPrice = hit.getString("shippingCost");
                                    String condition = hit.getString("condition");
                                    String price = hit.getString("currentPrice");
                                    JSONObject shippingInfo = hit.getJSONObject("shippingInfo");
                                    String shippingCost = hit.getString("shippingCost");
                                    String itemId = hit.getString("itemId");
                                    String top = hit.getString("topRatedListing");

                                    mItemList.add(new Item(url, imageURL, name, shippingPrice, condition, price, shippingInfo.toString(), shippingCost, itemId, top));
                                }
                                String nums = String.valueOf(jsonArray.length());
                                TextView resultNum = findViewById(R.id.result_num);
                                resultNum.setText(nums);
                                TextView key = findViewById(R.id.keyword);
                                key.setText(message);
                                findViewById(R.id.result_info).setVisibility(View.VISIBLE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);

                                mItemAdapter = new ItemAdapter(DisplayMessageActivity.this, mItemList);
                                mRecyclerView.setLayoutManager(gridLayoutManager);
                                mRecyclerView.setAdapter(mItemAdapter);
                                mItemAdapter.setOnItemClickListener(DisplayMessageActivity.this);
                                //.v(TAG, jsonArray.toString());
                            }

                            spinner.setVisibility(View.GONE);
                            if(mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
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

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Item clickedItem = mItemList.get(position);

        detailIntent.putExtra(EXTRA_SHIPPINGINFO, clickedItem.getShippingInfo());
        detailIntent.putExtra(EXTRA_SHIPPINGCOST, clickedItem.getShippingCost());

        detailIntent.putExtra(EXTRA_NAME, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_PRICE, clickedItem.getPrice());

        detailIntent.putExtra(EXTRA_ITEMID, clickedItem.getItemId());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getUrl());

        startActivity(detailIntent);
    }
}