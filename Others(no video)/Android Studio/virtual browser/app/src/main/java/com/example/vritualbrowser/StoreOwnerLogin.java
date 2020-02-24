package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * storeOwner Login activity
 */
public class StoreOwnerLogin extends AppCompatActivity {

    /**
     * The username of the storeOwner
     */
    private TextView username;
    /**
     * The password of the storeOwner
     */
    private TextView password;
    /**
     * Login Button
     */
    private ImageButton login_btn;
    /**
     * Register Button
     */
    private Button noAccount_btn;

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
        setContentView(R.layout.activity_store_owner_login);
        System.out.println("this is store owner");
        // Finds the first descendant view with the given ID
        username = findViewById(R.id.S_login_username);
        password = findViewById(R.id.S_login_password);
        login_btn = findViewById(R.id.Slogin_login_button);
        noAccount_btn = findViewById(R.id.S_login_no_account);

        /*
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        */
         

        //policy = findViewById(R.id.policy);

        // set the underline for no account info
        noAccount_btn.setPaintFlags(noAccount_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setListeners();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();
        login_btn.setOnClickListener(onClick);
        noAccount_btn.setOnClickListener(onClick);
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
                case R.id.Slogin_login_button:
                    validate(username.getText().toString(), password.getText().toString(),"1");
                    break;
                case R.id.S_login_no_account:
                    // switch to register activity
                    intent = new Intent(StoreOwnerLogin.this, StoreOwnerRegister.class);
                    startActivity(intent);
                //case R.id.policy:
                    // Switch to PolicyActivity
                //    intent = new Intent(StoreOwnerLogin.this, PolicyActivity.class);
                //    startActivity(intent);
                //    break;
            }
        }
    }


    private void updateViews(Resources resources) {
        //username.setHint(resources.getString(R.string.login_username));
        password.setHint(resources.getString(R.string.login_password));
        noAccount_btn.setText(resources.getString(R.string.no_account));
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
}
