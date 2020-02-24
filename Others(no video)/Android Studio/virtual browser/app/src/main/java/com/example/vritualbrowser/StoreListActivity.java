package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vritualbrowser.Adapter.RecyclerViewAdaptor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity showing the store list
 */
public class StoreListActivity extends AppCompatActivity {
    /**
     * String tag
     */
    private static final String TAG = "MainActivity";
    /**
     * The policy
     */
    private TextView policy;
    /**
     * The profile
     */
    private ImageView profile;
    /**
     * Image list
     */
    private ArrayList<String> mImage = new ArrayList<>();
    /**
     * Storename list
     */
    private ArrayList<String> mStoreName = new ArrayList<>();
    /**
     * storeDescription list
     */
    private ArrayList<String> mStoreDescription = new ArrayList<>();
    /**
     * StoreLocation list
     */
    private ArrayList<String> mStoreLocation = new ArrayList<>();
    /**
     * mweek time list
     */
    private ArrayList<String> mWeekTime = new ArrayList<>();
    /**
     * mweekend time list
     */
    private ArrayList<String> mWeekendTime = new ArrayList<>();
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


    private TextView storeListGC;

    private TextView termAndCon;

    private EditText searchtext;

    private ImageView searchButton;

    private String originalMessage;


    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);

        ImageView backBtn = findViewById(R.id.backBtn);
        storeListGC = findViewById(R.id.textView55);
        termAndCon = findViewById(R.id.textView10);

        Log.d(TAG, "onCreate: started.");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        policy = findViewById(R.id.policy);
        profile = findViewById(R.id.profile);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        resource = (String) getIntent().getExtras().get("resource");
        searchtext = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchBtn);

        System.out.println("Intent: "+getIntent().getExtras().get("FROM_ACTIVITY"));
        if(getIntent().getExtras().get("FROM_ACTIVITY").equals("menu")){
            originalMessage = resource;
        } else if(getIntent().getExtras().get("FROM_ACTIVITY").equals("StoreList")){
            originalMessage = getIntent().getExtras().get("original").toString();
        }

        System.out.println("Original: "+originalMessage);

        setListeners();
        initImageBitMaps();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
        profile.setOnClickListener(onClick);
        searchButton.setOnClickListener(onClick);

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
                    intent = new Intent(StoreListActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    intent = new Intent(StoreListActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    break;
                /*
                case R.id.searchBar:
                    System.out.println("clear MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE6");
                    searchtext.getText().clear();

                 */

                case R.id.searchBtn:
                    String result = passContent();
                    if(result.isEmpty()){
                        System.out.println("empty");
                        validate();
                    } else {
                        System.out.println("filtered");
                        StoreListActivity.this.finish();
                        intent = new Intent(StoreListActivity.this, StoreListActivity.class);
                        intent.putExtra("resource",result);
                        intent.putExtra("original", originalMessage);
                        intent.putExtra("FROM_ACTIVITY", "StoreList");
                        startActivity(intent);
                    }


            }
        }
    }

    /**
     * Initialize all the images
     */
    private void initImageBitMaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");



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
            mStoreName.add(finalresource[1]);
            mStoreDescription.add(finalresource[2]);
            mStoreLocation.add(finalresource[3]);
            mWeekTime.add(finalresource[4]);
            mWeekendTime.add(finalresource[5]);
        }

        System.out.println("Image"+mImage);
        System.out.println("StoreName"+mStoreName);
        System.out.println("StoreDescripition"+mStoreDescription);
        System.out.println("StoreLocation"+mStoreLocation);
        System.out.println("WeekTime"+mWeekTime);
        System.out.println("WeekEndTime"+mWeekendTime);

        initRecycleView();
    }

    /**
     * Initialize the recyclerView for promotion
     */
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdaptor adaptor = new RecyclerViewAdaptor(mImage,mStoreName,mStoreDescription,mStoreLocation,mWeekTime,mWeekendTime, this);
        //System.out.println(adaptor.getmImage());
        //System.out.println(adaptor.getmStoreName());
        //System.out.println(adaptor.getmStoreDescription());
        //System.out.println(adaptor.getItemCount());
        //System.out.println(adaptor.getmContext());

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void updateViews(Resources resources) {
        storeListGC.setText(resources.getString(R.string.store_list_for_garden_city));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }

    private String passContent(){
        String result = "storelist";
        String searchString = searchtext.getText().toString().toLowerCase().trim();
        System.out.println("EditText: "+searchString);

        if(searchString.length()!=0){
            System.out.println("length success");
            getFullList();
            for(int i=0; i<mStoreName.size(); i++) {
                if (mStoreName.get(i).toLowerCase().contains(searchString)) {
                    System.out.println("Can find the name in list");
                    String item = mImage.get(i) + "*" + mStoreName.get(i) + "*" + mStoreDescription.get(i) + "*" +
                            mStoreLocation.get(i) + "*" + mWeekTime.get(i) + "*" + mWeekendTime.get(i);

                    result = result + "^" + item;
                }
            }
        } else{
            getFullList();
            for(int i=0; i<mStoreName.size(); i++) {

                    String item = mImage.get(i) + "*" + mStoreName.get(i) + "*" + mStoreDescription.get(i) + "*" +
                            mStoreLocation.get(i) + "*" + mWeekTime.get(i) + "*" + mWeekendTime.get(i);

                    result = result + "^" + item;

            }
        }
        System.out.println("Result: "+result);

        return result;

    }

    private void validate() {
        String type = "storelist";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }

    private void getFullList() {
        if(getIntent().getExtras().get("FROM_ACTIVITY").equals("StoreList")){
            String[] resources = ((String)getIntent().getExtras().get("original")).split("\\^");
            resourceArray.clear();
            for (String element: resources){
                resourceArray.add(element);
            }
            resourceArray.remove(0);
            finalresourceArray.clear();
            for (String elements : resourceArray){
                String[] a = elements.split("\\*");
                finalresourceArray.add(a);
            }
            mImage.clear();
            mStoreName.clear();
            mStoreDescription.clear();
            mStoreLocation.clear();
            mWeekTime.clear();
            mWeekendTime.clear();
            for(String[] finalresource:finalresourceArray){
                mImage.add(finalresource[0]);
                mStoreName.add(finalresource[1]);
                mStoreDescription.add(finalresource[2]);
                mStoreLocation.add(finalresource[3]);
                mWeekTime.add(finalresource[4]);
                mWeekendTime.add(finalresource[5]);
            }
        }
    }

    private String searchText(String searchString, String result) {
        for(int i=0; i<mStoreName.size(); i++) {
            if (mStoreName.get(i).toLowerCase().contains(searchString)) {
                System.out.println("Can find the name in list");
                String item = mImage.get(i) + "*" + mStoreName.get(i) + "*" + mStoreDescription.get(i) + "*" +
                        mStoreLocation.get(i) + "*" + mWeekTime.get(i) + "*" + mWeekendTime.get(i);

                result = result + "^" + item;
            }
        }
        return result;

    }
}
