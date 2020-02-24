package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vritualbrowser.R;
import java.util.ArrayList;

/**
 * The profile activity
 */
public class ProfileActivity extends AppCompatActivity {
    /**
     * The policy
     */
    private TextView policy;
    /**
     * The profile
     */
    private String profile;
    /**
     * The username
     */
    private String username;
    /**
     * The user address 1
     */
    private String address1;
    /**
     * The user address 2
     */
    private String address2;

    private Button logoutBtn;

    private Button edit;

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
        setContentView(R.layout.activity_profile);

        profile = (String) getIntent().getExtras().get("PROFILE");
        username = splitData()[0];
        System.out.println("profile" + profile);

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        edit = findViewById(R.id.button4);
        termAndCon = findViewById(R.id.textView33);
        policy = findViewById(R.id.policy);
        ImageView backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        usernameTextView.setText(username);
        emailTextView.setText(splitData()[1]);
        addressTextView.setText(splitData()[2]);



        // logout button function
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CustomerLogin.class);
                startActivity(intent);
            }
        });

        // go back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setListeners();
    }

    /**
     * Set the onClick listener for the policy buttons
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
                    intent = new Intent(ProfileActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * split the string by comma
     * @return the splited string array
     */
    private String[] splitData(){
        return profile.split(",");
    }

    private void updateViews(Resources resources) {
        edit.setText(resources.getString(R.string.edit));
        logoutBtn.setText(resources.getString(R.string.log_out));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }
}
