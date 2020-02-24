package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.w3c.dom.Text;

/**
 * Activity showing a shoppingCenter list
 */
public class ShoppingCentreList extends AppCompatActivity {
    /**
     * the policy
     */
    private TextView policy;
    /**
     * the profile
     */
    private String profile;
    /**
     * the title
     */
    private TextView title;

    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_centre);
        TextView gardenCityBtn = findViewById(R.id.gardenCityBtn);
        policy = findViewById(R.id.policy);
        title = findViewById(R.id.textView30);

        if (WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }

        setListeners();

        profile = (String) getIntent().getExtras().get("PROFILE");

        // so far, only function the gardenCity button
        gardenCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bundle.get("parent").equals("guest")){
                        Intent intent = new Intent(ShoppingCentreList.this,GuestActivity.class);
                        startActivity(intent);

                } else {
                    System.out.println("customer");
                    Intent intent = new Intent(ShoppingCentreList.this, CustomerHome.class);
                    intent.putExtra("PROFILE", profile);
                    startActivity(intent);
                }
            }
        });
        // define and function the back button
        TextView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
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
                    intent = new Intent(ShoppingCentreList.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void updateViews(Resources resources) {
        title.setText(resources.getString(R.string.shopping_centres));
    }
}
