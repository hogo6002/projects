/*
Code is referenced and adapted from https://www.youtube.com/watch?v=Vyqz_-sJGFk&t=965s.
 */
package com.example.vritualbrowser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vritualbrowser.R;
import com.example.vritualbrowser.StorePageActivity;

import java.util.ArrayList;



public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>{
    //ArrayList of store name;
    private ArrayList<String> mStoreName;

    //ArrayList of store description
    private ArrayList<String> mStoreDescription;

    //ArrayList of store image_url
    private ArrayList<String> mImage;

    //ArrayList of store location
    private ArrayList<String> mStoreLocation;

    //ArrayList of store week opening time;
    private ArrayList<String> mWeekTime;

    //ArrayList of store weekend opening time
    private ArrayList<String> mWeekendTime;

    //Context of the Activity
    private Context mContext;



    /**
     * Constructor of the RecyclerView Adaptor
     * @param mImage - list of image_url
     * @param mStoreName - list of store name
     * @param mStoreDescription - list of store description
     * @param mStoreLocation - list of store location
     * @param mWeekTime - list of week time
     * @param mWeekendTime - list of weekend time
     * @param mContext - Context of the activity
     */
    public RecyclerViewAdaptor(ArrayList<String> mImage, ArrayList<String> mStoreName, ArrayList<String> mStoreDescription, ArrayList<String> mStoreLocation, ArrayList<String> mWeekTime, ArrayList<String> mWeekendTime, Context mContext) {
        this.mImage = mImage;
        this.mStoreName = mStoreName;
        this.mStoreDescription = mStoreDescription;
        this.mStoreLocation = mStoreLocation;
        this.mWeekTime = mWeekTime;
        this.mWeekendTime = mWeekendTime;
        this.mContext = mContext;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent,false);
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
        holder.storeName.setText(mStoreName.get(position));
        holder.storeDescription.setText(mStoreDescription.get(position));


        // This code is used to change color for the itemView, however there are visual bugs.
        // It will be implemented before the Final exhibition.
        /*
        String[] mColor= {"#EC7475","D97E7D","A57C7B"};
        holder.itemView.setBackgroundColor(Color.parseColor(mColor[position%mColor.length]));

         */



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            /*
            Here store_location, weektime, weekendtime is passed into the next activity in order to display the desired content
            without a second query to the DB.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StorePageActivity.class);
                intent.putExtra("image_url", mImage.get(position));
                intent.putExtra("store_name", mStoreName.get(position));
                intent.putExtra("store_description", mStoreDescription.get(position));
                intent.putExtra("store_location",mStoreLocation.get(position));
                intent.putExtra("weektime",mWeekTime.get(position));
                intent.putExtra("weekendtime",mWeekendTime.get(position));

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView storeName;
        TextView storeDescription;
        RelativeLayout parentLayout;
        TextView right;

        /**
         * Define each element in the ViewHolder by giving the id.
         * @param itemView - The item view that will be used in ViewHolder.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            storeName = itemView.findViewById(R.id.StoreName);
            storeDescription = itemView.findViewById(R.id.StoreDescription);
            parentLayout = itemView.findViewById(R.id.list_view);
            right = itemView.findViewById(R.id.right);


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
    public ArrayList<String> getmStoreName() {
        return mStoreName;
    }

    /**
     * Return the list of store description
     * @return list of store description
     */
    public ArrayList<String> getmStoreDescription() {
        return mStoreDescription;
    }
}
