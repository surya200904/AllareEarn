package com.surya.allareearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

// b) LOGIN: email + password. Firebase keeps the session, so next open goes straight home.
public class LoginActivity extends BaseActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        // Session persistence: already logged in? skip login screen.
        if (auth.getCurrentUser() != null) { startActivity(new Intent(this, HomeActivity.class)); finish(); return; }
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.email), pass = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(v -> {
            auth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString())
                .addOnSuccessListener(r -> { startActivity(new Intent(this, HomeActivity.class)); finish(); })
                .addOnFailureListener(r -> toast("Login failed: " + r.getMessage()));
        });
        findViewById(R.id.toSignup).setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }
}
