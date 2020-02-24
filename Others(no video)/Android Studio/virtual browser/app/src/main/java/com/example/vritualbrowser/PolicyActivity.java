package com.example.vritualbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * this class is used to show the policy of this app.
 * it will bring user to an new page which will show all the policy of the app.
 * user can back to the previous page by clicking back button.
 */
public class PolicyActivity extends AppCompatActivity {

    private TextView backFromPolicy;

    private TextView version;

    private TextView termAndCon;

    private TextView policy;

    /**
     * create policy view and put the activity_terms_policy layout into this view.
     * give the action to the back button.
     * @param savedInstanceState previous saved state of this activity, can be null if no
     *                           data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_policy);
        policy = findViewById(R.id.textView35);
        version = findViewById(R.id.textView52);
        backFromPolicy = findViewById(R.id.backFromPolicy);
        termAndCon = findViewById(R.id.textView33);
        if(WelcomePage.resources != null) {
            updateViews(WelcomePage.resources);
        }
        policy.setMovementMethod(new ScrollingMovementMethod());

        backFromPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateViews(Resources resources) {
        backFromPolicy.setText(resources.getString(R.string.terms_and_policy));
        version.setText(resources.getString(R.string.version));
        termAndCon.setText(resources.getString(R.string.terms_conditions));
        policy.setText(resources.getString(R.string.privacy_policy));
    }
}
