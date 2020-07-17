package com.example.android.ebsearch;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class Item {
    private String mUrl;
    private String mImageUrl;
    private String mCreator;
    private String mShipping;
    private String mCondition;
    private String mPrice;
    private String mShippingInfo;
    private String mShippingCost;
    private String mItemId;
    private String mTop;

    public Item(String Url, String imageUrl, String creator, String shipping, String condition, String price, String shippingInfo, String shippingCost, String itemId, String top) {
        mUrl = Url;
        mImageUrl = imageUrl;
        mCreator = creator;
        mShipping = shipping;
        mCondition = condition;
        mPrice = price;
        mShippingInfo = shippingInfo;
        mShippingCost = shippingCost;
        mItemId = itemId;
        mTop = top;
    }
    public String getUrl() { return mUrl; }
    public String getImageUrl() {
        return mImageUrl;
    }
    public String getCreator() {
        return mCreator;
    }
    public String getShipping() {
        return mShipping;
    }
    public String getCondition() {
        return mCondition;
    }
    public String getPrice() {
        return mPrice;
    }
    public String getShippingInfo() {
        return mShippingInfo;
    }
    public String getShippingCost() {
        return mShippingCost;
    }
    public String getItemId() {
        return mItemId;
    }
    public String getTop() {
        return mTop;
    }
}
