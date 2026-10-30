package com.surya.allareearn;

import android.os.Bundle;
import android.widget.*;

// SURVEYS: opens the survey wall. Needs YOUR API key in AppConfig.SURVEY_API_KEY.
// Bitlabs: after signup, surveys open in a WebView and completion callbacks credit points.
// This placeholder tells you exactly what to wire once you have the key.
public class SurveyActivity extends BaseActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_survey);
        TextView status = findViewById(R.id.status);
        if (AppConfig.SURVEY_API_KEY.isEmpty()) {
            status.setText("Surveys are not set up yet.\n\nOwner: create a free account at bitlabs.ai or pollfish.com,\ncopy your API key into AppConfig.SURVEY_API_KEY, then surveys show here and completed ones credit points automatically.");
        } else {
            status.setText("Survey wall goes here (Bitlabs/Pollfish SDK).\nCompleted surveys credit points via the addEarnings call.");
            // Example credit after the survey network's completion callback:
            // addEarnings(paiseForSurvey, "survey completed");
        }
    }
}
