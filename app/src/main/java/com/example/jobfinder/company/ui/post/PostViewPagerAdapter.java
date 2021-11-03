package com.example.jobfinder.company.ui.post;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.jobfinder.company.ui.post.applicants.Applicants;
import com.example.jobfinder.company.ui.post.post_jobs.PostJob;

public class PostViewPagerAdapter extends FragmentStatePagerAdapter {

    Integer numberOfTabs;

    public PostViewPagerAdapter(FragmentManager fragmentManager , Integer numberOfTabs)
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
                return new PostJob();
            case 1:
                return new Applicants();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
