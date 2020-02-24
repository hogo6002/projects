package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity to show the info of a shopping center
 */
public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * the policy
     */
    private TextView policy;
    /**
     * A mapview which displays the google map
     */
    private MapView mapView;
    /**
     * A google map object
     */
    private GoogleMap gmap;
    /**
     * A string for the mapViewBundle
     */
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private TextView storeName;

    private TextView openNow;

    private TextView monToFriOt;

    private TextView satToSunOt;

    private TextView centerInfo;

    private TextView freeParking;

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
        setContentView(R.layout.activity_centre_information);

        // for the mapview
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        // Finds the first descendant view with the given ID
        storeName = findViewById(R.id.storeName);
        mapView = findViewById(R.id.mapView2);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        ImageView backBtn = findViewById(R.id.backBtn);
        openNow = findViewById(R.id.textView21);
        monToFriOt = findViewById(R.id.textView22);
        satToSunOt = findViewById(R.id.textView23);
        centerInfo = findViewById(R.id.textView24);
        freeParking = findViewById(R.id.textView27);
        termAndCon = findViewById(R.id.textView10);
        policy = findViewById(R.id.policy);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
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
     * invoked when the activity may be temporarily destroyed, save the instance state here
     * @param outState - the state to be saved
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);

    }

    /**
     * invokes when the activity enters the Resumed state,
     * The app stays in this state until something happens to take focus away from the app
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * invokes when the activity enters the Started state,
     * as the app prepares for the activity to enter the foreground and become interactive
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * invokes when your activity is no longer visible to the user
     */
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * calls this method as the first indication that the user is leaving your activity
     * (though it does not always mean the activity is being destroyed);
     * it indicates that the activity is no longer in the foreground
     * (though it may still be visible if the user is in multi-window mode).
     */
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /**
     * Is called before the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * called when the overall system is running low on memory,
     * and actively running processes should trim their memory usage
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * The activity to display when the google map is ready
     * @param googleMap - the connected google map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(14);
        String place =(String) storeName.getText();
        LatLng ny = null;
        switch (place) {
            case "Garden City":
                ny = new LatLng(-27.5633459, 153.0834233);
                gmap.addMarker(new MarkerOptions().position(ny).title("Garden City Westfield"));
                gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        }
        //LatLng ny = new LatLng(40.7143528, -74.0059731);
        //gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));


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
                    intent = new Intent(InfoActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void updateViews(Resources resources) {
        openNow.setText(resources.getString(R.string.open_now));
        //openNow.setTextSize(21);
        monToFriOt.setText(resources.getString(R.string.OpeningHours));
        satToSunOt.setText(resources.getString(R.string.WeekendOpeningHours));
        centerInfo.setText(resources.getString(R.string.centre_info));
        freeParking.setText(resources.getString(R.string.storeParkingInfo));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }
}
