package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Customer Register activity
 */
public class StoreOwnerRegister extends AppCompatActivity {

    /**
     * The storeOwner username
     */
    private EditText username;
    /**
     * The storeOwner email
     */
    private EditText email;
    /**
     * The storeOwner password
     */
    private EditText password;
    /**
     * StoreOwner confirm password
     */
    private EditText confirmPassword;
    /**
     * StoreOwner address one
     */
    private EditText addressOne;
    /**
     * StoreOwner address two
     */
    private EditText addressTwo;
    /**
     * storeOwner select state
     */
    private Spinner stateSelecter;
    /**
     * storeOwner back button
     */
    private ImageButton back;
    /**
     * Register confirm button
     */
    private ImageButton confirm;

    private TextView termAndCon;

    private TextView policy;

    private String chooseState = "select your state";

    private String queensland = "Queensland";

    private String newSouthWales = "New South Wales";

    private String wa = "Western Australia";

    private String sa = "South Australia";

    private String victoria = "Victoria";

    private String tasmania = "Tasmania";

    private FirebaseAuth mAuth;

    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_register);
        // Finds the first descendant view with the given ID
        username = findViewById(R.id.SRegister_username);
        email = findViewById(R.id.SRegister_Email);
        password = findViewById(R.id.SRegister_password);
        confirmPassword = findViewById(R.id.SRegister_confirmPassword);
        addressOne = findViewById(R.id.SRegister_Address1);
        addressTwo = findViewById(R.id.SRegister_Address2);
        stateSelecter = findViewById(R.id.SRegister_state_selector);
        back = findViewById(R.id.SRegister_back);
        confirm = findViewById(R.id.SRegister_confirm);
        termAndCon = findViewById(R.id.SRegister_bottomOne);
        policy = findViewById(R.id.SRegister_bottomTwo);


        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }

        mAuth = FirebaseAuth.getInstance();
        // initializing a String Array containing all states in Australia
        String[] statesList = new String[]{
                chooseState,
                newSouthWales,
                wa,
                queensland,
                sa,
                victoria,
                tasmania
        };
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, statesList) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Configure the dropdown list for state selection
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        stateSelecter.setAdapter(spinnerArrayAdapter);
        stateSelecter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setListeners();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onclick = new Onclick();
        back.setOnClickListener(onclick);
        confirm.setOnClickListener(onclick);
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
                case R.id.SRegister_back:
                    // jump back to storeOwnerLogin activity
                    intent = new Intent(StoreOwnerRegister.this, StoreOwnerLogin.class);
                    startActivity(intent);
                    break;
                case R.id.SRegister_confirm:
                    // todo: need to check if the information is valid, if yes, go stright into future customer page
                    if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                        validateBackEnd();
                    } else {
                        // stay at this activity
                        intent = new Intent(StoreOwnerRegister.this, StoreOwnerRegister.class);
                        startActivity(intent);
            }
        }}
    }

    private void updateViews(Resources resources) {
        username.setHint(resources.getString(R.string.login_username));
        password.setHint(resources.getString(R.string.login_password));
        email.setHint(resources.getString(R.string.register_email_hint));
        confirmPassword.setHint(resources.getString(R.string.register_conform_password));
        addressOne.setHint(resources.getString(R.string.register_address_one));
        addressTwo.setHint(resources.getString(R.string.register_address_two));
        chooseState = resources.getString(R.string.choose_the_state);
        newSouthWales = resources.getString(R.string.nsw);
        queensland = resources.getString(R.string.queensland);
        wa = resources.getString(R.string.westAU);
        sa = resources.getString(R.string.southAU);
        victoria = resources.getString(R.string.victoria);
        tasmania = resources.getString(R.string.tasmania);
        termAndCon.setText(resources.getString(R.string.register_bottom));
        policy.setText(resources.getString(R.string.register_bottom_two));
    }

    /**
     * Update the back end database
     */
    private void validateBackEnd() {
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username.getText().toString(), password.getText().toString(),
                email.getText().toString(), addressOne.getText().toString(),
                addressTwo.getText().toString(),"1");
    }

        /**
         * Check if all the details are correctly filled
         * @return true if all fields are correctly filled
         *         false otherwise
         */
        private boolean checkValid() {
            if (username.getText().toString().trim().length() > 16 ||
                    checkSpecialChar(username.getText().toString().trim()) || checkEmailFormat(email.getText().toString().trim()) ||
                    password.getText().toString().trim().length() > 16 || password.getText().toString().trim().length() < 6 ||
                    !password.getText().toString().equals(confirmPassword.getText().toString())) {
                System.out.println("not correct");
                return false;
            }else {
                return true;
            }
        }

        /***
         * Check if a string contains special character
         * @param str - the username to be checked
         * @return true if the username string contains special character , false otherwise
         */
        public boolean checkSpecialChar(String str) {
            String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.find();
        }

        /**
         * Check if the email format is valid
         * @param content - the email string to be checked
         * @return true if the email string is in correct format, false otherwise
         */
        private boolean checkEmailFormat(String content){

            String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
            Pattern p = Pattern.compile(REGEX);
            Matcher matcher=p.matcher(content);
            return matcher.matches();
        }


}
