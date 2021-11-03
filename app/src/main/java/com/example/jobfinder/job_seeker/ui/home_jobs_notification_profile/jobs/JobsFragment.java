package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.jobfinder.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class JobsFragment extends Fragment {




    TabLayout tabLayout;
    TabItem tabAppliedFor , tabSavedJobs;
    ViewPager jobsViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        tabLayout = view.findViewById(R.id.jobs_tab_layout);
        tabAppliedFor = view.findViewById(R.id.applied_for_button_id);
        tabSavedJobs = view.findViewById(R.id.saved_jobs_button_id);
        jobsViewPager = view.findViewById(R.id.jobs_viewpager);

        JobsViewPagerAdapter jobsViewPagerAdapter =
                new JobsViewPagerAdapter((((AppCompatActivity)getActivity()).getSupportFragmentManager()),tabLayout.getTabCount());

        jobsViewPager.setAdapter(jobsViewPagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                jobsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    public void gotoApplicants()
    {
        jobsViewPager.setCurrentItem(1);
    }

}