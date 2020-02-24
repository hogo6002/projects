package com.example.vritualbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Cutomer Login activity
 */
public class CustomerLogin extends AppCompatActivity {

    /**
     * The username of the customer
     */
    private TextView username;
    /**
     * The password
     */
    private TextView password;
    /**
     * Login Button
     */
    private ImageButton loginBtn;
    /**
     * Register Button
     */
    private Button noAccount_btn;
    /**
     * policy
     */
    private TextView policy;

    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        // Finds the first descendant view with the given ID
        username = findViewById(R.id.C_login_username);
        password = findViewById(R.id.C_login_password);
        loginBtn = findViewById(R.id.Clogin_login_button);
        noAccount_btn = findViewById(R.id.C_login_no_account);
        policy = findViewById(R.id.policy);
        // set the underline for no account info
        noAccount_btn.setPaintFlags(noAccount_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        policyTextView = (TextView) findViewById(R.id.policyTextView);
//        policyTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CustomerLogin.this, PolicyActivity.class);
//                startActivity(intent);
//            }
//        });
        System.out.println(WelcomePage.resources);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        //System.out.println(WelcomePage.lang);
        setListeners();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();
        loginBtn.setOnClickListener(onClick);
        noAccount_btn.setOnClickListener(onClick);
        policy.setOnClickListener(onClick);
    }

    /**
     * Acheive the function of the buttons, use intent to connect current activity to another
     * activity
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // The intent, the operation to perform, for different buttons, switch to its activity
            Intent intent = null;
            switch (v.getId()) {
                case R.id.Clogin_login_button:
                    // check if the password corresponds with the database password of the username
                    validate(username.getText().toString(), password.getText().toString(),"0");
                    break;
                case R.id.C_login_no_account:
                    // switch to CustomerRegister activity
                    intent = new Intent(CustomerLogin.this, CustomerRegister.class);
                    startActivity(intent);
                    break;
                case R.id.policy:
                    // Switch to PolicyActivity
                    intent = new Intent(CustomerLogin.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * connect the database to validate the password
     * @param name - the username
     * @param pass - the password to validate
     */
    private void validate(String name, String pass, String userType) {
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, name,pass, userType);
    }

    private void updateViews(Resources resources) {

        username.setHint(resources.getString(R.string.login_username));
        System.out.println(resources.getString(R.string.welcome_page_select_customer));
        password.setHint(resources.getString(R.string.login_password));
        noAccount_btn.setText(resources.getString((R.string.no_account)));
    }
}
