package com.ren.dianav2.screens;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ren.dianav2.R;
import com.ren.dianav2.fragments.ChatFragment;
import com.ren.dianav2.fragments.HomeFragment;
import com.ren.dianav2.fragments.ProfileFragment;

public class MainScreen extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom - navigationBarHeight);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString("current_fragment_tag");
            replaceFragment(getSupportFragmentManager().findFragmentByTag(currentFragmentTag)
                    , currentFragmentTag);
        } else {
            replaceFragment(new HomeFragment(), "home");
        }

        onClickItemBottomNavigation(bottomNavigationView);
    }

    private void onClickItemBottomNavigation(BottomNavigationView navigationView) {
        navigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), "home");
            } else if (item.getItemId() == R.id.new_chat) {
                replaceFragment(new ChatFragment(), "chat");
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment(), "profile");
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        currentFragmentTag = tag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_fragment_tag", currentFragmentTag);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}