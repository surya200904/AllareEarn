package com.surya.allareearn;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

// Common helpers every screen shares.
public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth auth = FirebaseAuth.getInstance();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected String uid() { return auth.getCurrentUser() == null ? "" : auth.getCurrentUser().getUid(); }
    protected void toast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }
}
