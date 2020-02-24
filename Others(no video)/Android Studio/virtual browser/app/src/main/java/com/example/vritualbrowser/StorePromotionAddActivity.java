package com.example.vritualbrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class StorePromotionAddActivity extends AppCompatActivity{
    private TextView policy;
    private ImageView backBtn;
    private ImageView storeImage;
    private Button chooseProductButton;
    private EditText price;
    private EditText promotionPrice;
    private EditText promotionDescription;
    private Button confirmBtn;

    private String storeId = "1";
    private String productId;


    //Todo Add Barcode scanner for product_id scan
    //Todo Add support for store_id
    /**
     * Create the feedback page, by putting activity_feedback layout into this view, and set action
     * for both back button and policy button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_promotion_add);

        ImageView backBtn = findViewById(R.id.backBtn);
        policy = findViewById(R.id.policy);
        this.backBtn = findViewById(R.id.backBtn);
        storeImage = findViewById(R.id.store_image);
        chooseProductButton = findViewById(R.id.chooseProductBtn);
        price = findViewById(R.id.priceEdit);
        promotionPrice = findViewById(R.id.promotionPriceEdit);
        promotionDescription = findViewById(R.id.descriptionText);
        confirmBtn = findViewById(R.id.confirmButton);

        try{
        if(getIntent().getExtras().get("image_url")!=null){
            /*Uri uri = Uri.parse((String)getIntent().getExtras().get("image_url"));
            System.out.print(getIntent().getExtras().get("image_url"));
            storeImage.setImageURI(uri);*/
            Glide.with(this).asBitmap().load(getIntent().getExtras().get("image_url")).into(storeImage);
        }

        if(getIntent().getExtras().get("price")!=null){
            price.setText((String)getIntent().getExtras().get("price"));
        }

        if(getIntent().getExtras().get("promotionPrice")!=null){
            promotionPrice.setText((String)getIntent().getExtras().get("promotionPrice"));
        }

        if(getIntent().getExtras().get("Product_id")!=null){
            productId = (String)getIntent().getExtras().get("Product_id");
        }

        } catch (NullPointerException e){

        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setListeners();
    }

    /**
     * set listeners to policy button.
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
        chooseProductButton.setOnClickListener(onClick);
        confirmBtn.setOnClickListener(onClick);
    }

    /**
     * add the even for policy button.
     * when user click policy text view, it will bring user to PolicyActivity.
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(StorePromotionAddActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.chooseProductBtn:
                    validateStock();
                    break;
                case R.id.confirmButton:
                    validatePromotionAdd();
                    break;
            }
        }
    }


    /**
     * Update the back end database
     */


    private void validatePromotionAdd() {
        String type = "promotionAdd";
        System.out.println(productId);
        System.out.println(getIntent().getExtras().get("Product_id"));
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, ""+productId, ""+storeId,
                promotionPrice.getText().toString(), promotionDescription.getText().toString());
    }

    private void validateStock() {
        String type = "stock1";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        finish();
        backgroundWorker.execute(type);
    }


}
