package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

/**
 * Activity showing the store info
 */
public class StorePageActivity extends AppCompatActivity {
    /**
     * The policy
     */
    private TextView policy;
    /**
     * show on map button
     */
    private Button button;
    /**
     * an image url
     */
    private String image_url_s;
    /**
     * the store name
     */
    private String store_name_s;
    /**
     * the store location
     */
    private String store_location_s;
    /**
     * the weekday opening time
     */
    private String weektime_s;
    /**
     * the weekend opening time
     */
    private String weekendtime_s;

    private TextView openTimeLabel;

    private TextView weektime;

    private TextView weekendtime;

    private TextView locationLabel;

    private TextView termAndCon;


    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        image_url_s = (String) getIntent().getExtras().get("image_url");
        store_name_s = (String) getIntent().getExtras().get("store_name");
        store_location_s = (String) getIntent().getExtras().get("store_location");
        weektime_s = (String) getIntent().getExtras().get("weektime");
        weekendtime_s = (String) getIntent().getExtras().get("weekendtime");

        ImageView store_image = findViewById(R.id.store_image);
        TextView store_name = findViewById(R.id.store_name);
        TextView store_location = findViewById(R.id.store_location);
        weektime = findViewById(R.id.weektime);
        weekendtime = findViewById(R.id.weekendtime);
        openTimeLabel = findViewById(R.id.textView17);
        locationLabel = findViewById(R.id.textView47);



        Glide.with(this).load(image_url_s).into(store_image);
        store_name.setText(store_name_s);
        store_location.setText(store_location_s);
        weektime.setText("Mon - Fri: "+weektime_s);
        weekendtime.setText("Sat - Sun: " +weekendtime_s);

        // set the function of the back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        policy = findViewById(R.id.policy);
        button = findViewById(R.id.logoutBtn);
        termAndCon = findViewById(R.id.textView33);

        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        setListeners();

    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
        button.setOnClickListener(onClick);
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
                    intent = new Intent(StorePageActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logoutBtn:
                    String type = "stores";
                    String extra = store_name_s;
                    BackgroundWorker backgroundWorker = new BackgroundWorker(StorePageActivity.this);
                    backgroundWorker.execute(type, extra);
//                    intent = new Intent(StorePageActivity.this, NavActivity.class);
//                    intent.putExtra("storeName", store_name_s);
//                    startActivity(intent);
                    break;

            }
        }
    }

    private void updateViews(Resources resources) {
        openTimeLabel.setText(resources.getString(R.string.open_time));
        weektime.setText(resources.getString(R.string.Store_OpeningHour));
        weekendtime.setText(resources.getString(R.string.Store_OpeningHourHoliday));
        locationLabel.setText(resources.getString(R.string.location));
        button.setText(resources.getString(R.string.show_on_map));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));

    }
}
