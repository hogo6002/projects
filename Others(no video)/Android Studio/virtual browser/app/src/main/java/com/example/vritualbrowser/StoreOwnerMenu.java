package com.example.vritualbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class StoreOwnerMenu extends AppCompatActivity{
    private TextView policy;
    private ViewFlipper viewFlipper;

    // some information which stores the user name and address.
    private String profile;

    private static final String TAG = "StoreMenu";

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

    private ImageView holder;

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

    /**
     * set activity_main_menu as the view of this page, and set the click listeners.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeowner_menu);
        policy = findViewById(R.id.policy);
        viewFlipper = findViewById(R.id.activity_main_menu_viewFlipper);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
        //resource =
        holder = findViewById(R.id.holder);

        profile = (String) getIntent().getExtras().get("PROFILE");

        setListeners();

        setClick();
        //initImageBitMaps();

    }

    private void setListeners() {
        Onclick onClick = new Onclick();
        policy.setOnClickListener(onClick);
    }

    /**
     * set click function to each button.
     */
    public void setClick() {
        goToStock();
        goToProfile();
        goToPromotion();
    }

    /**
     * set click function for both store icon and text, now user ca click those button to store list page.
     */
    public void goToStock() {
        ImageView stockBtnIcon = (ImageView) findViewById(R.id.stockBtnIcon);
        stockBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        TextView stockBtn = (TextView) findViewById(R.id.stockBtn);
        stockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();

            }
        });
    }

    /**
     * set click function for both adv promotion icon and text, now user ca click those button to advertisements promotion page.
     */
    public void goToPromotion() {
        ImageView promotionBtnIcon = (ImageView) findViewById(R.id.PromotionBtnIcon);
        promotionBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePromotion();
            }
        });

        TextView promotionBtn = (TextView) findViewById(R.id.promotionBtn);
        promotionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePromotion();
            }
        });
    }

    public void goToProfile() {
        ImageView profileBtnIcon = (ImageView) findViewById(R.id.profileBtnIcon);
        profileBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreOwnerMenu.this, ProfileActivity.class);
                intent.putExtra("PROFILE", profile);
                startActivity(intent);
            }
        });

        TextView profileBtn = (TextView) findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                Intent intent = new Intent(StoreOwnerMenu.this, ProfileActivity.class);
                intent.putExtra("PROFILE", profile);
                startActivity(intent);
            }
        });
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
                    intent = new Intent(StoreOwnerMenu.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * Initialize all the images
     *//*
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
        }

        System.out.println("Image"+mImage);
        System.out.println("ProductName"+mProductName);
        System.out.println("Price"+mPrice);
        System.out.println("PromotionPrice"+mPromotionPrice);


        initRecycleView();
    }*/
    /*
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StorePromotionViewAdaptor adaptor = new StorePromotionViewAdaptor(mImage,mProductName,mPrice,mPromotionPrice, this);
        //System.out.println(adaptor.getmImage());
        //System.out.println(adaptor.getmStoreName());
        //System.out.println(adaptor.getmStoreDescription());
        //System.out.println(adaptor.getItemCount());
        //System.out.println(adaptor.getmContext());

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }*/

    /**
     * Validate the current info
     */
    private void validate() {
        String type = "stock";
        System.out.println("Type: "+type);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }

    /**
     * Validate the current info
     */
    private void validatePromotion() {
        String type = "storePromotion";
        System.out.println("Type: "+type);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }


}
