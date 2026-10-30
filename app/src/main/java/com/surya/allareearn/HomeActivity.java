package com.surya.allareearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

// c) + d) HOME: watch unlimited rewarded ads (+0.1 pts each) and see earnings summary.
public class HomeActivity extends BaseActivity {
    private RewardedAd rewarded;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);
        loadAd();
        findViewById(R.id.watchAd).setOnClickListener(v -> showAd());
        findViewById(R.id.surveys).setOnClickListener(v -> startActivity(new Intent(this, SurveyActivity.class)));
        findViewById(R.id.withdraw).setOnClickListener(v -> startActivity(new Intent(this, WithdrawActivity.class)));
        findViewById(R.id.profile).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        if (AppConfig.ADMIN_EMAIL.equalsIgnoreCase(auth.getCurrentUser().getEmail()))
            findViewById(R.id.admin).setOnClickListener(v -> startActivity(new Intent(this, AdminActivity.class)));
        else ((TextView) findViewById(R.id.admin)).setVisibility(android.view.View.GONE);
        refreshBalance();
    }

    private void loadAd() {
        RewardedAd.load(this, AppConfig.REWARDED_AD_UNIT,
                new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override public void onAdLoaded(RewardedAd ad) { rewarded = ad; }
            @Override public void onAdFailedToLoad(int err) { rewarded = null; }
        });
    }

    private void showAd() {
        if (rewarded == null) { toast("Ad not ready, try again in a few seconds"); loadAd(); return; }
        rewarded.show(this, rewardItem -> {
            // Only this callback runs when the ad is FULLY watched.
            addEarnings(AppConfig.PAISE_REWARD_PER_AD, "ad reward");
            loadAd(); // preload the next one, unlimited watching
        });
    }

    // Adds paise to Total/Today/Monthly and handles daily/monthly resets (d).
    protected void addEarnings(long paise, String note) {
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date());
        String month = today.substring(0, 7);
        db.runTransaction(t -> {
            var ref = db.collection(AppConfig.USERS).document(uid());
            var snap = t.get(ref);
            long todayP = or0(snap.getLong("TodayEarnPaise")), totP = or0(snap.getLong("TotalEarnPaise"));
            long yestP = or0(snap.getLong("YesterdayEarnPaise")), monP = or0(snap.getLong("MonthlyEarnPaise"));
            String lastD = snap.getString("LastEarnDate") == null ? "" : snap.getString("LastEarnDate");
            String lastM = snap.getString("LastEarnMonth") == null ? "" : snap.getString("LastEarnMonth");
            if (!lastD.equals(today)) { yestP = todayP; todayP = 0; lastD = today; } // day rolled over
            if (!lastM.equals(month)) { monP = 0; lastM = month; }                  // month rolled over
            todayP += paise; totP += paise; monP += paise;
            t.update(ref, "TodayEarnPaise", todayP, "TotalEarnPaise", totP, "YesterdayEarnPaise", yestP,
                    "MonthlyEarnPaise", monP, "LastEarnDate", lastD, "LastEarnMonth", lastM, "PointsPaise", totP);
            return null;
        }).addOnSuccessListener(x -> { toast("+" + (paise / 100.0) + " pts"); refreshBalance(); })
          .addOnFailureListener(e -> toast("Save failed: " + e.getMessage()));
    }

    protected long or0(Long v) { return v == null ? 0 : v; }

    protected void refreshBalance() {
        db.collection(AppConfig.USERS).document(uid()).get().addOnSuccessListener(s ->
            ((TextView) findViewById(R.id.balance)).setText(
                "Total: Rs " + (or0(s.getLong("TotalEarnPaise")) / 100.0) + "\n" +
                "Today: Rs " + (or0(s.getLong("TodayEarnPaise")) / 100.0) + "\n" +
                "Yesterday: Rs " + (or0(s.getLong("YesterdayEarnPaise")) / 100.0) + "\n" +
                "This month: Rs " + (or0(s.getLong("MonthlyEarnPaise")) / 100.0)));
    }
}
