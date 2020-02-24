package com.example.vritualbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// todo:
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * Customer Register activity
 */
public class CustomerRegister extends AppCompatActivity {

    /**
     * The customer username
     */
    private EditText username;
    /**
     * The customer email
     */
    private EditText email;
    /**
     * The customer password
     */
    private EditText password;
    /**
     * The confirm password
     */
    private EditText confirmPassword;
    /**
     * The customer address One
     */
    private EditText addressOne;
    /**
     * The cutomer address two
     */
    private EditText addressTwo;
    /**
     * A spinner to select which state customer is in
     */
    private Spinner stateSelecter;
    /**
     * Button to go back to customerLogin activity
     */
    private ImageButton back;
    /**
     * Button to confirm register and go to customerLogin activity
     */
    private ImageButton confirm;

    //todo:
    private FirebaseAuth mAuth;

    private String chooseState = "select your state";

    private String queensland = "Queensland";

    private String newSouthWales = "New South Wales";

    private String wa = "Western Australia";

    private String sa = "South Australia";

    private String victoria = "Victoria";

    private String tasmania = "Tasmania";

    private TextView termsAndCon;

    private TextView pricyPolicy;
    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        // Finds the first descendant view with the given ID
        username = findViewById(R.id.CRegister_username);
        email = findViewById(R.id.CRegister_Email);
        password = findViewById(R.id.CRegister_password);
        confirmPassword = findViewById(R.id.CRegister_confirmPassoword);
        addressOne = findViewById(R.id.CRegister_Adress1);
        addressTwo = findViewById(R.id.CRegister_Adress2);
        stateSelecter = findViewById(R.id.CRegister_state_selecter);
        back = findViewById(R.id.CRegister_back);
        confirm = findViewById(R.id.CRegister_confirm);
        termsAndCon = findViewById(R.id.SRegister_bottomOne);
        pricyPolicy = findViewById(R.id.SRegister_bottomTwo);

        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        //todo:
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
                if (position == 0) {
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
                case R.id.CRegister_back:
                    // jump back to customerLogin activity
                    intent = new Intent(CustomerRegister.this, CustomerLogin.class);
                    startActivity(intent);
                    break;
                case R.id.CRegister_confirm:
                    // todo: need to check if the information is valid, if yes, go stright into future customer page
                    // check confirmed password , database update
                    if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                        validateBackEnd();

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CustomerRegister.this, "Registered successful, please check your email for verification", Toast.LENGTH_LONG).show();
                                                    }else {
                                                        Toast.makeText(CustomerRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(CustomerRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } else {
                        // stay at this activity
                        intent = new Intent(CustomerRegister.this, CustomerRegister.class);
                        startActivity(intent);
                    }
            }
        }
    }

    /**
     * Update the back end database
     */
    private void validateBackEnd() {
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username.getText().toString(), password.getText().toString(),
                email.getText().toString(), addressOne.getText().toString(),
                addressTwo.getText().toString(),"0");
    }

    /**
     * Check if all fields are filled
     * @return true if all fields are filled, false otherwise
     */
    private boolean checkEmpty() {
        if (username.getText().toString().trim().length() == 0) {
            return false;
        } else if (email.getText().toString().trim().length() == 0) {
            return false;
        } else if (password.getText().toString().trim().length() == 0) {
            return false;
        } else if (confirmPassword.getText().toString().trim().length() == 0) {
            return false;
        } else if (addressOne.getText().toString().trim().length() == 0) {
            return false;
        } else if (addressTwo.getText().toString().trim().length() == 0) {
            return false;
        } else {
            return true;
        }
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
    public static boolean checkSpecialChar(String str) {
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
        termsAndCon.setText(resources.getString(R.string.register_bottom));
        pricyPolicy.setText(resources.getString(R.string.register_bottom_two));
    }
}
