package com.surya.allareearn;

// ALL app settings live here. Change numbers in ONE place.
public class AppConfig {
    // AdMob IDs (real, from Surya's AdMob account)
    public static final String ADMOB_APP_ID = "ca-app-pub-8736860565828395~2294437877";
    public static final String REWARDED_AD_UNIT = "ca-app-pub-8736860565828395/2118343909";

    // Points system: 1 point = Rs 1, each ad gives 0.1 points.
    // Points are stored as PAISE (1 point = 100 paise) to avoid decimals.
    public static final long PAISE_PER_POINT = 100;      // 1 point = 100 paise
    public static final long PAISE_REWARD_PER_AD = 10;   // 0.1 point = 10 paise per ad
    public static final long MIN_WITHDRAW_PAISE = 5000;  // Rs 50 minimum withdrawal

    // Survey network: put YOUR Bitlabs or Pollfish API key here after signing up.
    public static final String SURVEY_API_KEY = "";      // empty = surveys hidden

    // Admin is detected by email after Firebase login. No password stored in the app.
    public static final String ADMIN_EMAIL = "suryak0411@gmail.com";

    // Firestore collection names
    public static final String USERS = "Users";
    public static final String REQUESTS = "WithdrawalRequests";
}
