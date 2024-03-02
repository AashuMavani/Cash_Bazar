package com.reward.cashbazar.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.reward.cashbazar.fragments.ActiveMilestonesFragment;
import com.reward.cashbazar.fragments.FinishedMilestonesFragment;

import java.util.ArrayList;

public class MilestonesTabAdapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private ActiveMilestonesFragment fragmentActive;
    private FinishedMilestonesFragment fragmentCompleted;

    public MilestonesTabAdapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentActive = new ActiveMilestonesFragment();
        fragmentCompleted = new FinishedMilestonesFragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentActive;
        } else if (i == 1) {
            return   fragmentCompleted;
        }
        return null;
    }

    public ActiveMilestonesFragment getActiveMilestonesFragment() {
        return fragmentActive;
    }

    public FinishedMilestonesFragment getCompletedMilestonesFragment() {
        return fragmentCompleted;
    }

    @Override
    public int getCount() {
        return mFragmentItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentItems.get(position);
    }
}
