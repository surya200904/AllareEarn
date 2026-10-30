package com.surya.allareearn;

import android.os.Bundle;
import android.widget.*;

// PROFILE: shows saved details + full earnings breakdown.
public class ProfileActivity extends BaseActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_profile);
        TextView info = findViewById(R.id.info);
        db.collection(AppConfig.USERS).document(uid()).get().addOnSuccessListener(s -> {
            if (!s.exists()) { info.setText("No profile data"); return; }
            info.setText(
                "Name: " + s.getString("FullName") + "\n" +
                "Email: " + s.getString("Email") + "\n" +
                "Phone: " + s.getString("Phone") + "\n" +
                "UPI: " + s.getString("UpiId") + "\n\n" +
                "Total Earn: Rs " + (or0(s.getLong("TotalEarnPaise")) / 100.0) + "\n" +
                "Today: Rs " + (or0(s.getLong("TodayEarnPaise")) / 100.0) + "\n" +
                "Yesterday: Rs " + (or0(s.getLong("YesterdayEarnPaise")) / 100.0) + "\n" +
                "This month: Rs " + (or0(s.getLong("MonthlyEarnPaise")) / 100.0) + "\n\n" +
                "Balance: Rs " + (or0(s.getLong("PointsPaise")) / 100.0));
        });
        findViewById(R.id.logout).setOnClickListener(v -> { auth.signOut(); finishAffinity(); });
    }
}
