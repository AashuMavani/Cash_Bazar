package com.reward.cashbazar.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import com.reward.cashbazar.fragments.AllLucky_Number_ContestDetail_Fragment;
import com.reward.cashbazar.fragments.My_LuckyNumber_ContestDetail_Fragment;

public class Lucky_Number_Consent_History_Tab_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private My_LuckyNumber_ContestDetail_Fragment fragmentMyContest;
    private AllLucky_Number_ContestDetail_Fragment fragmentAllContest;

    public Lucky_Number_Consent_History_Tab_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentMyContest = new My_LuckyNumber_ContestDetail_Fragment();
        fragmentAllContest = new AllLucky_Number_ContestDetail_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentMyContest;
        } else if (i == 1) {
            return fragmentAllContest;
        }
        return null;
    }

    public My_LuckyNumber_ContestDetail_Fragment getMyContestHistoryFragment() {
        return fragmentMyContest;
    }

    public AllLucky_Number_ContestDetail_Fragment getAllContestHistoryFragment() {
        return fragmentAllContest;
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
