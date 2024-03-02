package com.reward.cashbazar.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import com.reward.cashbazar.fragments.All_Quiz_Detail_Fragment;
import com.reward.cashbazar.fragments.My_Quiz_Detail_Fragment;

public class Quiz_Detail_Tab_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private My_Quiz_Detail_Fragment fragmentMyQuiz;
    private All_Quiz_Detail_Fragment fragmentAllQuiz;

    public Quiz_Detail_Tab_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentMyQuiz = new My_Quiz_Detail_Fragment();
        fragmentAllQuiz = new All_Quiz_Detail_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentMyQuiz;
        } else if (i == 1) {
            return fragmentAllQuiz;
        }
        return null;
    }

    public My_Quiz_Detail_Fragment getMyQuizHistoryFragment() {
        return fragmentMyQuiz;
    }

    public All_Quiz_Detail_Fragment getAllQuizHistoryFragment() {
        return fragmentAllQuiz;
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
