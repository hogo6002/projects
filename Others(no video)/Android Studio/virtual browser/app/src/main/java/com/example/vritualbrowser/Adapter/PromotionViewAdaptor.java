/*
Code is referenced and adapted from https://www.youtube.com/watch?v=Vyqz_-sJGFk&t=965s.
This class is not fully implemented yet as it is held and will be completed after the MVP.
 */
package com.example.vritualbrowser.Adapter;

import android.content.Context;
import android.content.Intent;
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

public class PromotionViewAdaptor extends RecyclerView.Adapter<PromotionViewAdaptor.ViewHolder>{

    // ArrayList storing promotion image url
    private ArrayList<String> mImage;

    // ArrayList storing store name strings
    private ArrayList<String> mStoreName;

    //ArrayList storing promotion description strings
    private ArrayList<String> mPromotionDescription;

    //ArrayList storing the context of the activity
    private Context mContext;


    /**
     * Constructor of the PromotionViewAdaptor
     * @param mImage - list of image url
     * @param mStoreName - list of storename
     * @param mPromotionDescription - list of promotion descrption
     * @param mContext the context of the calling activity
     */
    public PromotionViewAdaptor(ArrayList<String> mImage, ArrayList<String> mStoreName, ArrayList<String> mPromotionDescription, Context mContext) {
        this.mImage = mImage;
        this.mStoreName = mStoreName;
        this.mPromotionDescription = mPromotionDescription;
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
        holder.storeDescription.setText(mPromotionDescription.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StorePageActivity.class);
                intent.putExtra("image_url", mImage.get(position));
                intent.putExtra("store_name", mStoreName.get(position));
                intent.putExtra("promotion_description", mPromotionDescription.get(position));

                mContext.startActivity(intent);
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

    /**
     * Define the ViewHolder class with elements within the holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        //Promotion image
        ImageView image;

        //Storename
        TextView storeName;

        //Store description
        TextView storeDescription;

        //The viewholder parent layout
        RelativeLayout parentLayout;

        //The right text ">"
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
        return mPromotionDescription;
    }
}
