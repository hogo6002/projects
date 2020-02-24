package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * This activity is used for customers to leave their feedback for both shopping centres and stores.
 */
public class FeedbackActivity extends AppCompatActivity {
    // the text view which can link user to policy page.
    private TextView policy;

    private TextView feedback;

    private TextView shopExperience;

    private TextView appExperience;

    private TextView suggestion;

    private Button submit;

    private TextView termAndCon;
    /**
     * Create the feedback page, by putting activity_feedback layout into this view, and set action
     * for both back button and policy button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ImageView backBtn = findViewById(R.id.backBtn);
        policy = findViewById(R.id.policy);
        feedback = findViewById(R.id.textView37);
        shopExperience = findViewById(R.id.textView38);
        appExperience = findViewById(R.id.textView39);
        suggestion = findViewById(R.id.textView40);
        submit = findViewById(R.id.button2);
        termAndCon = findViewById(R.id.textView33);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setListeners();
    }

    /**
     * set listeners to policy button.
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
    }

    /**
     * add the even for policy button.
     * when user click policy text view, it will bring user to PolicyActivity.
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(FeedbackActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void updateViews(Resources resources) {
        feedback.setText(resources.getString(R.string.feedback));
        shopExperience.setText(resources.getString(R.string.shopping_experience));
        appExperience.setText(resources.getString(R.string.app_experience));
        suggestion.setText(resources.getString(R.string.suggestion));
        submit.setText(resources.getString(R.string.submit));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }

}
