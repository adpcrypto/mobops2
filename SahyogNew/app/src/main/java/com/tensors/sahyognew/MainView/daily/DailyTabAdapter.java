package com.tensors.sahyognew.MainView.daily;



import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class DailyTabAdapter extends FragmentPagerAdapter {

    private DailyFragment myContext;
    int totalTabs;

    public DailyTabAdapter(DailyFragment context, FragmentManager fm, int totalTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AttendanceFragment();
            case 1:
                return new ActivitiesFragment();

            default:return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}