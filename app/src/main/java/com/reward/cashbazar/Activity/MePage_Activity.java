/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Get_User_Profile_Async;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.User_History;
import com.reward.cashbazar.Async.Models.User_Profile_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import de.hdodenhof.circleimageview.CircleImageView;

public class MePage_Activity extends AppCompatActivity {
    private View  viewShine, viewShineLogin, aboutSep;
    private CircleImageView ivProfilePic;
    private TextView tvName, tvEmail, tvRupees, tvPoints, tvTotalPoints, tvExpendPoints;
    private Response_Model responseMain;
    private User_History userDetails;
    private boolean isResumeCalled = false;
    private ImageView ivEdit;
    private AppCompatButton btnWithdraw, btnLogin;
    private LinearLayout layoutWalletWithdraw,lnr_balance, layoutWallet, layoutInvite, layoutMakeMoney, layoutTask, layoutFAQ, layoutFeedback, layoutAboutUs, layoutLogout, layoutLogin, layoutDeleteAccount,referlayout;
    private RelativeLayout layoutProfile;

    private Animation animUpDown;
    private View viewlogout,viewpoint,viewdeleteaccount;
    private ImageView iv_left, ivHistory;
    private LinearLayout layoutPoints1;

    public MePage_Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(MePage_Activity.this);
        setContentView(R.layout.activity_me_page);
        initView();
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

    }

    private void initView() {
        try {
            layoutWallet = findViewById(R.id.layoutWallet);
            lnr_balance = findViewById(R.id.lnr_balance);
            layoutWalletWithdraw = findViewById(R.id.layoutWalletWithdraw);
            viewShineLogin = findViewById(R.id.viewShineLogin);
            viewShine = findViewById(R.id.viewShine);
            layoutProfile = findViewById(R.id.layoutProfile);
            layoutLogin = findViewById(R.id.layoutLogin);
            ivProfilePic = findViewById(R.id.ivProfilePic);
            tvName = findViewById(R.id.tvName);
            tvEmail = findViewById(R.id.tvEmail);
            viewlogout = findViewById(R.id.viewlogout);
            viewdeleteaccount = findViewById(R.id.viewdeleteaccount);
            viewpoint = findViewById(R.id.viewpoint);
            iv_left = findViewById(R.id.iv_left);
            referlayout = findViewById(R.id.referlayout);


            iv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });



           /* layoutPoints1 = findViewById(R.id.layoutPoints1);

            layoutPoints1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        startActivity(new Intent(MePage_Activity.this, MyWalletActivity.class));
                    } else {
                        Common_Utils.NotifyLogin(MePage_Activity.this);
                    }
                }
            });*/

//              tvRupees = findViewById(R.id.tvRupees);
//            tvPoints = findViewById(R.id.tvPoints);
//            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

//            tvRupees.setText("₹ 1");
            tvTotalPoints = findViewById(R.id.tvTotalPoints);
            tvExpendPoints = findViewById(R.id.tvExpendPoints);

            layoutInvite = findViewById(R.id.layoutInvite);
            layoutInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MePage_Activity.this, ReferUserActivity.class);
                    startActivity(intent);
//                    ( MePage_Activity.this).performInviteClick();
                }
            });

            layoutMakeMoney = findViewById(R.id.layoutMakeMoney);
            layoutMakeMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MePage_Activity.this, RewardPage_Activity.class);
                    startActivity(intent);
//                    ((MainActivity) MePage_Activity.this).performRewardClick();
                }
            });

          /*  layoutTask = findViewById(R.id.layoutTask);
            layoutTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MePage_Activity.this,TaskPage_Activity.class);
                    startActivity(intent);

//                    ((MainActivity) MePage_Activity.this).performTaskClick();
                }
            });*/

            layoutFAQ = findViewById(R.id.layoutFAQ);
            layoutFAQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MePage_Activity.this, FAQActivity.class));
                }
            });

            layoutFeedback = findViewById(R.id.layoutFeedback);
            layoutFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MePage_Activity.this, SubmitFeedbackActivity.class).putExtra("title", "Give Feedback"));
                }
            });

            layoutAboutUs = findViewById(R.id.layoutAboutUs);
            layoutAboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent i = new Intent(MePage_Activity.this, WebActivity.class);
                        i.putExtra("URL", responseMain.getAboutUsUrl());
                        i.putExtra("Title", "About Us");
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnWithdraw = findViewById(R.id.btnWithdraw);
            btnWithdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MePage_Activity.this, ActivityWithdrawTypes.class));
                }
            });

            new Get_User_Profile_Async(MePage_Activity.this);

            btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivity(new Intent(MePage_Activity.this, UserLoginActivity.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            layoutLogout = findViewById(R.id.layoutLogout);
            layoutLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Common_Utils.NotifyLogout(MePage_Activity.this, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            layoutDeleteAccount = findViewById(R.id.layoutDeleteAccount);
            layoutDeleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Common_Utils.NotifyDeleteAccount(MePage_Activity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            animUpDown = AnimationUtils.loadAnimation(MePage_Activity.this, R.anim.left_right);
            animUpDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        viewShine.startAnimation(animUpDown);

                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            viewShine.startAnimation(animUpDown);

            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                layoutWallet.setVisibility(View.VISIBLE);
                layoutProfile.setVisibility(View.VISIBLE);
                layoutLogout.setVisibility(View.VISIBLE);
                viewlogout.setVisibility(View.VISIBLE);
                viewdeleteaccount.setVisibility(View.VISIBLE);
                referlayout.setVisibility(View.VISIBLE);
                viewpoint.setVisibility(View.VISIBLE);
                layoutLogin.setVisibility(View.GONE);
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAccountDeleteOption()) && responseMain.getIsShowAccountDeleteOption().equals("1")) {
                    layoutDeleteAccount.setVisibility(View.VISIBLE);
                }
            } else {
                layoutProfile.setVisibility(View.GONE);
                layoutWallet.setVisibility(View.GONE);
                layoutWalletWithdraw.setVisibility(View.GONE);
                lnr_balance.setVisibility(View.GONE);
                layoutLogout.setVisibility(View.GONE);
                viewlogout.setVisibility(View.GONE);
                viewdeleteaccount.setVisibility(View.GONE);
                referlayout.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
                viewpoint.setVisibility(View.GONE);
                layoutDeleteAccount.setVisibility(View.GONE);
                viewShineLogin.startAnimation(animUpDown);
            }
//            tvPoints.setText(responseMain.getPointValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User_Profile_Model responseModel;
    public void setData(User_Profile_Model responseModel1) {
        responseModel = responseModel1;

        userDetails = responseModel1.getUserDetails();

        try {
            // Load home note webview top
            try {
                if (responseModel != null && !Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(View.VISIBLE);
                    webNote.loadDataWithBaseURL(null, responseModel.getHomeNote(), "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (responseModel != null && responseModel.getTopAds() != null && !Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    Common_Utils.loadTopBannerAd(MePage_Activity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvTotalPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            tvEmail.setText(userDetails.getEmailId());
            tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
            if (userDetails.getProfileImage() != null) {
                Glide.with(MePage_Activity.this).load(userDetails.getProfileImage()).override(getResources().getDimensionPixelSize(R.dimen.dim_80), getResources().getDimensionPixelSize(R.dimen.dim_80)).into(ivProfilePic);
            }
            tvExpendPoints.setText(userDetails.getWithdrawPoint());
            ivEdit.setVisibility(View.VISIBLE);
            // start the animation
            viewShine.startAnimation(animUpDown);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUserPoints();
    }

    private void updateUserPoints() {
        try {
            userDetails = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.User_Details), User_History.class);
            tvTotalPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            tvEmail.setText(userDetails.getEmailId());
            tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
//            tvExpendPoints.setText(userDetails.getWithdrawPoint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* @Override
    public void onBackPressed() {
        try {
            if (responseMain != null && responseMain.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(MePage_Activity.this, responseMain.getAppLuck());
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
}

