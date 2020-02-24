package com.example.vritualbrowser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vritualbrowser.Entities.MapView;
import com.example.vritualbrowser.Entities.Route;
import com.example.vritualbrowser.Entities.Store;

/**
 * Map navigation activity, which will show the centre navigation view.
 */
public class NavActivity extends AppCompatActivity {
    private MapView map;
    LinearLayout mapView;
    EditText searchText;
    /**
     * Put the layout into this view, and add map view into a sub layout of centre navigation layout.
     * set the on click listener to back button.
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                            data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_navigation);
        mapView = findViewById(R.id.map_view);
        String storesInfo = (String) getIntent().getExtras().get("stores");
        String stores = "";
        String storeName = null;
        if ( storesInfo.split(":extra:").length > 1) {
            stores = storesInfo.split(":extra:")[0];
            storeName = storesInfo.split(":extra:")[1];
        } else {
            stores = storesInfo;
        }
        map = new MapView(this, stores);
        mapView.addView(map);

        searchText = (EditText)findViewById(R.id.editText3);
        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        if (storeName != null) {
            storeName = storeName.toLowerCase();

            for (Store store : map.getMall().getStores()) {
                if (map.getMall().getEnd() == null && store.getName().toLowerCase().equals(storeName)) {
                    map.getMall().addEnd(store);
                    searchText.setHint("Enter the start store name");
                }
                mapView.removeView(map);
                mapView.addView(map);
            }
        }



        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        updateMap();
                    }
                }
        );


        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void updateMap() {
        String name = searchText.getText().toString();
        name = name.toLowerCase();
        if (map.getMall().getBegin() != null && map.getMall().getEnd() != null) {
            map.getMall().addBegin(null);
            map.getMall().addEnd(null);
        }
        String storeName = (String) getIntent().getExtras().get("storeName");
        for (Store store : map.getMall().getStores()) {
            if (store.getName().toLowerCase().equals(name)) {
                if (map.getMall().getBegin() == null) {
                    map.getMall().addBegin(store);
                    searchText.setHint("Enter the destination store name");
                } else if (map.getMall().getEnd() == null) {
                    map.getMall().addEnd(store);
                    searchText.setHint("Enter the start store name");
                }
                mapView.removeView(map);
                mapView.addView(map);
                break;
            }
        }
    }
}
