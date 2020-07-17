package com.example.android.ebsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TITLE = "title";
    private static final String ARG_PRICE = "price";
    private static final String ARG_SHIPPING = "shipping";
    private static final String ARG_BRAND = "brand";
    private static final String ARG_SUBTITLE = "subtitle";
    private static final String ARG_SPECIFICS = "specifics";
    private static final String ARG_PICURLS = "urls";
    private static final String ARG_SELLER = "seller";
    private static final String ARG_RETURN = "return";
    private static final String ARG_SHIPPINGINFO = "shippingInfo";

    String mPictureUrl;
    String mTitle;
    String mPrice;
    String mShipping;
    String mSubtitle;
    String mBrand;
    String mSpecifics;
    String mSeller;
    String mReturnPolicy;
    String mShippingInfo;

    public PagerAdapter(FragmentManager fm, int numOfTabs, ItemDetail itemDetail) {
        super(fm);
        mTitle = itemDetail.getTitle().toString();
        mPrice = itemDetail.getPrice().toString();
        mShipping = itemDetail.getShippingCost().toString();
        mPictureUrl = itemDetail.getPictureUrl().toString();
        mSubtitle = itemDetail.getSubtitle().toString();
        mBrand = itemDetail.getBrand().toString();
        mSpecifics = itemDetail.getSpecifics().toString();
        mSeller = itemDetail.getSeller().toString();
        mReturnPolicy = itemDetail.getReturnPolicy().toString();//new Gson().fromJson(itemDetail.getReturnPolicy().toString(), HashMap.class);
        mShippingInfo = itemDetail.getShippingInfo().toString();
//        for (String key : mReturnPolicy.keySet()) {
//            Log.v("aaa", key + " ：" + mReturnPolicy.get(key));
//        }
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                ProductFragment pf = new ProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString( ARG_PICURLS, mPictureUrl);
                bundle.putString( ARG_TITLE, mTitle);
                bundle.putString( ARG_PRICE, mPrice);
                bundle.putString( ARG_SHIPPING, mShipping);
                bundle.putString( ARG_BRAND, mBrand);
                bundle.putString( ARG_SUBTITLE, mSubtitle);
                bundle.putString( ARG_SPECIFICS, mSpecifics);
                pf.setArguments(bundle);

                return pf;
            case 1:
                SellerInfoFragment sif = new SellerInfoFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString( ARG_SELLER, mSeller);
                bundle2.putString( ARG_RETURN, mReturnPolicy);
                sif.setArguments(bundle2);
                return sif;
            case 2:
                ShippingFragment f = new ShippingFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putString(ARG_SHIPPINGINFO, mShippingInfo);
                f.setArguments(bundle3);

                return f;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
