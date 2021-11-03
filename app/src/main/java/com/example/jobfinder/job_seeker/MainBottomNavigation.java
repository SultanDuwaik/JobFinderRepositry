package com.example.jobfinder.job_seeker;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.jobfinder.R;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.HomeFragment;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.JobsFragment;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainBottomNavigation extends AppCompatActivity {

    public static Activity mainBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_navigation);

        mainBottomNavigation = this;

        Bundle extra = getIntent().getExtras();
        if(extra != null && extra.containsKey("open profile"))
        {
            Boolean openProfile = extra.getBoolean("open profile");
            if(openProfile)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment , new ProfileFragment()).commit();

            }
        }

        BottomNavigationView navView = findViewById(R.id.main_bottom_navigation_bar);

        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }
}