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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SHIPPINGINFO = "shippingInfo";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShippingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingFragment newInstance(String param1, String param2) {
        ShippingFragment fragment = new ShippingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_shipping, container, false);
        TextView tv_return = (TextView) rootView.findViewById(R.id.textView_shippingInfo_content);

        Log.v("sf", getArguments().getString( ARG_SHIPPINGINFO));
        String str = getArguments().getString( ARG_SHIPPINGINFO);

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
            Log.v("sf", key);
            if(key.contains("shippingServiceCost"))
                continue;
            else if(key.contains("__value__"))
                continue;
            String str1 = key.replaceAll("([^_])([A-Z])", "$1 $2");
            str1 = str1.substring(0, 1).toUpperCase() + str1.substring(1);
            Log.v("sf", str1 + " ：" + b.get(key));

            mText += "<li>&nbsp;<B>"+str1 + "</B> ：" + b.get(key).replace("[","").replace("]","")+"</li>";
        }

        mText += "</ul>";


        // Display the spannable text to TextView
        tv_return.setText(Html.fromHtml(mText));
        return rootView;
    }
}