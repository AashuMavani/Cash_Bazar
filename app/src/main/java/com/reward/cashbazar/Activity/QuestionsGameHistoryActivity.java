package com.reward.cashbazar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import com.reward.cashbazar.Adapter.Quiz_Detail_Tab_Adapter;
import com.reward.cashbazar.Async.Models.Point_History_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class QuestionsGameHistoryActivity extends AppCompatActivity {
    private TextView tvPoints;
    private TabLayout tabLayout;
    private ArrayList<String> mFragmentItems;
    private ViewPager viewpager;
    private Quiz_Detail_Tab_Adapter customTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(QuestionsGameHistoryActivity.this);
        setContentView(R.layout.activity_quiz_game_detail__history);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewpager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }
    private void setupViewPager(ViewPager viewPager) {
        mFragmentItems = new ArrayList<>();
        mFragmentItems.add("My Quiz");
        mFragmentItems.add("All Quiz");

        customTabAdapter = new Quiz_Detail_Tab_Adapter(getSupportFragmentManager(), mFragmentItems);
        viewPager.setAdapter(customTabAdapter);
        viewPager.setOffscreenPageLimit(1);
        customTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setData(String type, Point_History_Model responseModel) {
        if (type.equals(POC_Constants.HistoryType.LUCKY_NUMBER_MY_CONTEST)) {
            customTabAdapter.getMyQuizHistoryFragment().setData(responseModel);
        } else {
            customTabAdapter.getAllQuizHistoryFragment().setData(responseModel);
        }
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
    }
}