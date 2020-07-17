/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.ebsearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * The WhoWroteIt app query's the Book Search API for Books based
 * on a user's search.
 */
public class MainActivity extends AppCompatActivity {

    // Variables for the search input field, and results TextViews.
    private EditText mBookInput;
    private EditText mMinPrice;
    private EditText mMaxPrice;
    private CheckBox mNew;
    private CheckBox mUsed;
    private CheckBox mUnspecified;
    private Spinner mSpinner;

    private static final String TAG = "MainActivity";

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState The current state data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all the view variables.
        mBookInput = (EditText)findViewById(R.id.bookInput);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mMinPrice = (EditText) findViewById(R.id.priceMin);
        mMaxPrice = (EditText) findViewById(R.id.priceMax);
        mNew = (CheckBox) findViewById(R.id.checkBoxNew);
        mUsed = (CheckBox) findViewById(R.id.checkBoxUsed);
        mUnspecified = (CheckBox) findViewById(R.id.checkBoxUnspecified);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);


    }

    public static final String EXTRA_MESSAGE = "com.example.ebsearch.MESSAGE";
    public static final String EXTRA_MIN = "min";
    public static final String EXTRA_MAX = "max";
    public static final String EXTRA_NEW = "new";
    public static final String EXTRA_USED = "used";
    public static final String EXTRA_UNSPECIFIED = "unspecified";
    public static final String EXTRA_SORT = "sort";

    /**
     * Gets called when the user pushes the "Search Books" button
     *
     * @param view The view (Button) that was clicked.
     */
    public void searchBooks(View view) {
        // Get the search string from the input field.
        String queryString = mBookInput.getText().toString();

        boolean newChecked = mNew.isChecked();
        boolean usedChecked = mUsed.isChecked();
        boolean unspecifiedChecked = mUnspecified.isChecked();
        //Log.v(TAG, text);

//        // Hide the keyboard when the button is pushed.
//        InputMethodManager inputManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//
//        // Check the status of the network connection.
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
//        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
//            new FetchBook(mTitleText, mAuthorText, mBookInput).execute(queryString);
//        }
//        // Otherwise update the TextView to tell the user there is no connection or no search term.
//        else {
//            if (queryString.length() == 0) {
//                mAuthorText.setText("");
//                mTitleText.setText(R.string.no_search_term);
//            } else {
//                mAuthorText.setText("");
//                mTitleText.setText(R.string.no_network);
//            }
//        }
        // send message
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.bookInput);
        String message = editText.getText().toString();

        boolean flag = true;
        if(message.length()<1) {
            TextView errorKey = findViewById(R.id.error_keyword);
            errorKey.setVisibility(View.VISIBLE);
            flag = false;
        }
        else{
            TextView errorKey = findViewById(R.id.error_keyword);
            errorKey.setVisibility(View.GONE);
        }
        Integer minPrice = -1;
        Integer maxPrice = -1;
        boolean priceFlag = true;
        if(mMinPrice.getText().toString().length()!=0) {
            minPrice = Integer.parseInt(mMinPrice.getText().toString());
            if(minPrice < 0)
            {
                TextView errorKey = findViewById(R.id.error_price);
                errorKey.setVisibility(View.VISIBLE);
                flag = false;
                priceFlag = false;
            }
        }
        if(mMaxPrice.getText().toString().length()!=0) {
            maxPrice = Integer.parseInt(mMaxPrice.getText().toString());
            if(maxPrice < 0)
            {
                TextView errorKey = findViewById(R.id.error_price);
                errorKey.setVisibility(View.VISIBLE);
                flag = false;
                priceFlag = false;
            }
        }
        if(minPrice > maxPrice && maxPrice!=-1)
        {
            TextView errorKey = findViewById(R.id.error_price);
            errorKey.setVisibility(View.VISIBLE);
            flag = false;
            priceFlag = false;
        }

        if(priceFlag)
        {
            TextView errorKey = findViewById(R.id.error_price);
            errorKey.setVisibility(View.GONE);
        }



        if(flag)
        {
            String min = minPrice.toString();
            if(mMinPrice.getText().toString().length()==0)
                min = "";

            String max = maxPrice.toString();
            if(mMaxPrice.getText().toString().length()==0)
                max = "";
            String conditionNew = String.valueOf(newChecked);
            String conditionUsed = String.valueOf(usedChecked);
            String conditionUnspecified = String.valueOf(unspecifiedChecked);
            String sort = mSpinner.getSelectedItem().toString();
            String sortStr = "";
            switch (sort) {
                case "Best Match":
                    sortStr = "BestMatch";
                    break;
                case "Price: highest first":
                    sortStr = "CurrentPriceHighest";
                    break;
                case "Price + Shipping: Highest first":
                    sortStr = "PricePlusShippingHighest";
                    break;
                case "Price + Shipping: Lowest first":
                    sortStr = "PricePlusShippingLowest";
                    break;
                default:
                    sortStr = "BestMatch";
            }

            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra(EXTRA_MIN, min);
            intent.putExtra(EXTRA_MAX, max);
            intent.putExtra(EXTRA_NEW, conditionNew);
            intent.putExtra(EXTRA_USED, conditionUsed);
            intent.putExtra(EXTRA_UNSPECIFIED, conditionUnspecified);
            intent.putExtra(EXTRA_SORT, sortStr);

            startActivity(intent);
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "Please fix all fields with errors";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    public void clear(View view) {
        mBookInput.setText("");
        mMinPrice.setText("");
        mMaxPrice.setText("");
        mNew.setChecked(false);
        mUsed.setChecked(false);
        mUnspecified.setChecked(false);
        mSpinner.setSelection(0);
        findViewById(R.id.error_keyword).setVisibility(View.GONE);
        findViewById(R.id.error_price).setVisibility(View.GONE);
    }

}
