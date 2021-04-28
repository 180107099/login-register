package com.example.instagram1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagram1.fragments.AddFragment;
import com.example.instagram1.fragments.HeartFragment;
import com.example.instagram1.fragments.HomeFragment;
import com.example.instagram1.fragments.ProfileFragment;
import com.example.instagram1.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                    }
                    switch (item.getItemId()){
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                    }
                    switch (item.getItemId()){
                        case R.id.nav_add:
                            selectedFragment = new AddFragment();
                            break;
                    }
                    switch (item.getItemId()){
                        case R.id.nav_heart:
                            selectedFragment = new HeartFragment()
                            ;
                            break;
                    }
                    switch (item.getItemId()){
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment()
                            ;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, selectedFragment).commit();
                    return true;
                }


            };

    @Override
    public void onBackPressed() {
        if(bottomNav.getSelectedItemId()==R.id.nav_home){
            super.onBackPressed();
            finish();
        }
        else{

        }

    }
}