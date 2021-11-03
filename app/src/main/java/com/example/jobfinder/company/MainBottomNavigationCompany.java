package com.example.jobfinder.company;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.jobfinder.R;
import com.example.jobfinder.company.ui.post.PostFragmentCompany;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.JobsFragment;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


    public class MainBottomNavigationCompany extends AppCompatActivity {
    public static Activity mainBottomNavigationCompany;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_navigation_company);

        mainBottomNavigationCompany = this;

        BottomNavigationView navView = findViewById(R.id.main_bottom_navigation_bar_company);




        NavController navController = Navigation.findNavController(this, R.id.fragment_company);
        NavigationUI.setupWithNavController(navView, navController);

    }

}