package com.reward.cashbazar.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import com.reward.cashbazar.fragments.Refer_Point_Detail_Fragment;
import com.reward.cashbazar.fragments.Invite_User_Detail_Fragment;

public class Refer_Detail_History_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private Refer_Point_Detail_Fragment fragmentPointHistory;
    private Invite_User_Detail_Fragment fragmentUserHistory;

    public Refer_Detail_History_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentPointHistory = new Refer_Point_Detail_Fragment();
        fragmentUserHistory = new Invite_User_Detail_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentPointHistory;
        } else if (i == 1) {
            return fragmentUserHistory;
        }
        return null;
    }

    public Refer_Point_Detail_Fragment getReferPointHistoryFragment() {
        return fragmentPointHistory;
    }

    public Invite_User_Detail_Fragment getReferUserHistoryFragment() {
        return fragmentUserHistory;
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
