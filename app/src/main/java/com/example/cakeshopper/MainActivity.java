package com.example.cakeshopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    long backPressed_time;
    long Period = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (getIntent().getBooleanExtra("LOGOUT", false)) {
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "Click Once Again to exit", Toast.LENGTH_SHORT).show();
//        if (backPressed_time + Period > System.currentTimeMillis()) {
//            super.onBackPressed();
//        }
//        backPressed_time = System.currentTimeMillis();
//    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.Home) {
            selectedFragment = new Home();
        } else if (itemId == R.id.Cakes) {
            selectedFragment = new Cakes();
        } else if (itemId == R.id.ManageAccount) {
            selectedFragment = new ManageAccount();
        } else if (itemId == R.id.AboutStore) {
            selectedFragment = new AboutShop();
        } else if (itemId == R.id.PreviousOrders) {
            selectedFragment = new YourOrders();
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };
}
