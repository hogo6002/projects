package com.example.vritualbrowser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * The opening page of the application
 * Welcome page activity class
 */
public class WelcomePage extends AppCompatActivity {

    /**
     * The textview widget to select as customer - work the same as Button
     */
    private TextView selectCustomer;
    /**
     * The textview widget to select as storeOwner - work the same as Button
     */
    private TextView selectStoreOwner;
    /**
     * A viewFlipper for the slide show
     */
    private ViewFlipper viewFlipper;
    /**
     * A Button for the guset
     */
    private TextView guestBtn;
    /**
     * Policy
     */
    private TextView policy;

    /**
     * A button to change language
     */
    private Button changeLang;

    public static String lang = "en";

    public static Resources resources;

    /**
     * Initialize the activity
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                           data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_welcome_page);
        // Finds the first descendant view with the given ID
        selectCustomer = findViewById(R.id.welcome_page_select_customer);
        selectStoreOwner = findViewById(R.id.welcome_page_select_storeowners);
        viewFlipper = findViewById(R.id.welcome_page_viewFlipper);
        // set the time interval a slide is showing
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
        guestBtn = findViewById(R.id.guestBtn);
        policy = findViewById(R.id.welcome_page_privacy);
        changeLang = findViewById(R.id.change_language);
        //resources = LocaleHelper.setLocale(this, "en").getResources();
        setListeners();

    }


    /**
     * Set the onClick listener for all the buttons
     */
    public void setListeners() {
        Onclick onclick = new Onclick();
        selectCustomer.setOnClickListener(onclick);
        selectStoreOwner.setOnClickListener(onclick);
        guestBtn.setOnClickListener(onclick);
        policy.setOnClickListener(onclick);
        changeLang.setOnClickListener(onclick);
    }

    @Override
    public String toString() {
        return "WelcomePage";
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    /**
     * Acheive the function of the buttons, use intent to connect current activity to another
     * activity
     */
    private class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // The intent, the operation to perform, for different buttons, switch to its activity
            Intent intent = null;
            switch (v.getId()) {
                case R.id.welcome_page_select_customer:
                    // jump to CustomerLogin activity
                    intent = new Intent(WelcomePage.this, CustomerLogin.class);
                    startActivity(intent);
                    break;
                case R.id.welcome_page_select_storeowners:
                    // jump to StoreOwnerLogin activity
                    intent = new Intent(WelcomePage.this, StoreOwnerLogin.class);
                    startActivity(intent);
                    break;
                case R.id.guestBtn:
                    // jump to ShoppingCenterList activity
                    intent = new Intent(WelcomePage.this, ShoppingCentreList.class);
                    intent.putExtra("parent", "guest");
                    startActivity(intent);
                    WelcomePage.this.setResult(1,intent);
                    break;
                case R.id.welcome_page_privacy:
                    intent = new Intent(WelcomePage.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.change_language:
                    // show AlertDialog to display list of languages, one can be selected
                    //LocaleHelper.setLocale(WelcomePage.this, "zh-rHK");
                    //recreate ();
                    System.out.println(Locale.getDefault().getLanguage());
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        updateViews("zh");
                    } else {
                        updateViews("en");
                    }
            }
        }
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();
        lang = languageCode;
        System.out.println(Locale.getDefault().getLanguage());
        System.out.println(resources);
        WelcomePage.resources = resources;
        selectCustomer.setText(resources.getString(R.string.welcome_page_select_customer));
        System.out.println(selectCustomer.getText().toString());
        selectStoreOwner.setText(resources.getString(R.string.welcome_page_select_storeOwner));
        guestBtn.setText(resources.getString(R.string.welcome_page_select_guest));
    }

    private void showChangeLanguageDialog() {
        // array of languages to display in alert dialog
        final String[] listItems = {"中文（繁）", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(WelcomePage.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("zh-rHK");
                    recreate();
                }
                else if (i == 1) {
                    setLocale("en");
                    recreate();
                }

                // dismiss alert dialog when language selected
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        // show alert dialog
        mDialog.show();

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language in shared preferences
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}
