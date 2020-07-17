package com.example.android.ebsearch;

import org.json.JSONArray;

import java.util.HashMap;


public class ItemDetail {
    private JSONArray mPictureUrl;
    private JSONArray mSubtitle;
    private JSONArray mBrand;
    private JSONArray mSpecifics;
    private HashMap<String, Object> mSeller;
    private HashMap<String, Object> mReturnPolicy;

    private HashMap<String, Object> mShippingInfo;
    private String mTitle;
    private String mPrice;
    private String mShippingCost;

    public ItemDetail(String title, String price, String shippingCost, JSONArray pictureUrl, JSONArray subtitle, JSONArray brand, JSONArray specifics, HashMap<String, Object> seller, HashMap<String, Object> returnPolicy, HashMap<String, Object> shippingInfo) {
        mPictureUrl = pictureUrl;
        mSubtitle = subtitle;
        mBrand = brand;
        mSpecifics = specifics;
        mSeller = seller;
        mReturnPolicy = returnPolicy;

        mShippingInfo = shippingInfo;
        mTitle = title;
        mPrice = price;
        mShippingCost = shippingCost;
    }

    public JSONArray getPictureUrl() { return mPictureUrl; }
    public JSONArray getSubtitle() { return mSubtitle; }
    public JSONArray getBrand() { return mBrand; }
    public JSONArray getSpecifics() { return mSpecifics; }
    public HashMap<String, Object> getSeller() { return mSeller; }
    public HashMap<String, Object> getReturnPolicy() { return mReturnPolicy; }
    public HashMap<String, Object> getShippingInfo() { return mShippingInfo; }
    public String getTitle() { return mTitle; }
    public String getPrice() { return mPrice; }
    public String getShippingCost() { return mShippingCost; }
}
