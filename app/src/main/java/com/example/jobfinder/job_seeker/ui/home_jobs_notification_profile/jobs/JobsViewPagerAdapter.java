package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.applied_for.AppliedFor;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.saved_jobs.SavedJobs;

public class JobsViewPagerAdapter extends FragmentStatePagerAdapter {

    private Integer numberOfTabs;

    public JobsViewPagerAdapter(FragmentManager fragmentManager , Integer numberOfTabs)
    {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0 :
                return new SavedJobs();
            case 1:
                return new AppliedFor();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numberOfTabs;
    }
}