package com.example.android.ebsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<Item> mItemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        mContext = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        Item currentItem = mItemList.get(i);
        String imageUrl = currentItem.getImageUrl();
        String name = currentItem.getCreator();
        String shipping = currentItem.getShipping();
        String condition = currentItem.getCondition();
        String price = currentItem.getPrice();
        String shippingInfo = currentItem.getShippingInfo();

        itemViewHolder.mTextViewName.setText(name);
        String top = "";
        if(currentItem.getTop().contains("true"))
            top = "<br><B>Top Rated Listing</B>";
        if(Float.parseFloat(shipping)<=0)
            itemViewHolder.mTextViewShipping.setText(Html.fromHtml("<B>FREE</B> Shipping"+top));
        else
            itemViewHolder.mTextViewShipping.setText(Html.fromHtml("Ships for <B>$"+shipping+"</B>"+top));
        itemViewHolder.mTextViewCondition.setText(condition);
        itemViewHolder.mTextViewPrice.setText(Html.fromHtml("<B>$"+price+"</B>"));

        ViewTreeObserver observer = itemViewHolder.mTextViewName.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int maxLines = (int) itemViewHolder.mTextViewName.getHeight()
                        / itemViewHolder.mTextViewName.getLineHeight();
                itemViewHolder.mTextViewName.setMaxLines(maxLines);
                itemViewHolder.mTextViewName.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        if(imageUrl.contains("ebaystatic.com/pict/")&&imageUrl.contains("4040_0.jpg"))
        {
            itemViewHolder.mImageView.setImageDrawable(mContext.getDrawable(R.drawable.ebay_default));
        }
        else
            Picasso.with(mContext).load(imageUrl).fit().centerInside().into(itemViewHolder.mImageView);
    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewShipping;
        public TextView mTextViewCondition;
        public TextView mTextViewPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewName = itemView.findViewById(R.id.text_view_name);
            mTextViewShipping = itemView.findViewById(R.id.text_view_shipping);
            mTextViewCondition = itemView.findViewById(R.id.text_view_condition);
            mTextViewPrice = itemView.findViewById(R.id.text_view_price);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }
}
