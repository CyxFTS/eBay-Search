package com.example.android.ebsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.ebsearch.DisplayMessageActivity.EXTRA_SHIPPINGINFO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TITLE = "title";
    private static final String ARG_PRICE = "price";
    private static final String ARG_SHIPPING = "shipping";
    private static final String ARG_BRAND = "brand";
    private static final String ARG_SUBTITLE = "subtitle";
    private static final String ARG_SPECIFICS = "specifics";
    private static final String ARG_PICURLS = "urls";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        ArrayList<String> urls = new Gson().fromJson(getArguments().getString( ARG_PICURLS), ArrayList.class);

        LinearLayout gallery = (LinearLayout) rootView.findViewById(R.id.gallery);

        if(urls.size()==0)
        {
            ImageView image = new ImageView(rootView.getContext());
            LinearLayout.LayoutParams viewParamsCenter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            image.setImageDrawable(rootView.getContext().getDrawable(R.drawable.ebay_default));
            image.setImageResource(R.drawable.bg_launcher);
            image.setLayoutParams(viewParamsCenter);
            gallery.addView(image);
        }

        for( int i = 0; i < urls.size(); i++) {
            String imageURL = String.valueOf(urls.get(i)).replace("[", "").replace("]", "");
            Log.v("pf", imageURL);
            ImageView image = new ImageView(rootView.getContext());
            LinearLayout.LayoutParams viewParamsCenter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Picasso.with(rootView.getContext()).load(imageURL).fit().centerInside().into(image);
            image.setImageResource(R.drawable.bg_launcher);
            image.setLayoutParams(viewParamsCenter);
            gallery.addView(image);
        }
        getArguments().getString( ARG_PICURLS);




        TextView tv_title = (TextView) rootView.findViewById(R.id.textView_product_title);
        tv_title.setText(getArguments().getString( ARG_TITLE));
        TextView tv_price = (TextView) rootView.findViewById(R.id.textView_product_price);
        String price = getArguments().getString( ARG_PRICE);
        tv_price.setText("$"+price);
        TextView tv_shipping = (TextView) rootView.findViewById(R.id.textView_product_shipping);
        String shipping = getArguments().getString( ARG_SHIPPING);
        if(Float.parseFloat(shipping)<=0)
            tv_shipping.setText("FREE Shipping");
        else
            tv_shipping.setText("Ships for $"+shipping);
        TextView tv_brand = (TextView) rootView.findViewById(R.id.textView_product_brand_content);
        String brandContent = getArguments().getString( ARG_BRAND).replace("[","").replace("\"","").replace("]","");
        if(brandContent.length() < 1) {
            rootView.findViewById(R.id.foo1).setVisibility(View.GONE);
        }
        tv_brand.setText(brandContent);
        TextView tv_subtitle = (TextView) rootView.findViewById(R.id.textView_product_subtitle_content);
        String subtitleContent = getArguments().getString( ARG_SUBTITLE).replace("[","").replace("\"","").replace("]","");
        if(subtitleContent.length() < 1) {
            rootView.findViewById(R.id.foo2).setVisibility(View.GONE);
        }
        if(brandContent.length() < 1 && subtitleContent.length() < 1) {
            rootView.findViewById(R.id.fo31).setVisibility(View.GONE);
            rootView.findViewById(R.id.grey_line1).setVisibility(View.GONE);
        }
        tv_subtitle.setText(subtitleContent);
        TextView tv_specifics = (TextView) rootView.findViewById(R.id.textView_specifics_content);
        tv_specifics.setText(getArguments().getString( ARG_SPECIFICS));

        ArrayList<String> a = new Gson().fromJson(getArguments().getString( ARG_SPECIFICS), ArrayList.class);

        mText = "<ul>";

        if(a.size()==0) {
            rootView.findViewById(R.id.fo313).setVisibility(View.GONE);
            rootView.findViewById(R.id.grey_line2).setVisibility(View.GONE);
        }

        for( int i = 0; i < a.size(); i++) {
            String t = String.valueOf(a.get(i)).replace("[","").replace("]","");
            Log.v("pf", t);

            mText += "<li>&nbsp;"+t+"</li>";
        }

        mText += "</ul>";


        // Display the spannable text to TextView
        tv_specifics.setText(Html.fromHtml(mText));

        return rootView;
    }
}