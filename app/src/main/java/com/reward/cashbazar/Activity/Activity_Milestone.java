/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reward.cashbazar.Adapter.MilestonesTabAdapter;
import com.reward.cashbazar.Async.Models.MilestonesResponseModel;
import com.reward.cashbazar.Async.Models.Point_History_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.ArrayList;

public class Activity_Milestone extends AppCompatActivity {
    private TextView tvPoints;
    private TabLayout tabLayout;
    private ArrayList<String> mFragmentItems;
    private ViewPager viewpager;
    private MilestonesTabAdapter customTabAdapter;
    private ImageView ivHelp;
    private String helpUrl = "";
    private LinearLayout layoutPoints;
    private RelativeLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Activity_Milestone.this);
        setContentView(R.layout.activity_milestone);

        layoutMain = findViewById(R.id.layoutMain);
        layoutPoints = findViewById(R.id.layoutPoints);

        tvPoints = findViewById(R.id.tvPoints);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivHelp = findViewById(R.id.ivHelp);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common_Utils.isStringNullOrEmpty(helpUrl)) {
                    Common_Utils.openUrl(Activity_Milestone.this, helpUrl);
                }
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Activity_Milestone.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Activity_Milestone.this);
                }
            }
        });

        viewpager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);




    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentItems = new ArrayList<>();
        mFragmentItems.add("Active");
        mFragmentItems.add("Completed");

        customTabAdapter = new MilestonesTabAdapter(getSupportFragmentManager(), mFragmentItems);
        viewPager.setAdapter(customTabAdapter);
        viewPager.setOffscreenPageLimit(1);
        customTabAdapter.notifyDataSetChanged();
    }

/*    @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(Activity_Milestone.this, responseModel.getAppLuck());
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private MilestonesResponseModel responseModel;
    public void setActiveMilestonesData(MilestonesResponseModel responseModel1) {
        responseModel = responseModel1;
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        helpUrl = responseModel.getHelpVideoUrl();
        ivHelp.setVisibility(Common_Utils.isStringNullOrEmpty(helpUrl) ? View.GONE : View.VISIBLE);
        customTabAdapter.getActiveMilestonesFragment().setData(responseModel);
    }

    public void setCompletedMilestonesData(Point_History_Model responseModel) {
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        customTabAdapter.getCompletedMilestonesFragment().setData(responseModel);
    }

    public void changeMilestoneValues(MilestonesResponseModel responseModel) {
        customTabAdapter.getActiveMilestonesFragment().changeMilestoneValues(responseModel);
    }

    public void updateBalance() {
        Common_Utils.GetCoinAnimation(Activity_Milestone.this, layoutMain, layoutPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
    }

    public void updateHistoryScreen() {
        customTabAdapter.getCompletedMilestonesFragment().clearDataAndRefreshData();
    }
}