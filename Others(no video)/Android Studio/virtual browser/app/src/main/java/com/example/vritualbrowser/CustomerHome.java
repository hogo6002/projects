package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The home page to show all the activities that customers are allowed to use.
 * Set each button on click listener, which can bring customers to different pages..
 */
public class CustomerHome extends AppCompatActivity {

    //TODO: some methods are needed to add some comment.

    private TextView policy;
    private ViewFlipper viewFlipper;

    // some information which stores the user name and address.
    private String profile;

    private TextView opentime;

    private TextView storeLabel;

    private TextView navLabel;

    private TextView infoLabel;

    private TextView promotionLabel;

    private TextView profileLabel;

    private TextView feedbackLabel;

    private TextView termAndCon;

    private ImageView storeBtnIcon;
    /**
     * set activity_main_menu as the view of this page, and set the click listeners.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        policy = findViewById(R.id.policy);
        viewFlipper = findViewById(R.id.activity_main_menu_viewFlipper);
        opentime = findViewById(R.id.textView13);
        storeLabel = findViewById(R.id.storeBtn);
        navLabel = findViewById(R.id.navBtn);
        infoLabel = findViewById(R.id.infoBtn);
        promotionLabel = findViewById(R.id.advBtn);
        profileLabel = findViewById(R.id.profileBtn);
        feedbackLabel = findViewById(R.id.feedbackBtn);
        termAndCon = findViewById(R.id.textView10);
        storeBtnIcon = findViewById(R.id.storeBtnIcon);

        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }

        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
        profile = (String) getIntent().getExtras().get("PROFILE");

        setListeners();

        setClick();

    }
    
    private void setListeners() {
        Onclick onClick = new Onclick();
        policy.setOnClickListener(onClick);
    }

    /**
     * set click function to each button.
     */
    public void setClick() {
        goToStoreList();
        goToNavPage();
        goCentreInfo();
        goToProfile();
        goToFeedback();
        goToPromotion();
    }

    /**
     * set click function for both store icon and text, now user ca click those button to store list page.
     */
    public void goToStoreList() {
        storeBtnIcon = (ImageView) findViewById(R.id.storeBtnIcon);
        storeBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        TextView storeBtn = (TextView) findViewById(R.id.storeBtn);
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

        ImageView navBtnIcon = (ImageView) findViewById(R.id.navBtnIcon);
        navBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(CustomerHome.this, NavActivity.class);
//                startActivity(intent);
                getStoresInfo();
            }
        });

        TextView navBtn = (TextView) findViewById(R.id.navBtn);
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
        ImageView infoBtnIcon = (ImageView) findViewById(R.id.infoBtnIcon);
        infoBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        TextView infoBtn = (TextView) findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * set click function for both profile icon and text, now user ca click those button to user profile page.
     * Also, in this method, it will pass the "PROFILE" information to ProfileActivity.class, so that, it will display user information.
     */
    public void goToProfile() {
        ImageView profileBtnIcon = (ImageView) findViewById(R.id.profileBtnIcon);
        profileBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, ProfileActivity.class);
                intent.putExtra("PROFILE", profile);
                startActivity(intent);
            }
        });

        TextView profileBtn = (TextView) findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                Intent intent = new Intent(CustomerHome.this, ProfileActivity.class);
                intent.putExtra("PROFILE", profile);
                startActivity(intent);
            }
        });
    }

    /**
     * set click function for both feedback icon and text, now user ca click those button to user feedback page.
     */
    public void goToFeedback() {
        ImageView feedbackBtnIcon = (ImageView) findViewById(R.id.feedbackBtnIcon);
        feedbackBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        TextView feedbackBtn = (TextView) findViewById(R.id.feedbackBtn);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * set click function for both adv promotion icon and text, now user ca click those button to advertisements promotion page.
     */
    public void goToPromotion() {
        ImageView advBtnIcon = (ImageView) findViewById(R.id.advBtnIcon);
        advBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });

        TextView advBtn = (TextView) findViewById(R.id.advBtn);
        advBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this, PromotionsActivity.class);
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
                    intent = new Intent(CustomerHome.this, PolicyActivity.class);
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
        opentime.setText(resources.getString(R.string.Opening_hours));
        storeLabel.setText(resources.getString(R.string.stores));
        navLabel.setText(resources.getString(R.string.centre_nav));
        infoLabel.setText(resources.getString(R.string.centre_info));
        promotionLabel.setText(resources.getString(R.string.promotion));
        profileLabel.setText(resources.getString(R.string.profile));
        feedbackLabel.setText(resources.getString(R.string.feedback));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }
}
