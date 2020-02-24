package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * the main menu for customers who has not login yet. they can only access some limited function.
 */
public class GuestActivity extends AppCompatActivity {

    //TODO: haven't finish all javadoc
    private ViewFlipper viewFlipper;
    private TextView policy;
    private TextView openTime;
    private TextView storeBtn;
    private TextView navBtn;
    private TextView infoBtn;
    private TextView termsAndCon;
    private Button loginBtn;

    /**
     * Create the view of this activity, it will show the activity_guest_main_menu layout.
     * and link it to multiple pages.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guset_main_menu);

        loginBtn = findViewById(R.id.loginBtn);
        policy = findViewById(R.id.policy);
        viewFlipper = findViewById(R.id.guest_main_menu_viewFlipper);
        openTime = findViewById(R.id.textView13);
        storeBtn = findViewById(R.id.stockBtn);
        navBtn = findViewById(R.id.promotionBtn);
        infoBtn = findViewById(R.id.profileBtn);
        termsAndCon = findViewById(R.id.textView10);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestActivity.this, CustomerLogin.class);
                startActivity(intent);
            }
        });
        setListeners();
        setClick();

    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();
        policy.setOnClickListener(onClick);
    }

    /**
     * add action for store button, nav button and centre info button. link them to specific page.
     */
    public void setClick() {
        goToStoreList();
        goToNavPage();
        goCentreInfo();
    }

    /**
     * set click function for both store icon and text, now user ca click those button to store list page.
     */
    public void goToStoreList() {
        ImageView storeBtnIcon = (ImageView) findViewById(R.id.stockBtnIcon);
        storeBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        TextView storeBtn = (TextView) findViewById(R.id.stockBtn);
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    /**
     * set click function for both nav icon and text, now user ca click those button to map navigation page.
     */
    public void goToNavPage() {

        ImageView navBtnIcon = (ImageView) findViewById(R.id.PromotionBtnIcon);
        navBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoresInfo();
            }
        });

        TextView navBtn = (TextView) findViewById(R.id.promotionBtn);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoresInfo();
            }
        });
    }

    public void getStoresInfo() {
        String type = "stores";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }

    /**
     * set click function for both centre info icon and text, now user ca click those button to centre information page.
     */
    public void goCentreInfo() {
        ImageView infoBtnIcon = (ImageView) findViewById(R.id.profileBtnIcon);
        infoBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        TextView infoBtn = (TextView) findViewById(R.id.profileBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestActivity.this, InfoActivity.class);
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
            System.out.println("button pressed");
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(GuestActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * Validate the current info
     */
    private void validate() {
        String type = "storelist";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }

    private void updateViews(Resources resources) {
        openTime.setText(resources.getString(R.string.Opening_hours));
        storeBtn.setText(resources.getString(R.string.stores));
        navBtn.setText(resources.getString(R.string.centre_nav));
        infoBtn.setText(resources.getString(R.string.centre_info));
        loginBtn.setText(resources.getString(R.string.login));
        termsAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }

}
