package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vritualbrowser.Adapter.PromotionViewAdaptor;
import com.example.vritualbrowser.Adapter.RecyclerViewAdaptor;

import java.util.ArrayList;

/**
 * The promotion activity
 */
public class PromotionsActivity extends AppCompatActivity {
    /**
     * The policy
     */
    private TextView policy;
    /**
     * The back button
     */
    private TextView backBtn;
    /**
     * promotion tag
     */
    private static final String TAG = "PromotionActivity";
    /**
     * Image list
     */
    private ArrayList<String> mImage = new ArrayList<>();
    /**
     * storename list
     */
    private ArrayList<String> mStoreName = new ArrayList<>();
    /**
     * promotion description list
     */
    private ArrayList<String> mPromotionDescription = new ArrayList<>();

    private TextView promotion;
    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        policy = findViewById(R.id.policy);
        backBtn = findViewById(R.id.backFromPolicy);
        promotion = findViewById(R.id.textView33);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        // function the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setListeners();
        initImageBitMaps();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);

    }

    /**
     * Acheive the function of the buttons, use intent to connect current activity to another
     * activity
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(PromotionsActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * Initialize all the images
     */
    private void initImageBitMaps(){

        mImage.add("https://res.cloudinary.com/scentre-group-au/image/fetch/c_pad,b_auto,w_1152,h_1152,f_auto/https://cam.scentregroup.io/m/5a0e3a20331319b2");
        mStoreName.add("Kmart");
        mPromotionDescription.add("Buy what you love online at Kmart & pay later with Zip & Afterpay");

        mImage.add("https://res.cloudinary.com/scentre-group-au/image/fetch/c_pad,b_auto,w_1152,h_1152,f_auto/https://cam.scentregroup.io/m/399ad87543efabe2");
        mStoreName.add("Coles");
        mPromotionDescription.add("Welcome to Coles Supermarkets. View your local catalogue");

        mImage.add("https://skygate.com.au/sites/default/files/2017-11/Woolworths_logos_298x194px.jpg");
        mStoreName.add("Woolworths");
        mPromotionDescription.add("We're here to Help you Eat Fresher, Healthier Food");

        mImage.add("https://res.cloudinary.com/scentre-group-au/image/fetch/c_pad,b_auto,w_1152,h_1152,f_auto/https://cam.scentregroup.io/m/3f89a0caab73c2bd");
        mStoreName.add("David Jones");
        mPromotionDescription.add("Find a Range of Fashion, Beauty, Home & More From Your Favourite Bands");

        initRecycleView();
    }

    /**
     * Initialize the recyclerView for promotion
     */
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        PromotionViewAdaptor adaptor = new PromotionViewAdaptor(mImage,mStoreName,mPromotionDescription,this);
        System.out.println(adaptor.getmImage());
        System.out.println(adaptor.getmStoreName());
        System.out.println(adaptor.getmStoreDescription());
        System.out.println(adaptor.getItemCount());
        System.out.println(adaptor.getmContext());

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void updateViews(Resources resources) {
        backBtn.setText(resources.getString(R.string.Promotions));
        promotion.setText(resources.getString(R.string.promotions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }
}
