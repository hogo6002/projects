package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vritualbrowser.Adapter.StorePromotionViewAdaptor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The promotion activity
 */
public class StorePromotionsActivity extends AppCompatActivity {
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
    private static final String TAG = "StorePromotionActivity";
    /**
     * Image list
     */
    private ArrayList<String> mImage = new ArrayList<>();
    /**
     * storename list
     */
    private ArrayList<String> mProductName= new ArrayList<>();
    /**
     * promotion description list
     */
    private ArrayList<String> mPrice = new ArrayList<>();

    private ArrayList<String> mPromotionPrice = new ArrayList<>();

    private ArrayList<String> mProductId = new ArrayList<>();

    /**
     * resource
     */
    private String resource;
    /**
     * resource array
     */
    private ArrayList<String> resourceArray = new ArrayList<>();
    /**
     * A final resource array
     */
    private ArrayList<String[] > finalresourceArray = new ArrayList<>();

    private TextView promotion;

    private ImageView addBtn;
    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_promotions);
        policy = findViewById(R.id.policy);
        backBtn = findViewById(R.id.backFromPolicy);
        promotion = findViewById(R.id.textView33);
        resource = (String) getIntent().getExtras().get("resource");
        addBtn = findViewById(R.id.promotionAddBtn);
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
        addBtn.setOnClickListener(onClick);

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
                    intent = new Intent(StorePromotionsActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.promotionAddBtn:
                    //Todo allow selecting item. Preferablly by calling the stock list activity.
                    intent = new Intent(StorePromotionsActivity.this, StorePromotionAddActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * Initialize all the images
     */
    private void initImageBitMaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        System.out.println("resources: "+resource);

        String[] resources = resource.split("\\^");

        System.out.println("resources " + Arrays.toString(resources));

        for (String element: resources){
            resourceArray.add(element);
        }

        resourceArray.remove(0);

        for (String elements : resourceArray){
            String[] a = elements.split("\\*");
            finalresourceArray.add(a);
        }

        System.out.println(finalresourceArray);

        for(String[] finalresource:finalresourceArray){
            mImage.add(finalresource[0]);
            mProductName.add(finalresource[1]);
            mPrice.add(finalresource[2]);
            mPromotionPrice.add(finalresource[3]);
            mProductId.add(finalresource[4]);
        }

        System.out.println("Image"+mImage);
        System.out.println("ProductName"+mProductName);
        System.out.println("Price"+mPrice);
        System.out.println("PromotionPrice"+mPromotionPrice);
        System.out.println("ProductId"+mProductId);


        initRecycleView();
    }

    /**
     * Initialize the recyclerView for promotion
     */
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StorePromotionViewAdaptor adaptor = new StorePromotionViewAdaptor(mImage,mProductName,mPrice,mPromotionPrice, mProductId,this);
        //System.out.println(adaptor.getmImage());
        //System.out.println(adaptor.getmStoreName());
        //System.out.println(adaptor.getmStoreDescription());
        //System.out.println(adaptor.getItemCount());
        //System.out.println(adaptor.getmContext());

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateViews(Resources resources) {
        backBtn.setText(resources.getString(R.string.Promotions));
        promotion.setText(resources.getString(R.string.promotions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }

    private void validate() {
        String type = "storePromotion";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        System.out.println(type);
        backgroundWorker.execute(type);
    }


}
