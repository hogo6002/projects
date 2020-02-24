package com.example.vritualbrowser.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vritualbrowser.R;
import com.example.vritualbrowser.StockAddActivity;
import com.example.vritualbrowser.StockListActivity;
import com.example.vritualbrowser.StorePageActivity;
import com.example.vritualbrowser.StorePromotionAddActivity;

import java.util.ArrayList;


import static androidx.constraintlayout.widget.Constraints.TAG;

public class StockViewAdaptor extends RecyclerView.Adapter<StockViewAdaptor.ViewHolder>{
    // ArrayList storing promotion image url
    private ArrayList<String> mImage;

    // ArrayList storing store name strings
    private ArrayList<String> mStockName;

    //ArrayList storing promotion description strings
    private ArrayList<String> mStockAmount;

    private ArrayList<String> mPrice;

    private ArrayList<String> mProductId;

    //ArrayList storing the context of the activity
    private Context mContext;

    /**
     * Constructor of the PromotionViewAdaptor
     * @param mImage - list of image url
     * @param mStockName - list of storename
     * @param mStockAmount - list of promotion descrption
     * @param mContext the context of the calling activity
     */
    public StockViewAdaptor(ArrayList<String> mImage, ArrayList<String> mStockName, ArrayList<String> mStockAmount, ArrayList<String> mPrice,
                            ArrayList<String> mProductId, Context mContext,Intent intent) {
        this.mImage = mImage;
        this.mStockName = mStockName;
        this.mStockAmount = mStockAmount;
        this.mContext = mContext;
        this.mProductId = mProductId;
        this.mPrice = mPrice;
    }

    /**
     * Creating a new view holder by inflating from a XML layout file.
     * @param parent - The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("CreateView");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_stock, parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**
     * Update the content in the ViewHolder and reflect them at the given position.
     * @param holder - The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position - The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(mContext).asBitmap().load(mImage.get(position)).into(holder.image);
        holder.stockName.setText(mStockName.get(position));
        holder.stockAmount.setText("Stock: "+mStockAmount.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            /*
            Here store_location, weektime, weekendtime is passed into the next activity in order to display the desired content
            without a second query to the DB.
             */
            @Override
            public void onClick(View view) {
                if(((Activity)mContext).getIntent().getExtras().get("FROM_ACTIVITY").equals("promotionAdd")){
                    Intent intent = new Intent(mContext, StorePromotionAddActivity.class);
                    System.out.println(mImage.get(position));
                    intent.putExtra("image_url", mImage.get(position));
                    intent.putExtra("stock_name", mStockName.get(position));
                    intent.putExtra("stock_description", mStockAmount.get(position));
                    intent.putExtra("price",mPrice.get(position));
                    intent.putExtra("product_id", mProductId.get(position));
                    ((Activity)mContext).finish();
                    mContext.startActivity(intent);
                } else if(((Activity)mContext).getIntent().getExtras().get("FROM_ACTIVITY").equals("menu")){
                    Intent intent = new Intent(mContext, StockAddActivity.class);
                    System.out.println(mImage.get(position));
                    intent.putExtra("image_url", mImage.get(position));
                    intent.putExtra("stock_name", mStockName.get(position));
                    intent.putExtra("stock_description", mStockAmount.get(position));
                    intent.putExtra("price",mPrice.get(position));
                    intent.putExtra("product_id", mProductId.get(position));
                    mContext.startActivity(intent);
                } else if(((Activity)mContext).getIntent().getExtras().get("FROM_ACTIVITY").equals("StoreList")){
                    Intent intent = new Intent(mContext, StockAddActivity.class);
                    System.out.println(mImage.get(position));
                    intent.putExtra("image_url", mImage.get(position));
                    intent.putExtra("stock_name", mStockName.get(position));
                    intent.putExtra("stock_description", mStockAmount.get(position));
                    intent.putExtra("price",mPrice.get(position));
                    intent.putExtra("product_id", mProductId.get(position));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView stockName;
        TextView stockAmount;
        RelativeLayout parentLayout;
        TextView management;

        /**
         * Define each element in the ViewHolder by giving the id.
         * @param itemView - The item view that will be used in ViewHolder.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            stockName = itemView.findViewById(R.id.StoreName);
            stockAmount = itemView.findViewById(R.id.StoreDescription);
            parentLayout = itemView.findViewById(R.id.list_view);
            management = itemView.findViewById(R.id.right);


        }
    }

    /**
     * Return the context
     * @return context
     */
    public Context getmContext() {
        return mContext;
    }

    /**
     * Return the list of image_url
     * @return list of image_url
     */
    public ArrayList<String> getmImage() {
        return mImage;
    }

    /**
     * Return the list of store name
     * @return list of store name
     */
    public ArrayList<String> getmStockName() {
        return mStockName;
    }

    /**
     * Return the list of store description
     * @return list of store description
     */
    public ArrayList<String> getmStockAmount() {
        return mStockAmount;
    }


}
