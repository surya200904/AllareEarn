package com.surya.allareearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// a) SIGNUP: name + email + password + phone + UPI, validated, saved to Firestore.
public class SignupActivity extends BaseActivity {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$");

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_signup);
        EditText name = findViewById(R.id.name), email = findViewById(R.id.email),
                pass = findViewById(R.id.password), phone = findViewById(R.id.phone), upi = findViewById(R.id.upi);
        findViewById(R.id.signup).setOnClickListener(v -> {
            String n = name.getText().toString().trim(), e = email.getText().toString().trim(),
                    p = pass.getText().toString().trim(), ph = phone.getText().toString().trim(), u = upi.getText().toString().trim();
            // Validation: no empty fields, valid email, 6+ char password
            if (n.isEmpty() || e.isEmpty() || p.isEmpty() || ph.isEmpty() || u.isEmpty()) { toast("Fill all fields"); return; }
            if (!EMAIL.matcher(e).matches()) { toast("Invalid email format"); return; }
            if (p.length() < 6) { toast("Password: 6+ characters"); return; }
            auth.createUserWithEmailAndPassword(e, p).addOnSuccessListener(r -> {
                Map<String, Object> user = new HashMap<>();
                user.put("FullName", n); user.put("Email", e); user.put("Phone", ph); user.put("UpiId", u);
                user.put("PointsPaise", 0L);   // balance in paise
                user.put("TotalEarnPaise", 0L); user.put("TodayEarnPaise", 0L);
                user.put("YesterdayEarnPaise", 0L); user.put("MonthlyEarnPaise", 0L);
                user.put("LastEarnDate", ""); user.put("LastEarnMonth", "");
                db.collection(AppConfig.USERS).document(uid()).set(user)
                  .addOnSuccessListener(x -> { toast("Account created"); startActivity(new Intent(this, HomeActivity.class)); finish(); })
                  .addOnFailureListener(x -> toast("Save failed: " + x.getMessage()));
            }).addOnFailureListener(r -> toast("Signup failed: " + r.getMessage()));
        });
        findViewById(R.id.toLogin).setOnClickListener(v -> finish());
    }
}
