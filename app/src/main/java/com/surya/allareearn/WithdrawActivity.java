package com.surya.allareearn;

import android.os.Bundle;
import android.widget.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

// e) WITHDRAWAL: pick UPI, enter points, min Rs 50 = 500 points. Request saved as Pending.
public class WithdrawActivity extends BaseActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_withdraw);
        EditText points = findViewById(R.id.points), upi = findViewById(R.id.upi);
        findViewById(R.id.submit).setOnClickListener(v -> {
            long pts;
            try { pts = Long.parseLong(points.getText().toString().trim()); }
            catch (Exception ex) { toast("Enter points to withdraw"); return; }
            String pay = upi.getText().toString().trim();
            if (pay.isEmpty()) { toast("Enter your UPI ID"); return; }
            long paise = pts * AppConfig.PAISE_PER_POINT;
            if (paise < AppConfig.MIN_WITHDRAW_PAISE) { toast("Minimum withdrawal is 500 points (Rs 50)"); return; }
            DocumentReference user = db.collection(AppConfig.USERS).document(uid());
            db.runTransaction(t -> {
                long bal = or0(t.get(user).getLong("PointsPaise"));
                if (bal < paise) throw new RuntimeException("Not enough points");
                t.update(user, "PointsPaise", bal - paise); // hold the points immediately
                Map<String, Object> req = new HashMap<>();
                req.put("UserId", uid()); req.put("Points", pts); req.put("Rupees", pts); // 1 point = Rs 1
                req.put("UpiId", pay); req.put("Status", "Pending"); req.put("CreatedAt", FieldValue.serverTimestamp());
                t.set(db.collection(AppConfig.REQUESTS).document(), req);
                return null;
            }).addOnSuccessListener(x -> { toast("Request submitted. Payment is not automatic."); finish(); })
              .addOnFailureListener(e -> toast("Failed: " + e.getMessage()));
        });
    }
}
