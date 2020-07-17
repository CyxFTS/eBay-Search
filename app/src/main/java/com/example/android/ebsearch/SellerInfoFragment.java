package com.example.android.ebsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SELLER = "seller";
    private static final String ARG_RETURN = "return";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerInfoFragment newInstance(String param1, String param2) {
        SellerInfoFragment fragment = new SellerInfoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_seller_info, container, false);

        TextView tv_specifics = (TextView) rootView.findViewById(R.id.textView_sellerInfo_content);
        HashMap<String, Object> a = new Gson().fromJson(getArguments().getString( ARG_SELLER), HashMap.class);
        //Log.v("sif", getArguments().getString( ARG_SELLER));
        mText = "<ul>";

        for( String key : a.keySet()) {
            //String t = String.valueOf(key).replace("[","").replace("]","");
            String str1 = key.replaceAll("([^_])([A-Z])", "$1 $2");
            Log.v("sif", str1 + " ：" + a.get(key));

            mText += "<li>&nbsp;<B>"+str1 + "</B> ：" + a.get(key)+"</li>";
        }

        mText += "</ul>";


        // Display the spannable text to TextView
        tv_specifics.setText(Html.fromHtml(mText));

        TextView tv_return = (TextView) rootView.findViewById(R.id.textView_return_policy_content);

        Log.v("sif", getArguments().getString( ARG_RETURN));
        String str = getArguments().getString( ARG_RETURN);

        Properties props = new Properties();
        try {
            props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map2 = new HashMap<String, String>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            map2.put((String)e.getKey(), (String)e.getValue());
        }

        Map<String, String> b = map2; //= new Gson().fromJson(getArguments().getString( ARG_RETURN), HashMap.class);

        mText = "<ul>";

        for( String key : b.keySet()) {
            //String t = String.valueOf(key).replace("[","").replace("]","");
            String str1 = key.replaceAll("([^_])([A-Z])", "$1 $2");
            Log.v("sif", str1 + " ：" + b.get(key));

            mText += "<li>&nbsp;<B>"+str1 + "</B> ：" + b.get(key)+"</li>";
        }

        mText += "</ul>";


        // Display the spannable text to TextView
        tv_return.setText(Html.fromHtml(mText));
        if(a.size() < 1) {
            rootView.findViewById(R.id.textView_sellerInfo).setVisibility(View.GONE);
            rootView.findViewById(R.id.textView_sellerInfo_content).setVisibility(View.GONE);
            rootView.findViewById(R.id.seller_line).setVisibility(View.GONE);
        }

        if(b.size() < 1) {
            rootView.findViewById(R.id.textView_return_policy).setVisibility(View.GONE);
            rootView.findViewById(R.id.textView_return_policy_content).setVisibility(View.GONE);
            rootView.findViewById(R.id.seller_line).setVisibility(View.GONE);
        }
        return rootView;
    }
}