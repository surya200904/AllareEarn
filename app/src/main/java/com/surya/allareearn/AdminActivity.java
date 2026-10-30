package com.surya.allareearn;

import android.os.Bundle;
import android.widget.*;
import com.google.firebase.firestore.*;

// f) ADMIN: only suryak0411@gmail.com reaches this screen. Lists users + requests, mark Paid.
public class AdminActivity extends BaseActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        // Double-check: non-admins are kicked out.
        if (!AppConfig.ADMIN_EMAIL.equalsIgnoreCase(auth.getCurrentUser().getEmail())) { toast("Admin only"); finish(); return; }
        setContentView(R.layout.activity_admin);
        ListView list = findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        // Users list
        db.collection(AppConfig.USERS).addSnapshotListener(this, (snap, e) -> {
            if (e != null) { toast("Load failed"); return; }
            adapter.clear();
            if (snap != null) for (DocumentSnapshot d : snap.getDocuments())
                adapter.add(d.getString("FullName") + " | " + d.getString("Email") + " | " + d.getString("Phone")
                    + " | UPI: " + d.getString("UpiId")
                    + " | Total Rs " + (or0(d.getLong("TotalEarnPaise")) / 100.0)
                    + " | Today Rs " + (or0(d.getLong("TodayEarnPaise")) / 100.0)
                    + " | Yest Rs " + (or0(d.getLong("YesterdayEarnPaise")) / 100.0)
                    + " | Month Rs " + (or0(d.getLong("MonthlyEarnPaise")) / 100.0));
            adapter.notifyDataSetChanged();
        });
        // Tap a withdrawal request to mark it Paid.
        ListView reqs = findViewById(R.id.requests);
        ArrayAdapter<String> rAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        reqs.setAdapter(rAdapter);
        db.collection(AppConfig.REQUESTS).whereEqualTo("Status", "Pending").addSnapshotListener(this, (snap, e) -> {
            if (e != null) return;
            rAdapter.clear();
            java.util.List<String> ids = new java.util.ArrayList<>();
            if (snap != null) for (DocumentSnapshot d : snap.getDocuments()) {
                ids.add(d.getId());
                rAdapter.add(d.getString("Rupees") + " Rs to UPI " + d.getString("UpiId") + " | " + d.getString("Status"));
            }
            rAdapter.notifyDataSetChanged();
            reqs.setOnItemClickListener((a, v, pos, id) -> {
                String rid = ids.get(pos);
                db.collection(AppConfig.REQUESTS).document(rid).update("Status", "Paid").addOnSuccessListener(x -> toast("Marked Paid"));
            });
        });
    }
}
