package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Reward_Screen_Model;
import com.reward.cashbazar.Async.Models.User_History;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class MyWalletActivity extends AppCompatActivity {
    private AppCompatButton btnWithdraw;
    private View viewShine;
    private User_History userDetails;
    private TextView lblLoadingAds, tvWalletPoints, tvWalletRupees, tvPoints, tvRupees;
    private Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private Reward_Screen_Model responseModel;
    private LinearLayout layoutAds, layoutPointHistory, layoutWithdrawalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(MyWalletActivity.this);
        setContentView(R.layout.activity_my_wallet);

        userDetails = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.User_Details), User_History.class);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        viewShine = findViewById(R.id.viewShine);

        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSpin = new Intent(MyWalletActivity.this, ActivityWithdrawTypes.class);
                startActivity(inSpin);
            }
        });

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        layoutAds = findViewById(R.id.layoutAds);

        layoutPointHistory = findViewById(R.id.layoutPointHistory);
        layoutPointHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(MyWalletActivity.this, PointDetailsActivity.class));
                } else {
                    Common_Utils.NotifyLogin(MyWalletActivity.this);
                }
            }
        });


        layoutWithdrawalHistory = findViewById(R.id.layoutWithdrawalHistory);
        layoutWithdrawalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    Intent intent = new Intent(MyWalletActivity.this, PointDetailsActivity.class);
                    intent.putExtra("type", POC_Constants.HistoryType.WITHDRAW_HISTORY);
                    intent.putExtra("title", "Withdrawal History");
                    startActivity(intent);
                } else {
                    Common_Utils.NotifyLogin(MyWalletActivity.this);
                }
            }
        });

        tvWalletPoints = findViewById(R.id.tvWalletPoints);
        tvWalletRupees = findViewById(R.id.tvWalletRupees);
        tvPoints = findViewById(R.id.tvPoints);
        tvRupees = findViewById(R.id.tvRupees);
        Animation animUpDown = AnimationUtils.loadAnimation(MyWalletActivity.this, R.anim.left_right);
        animUpDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewShine.startAnimation(animUpDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // start the animation
        viewShine.startAnimation(animUpDown);
    }

    protected void onResume() {
        super.onResume();
        try {
            tvWalletPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            tvRupees.setText("₹ 1");
            if (responseMain.getPointValue() != null) {
                tvPoints.setText(responseMain.getPointValue() + " Bucks");
                tvWalletRupees.setText(Common_Utils.convertPointsInINR(POC_SharePrefs.getInstance().getEarningPointString(), responseMain.getPointValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), MyWalletActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutNativeAd.addView(nativeAdView);
                    layoutAds.setVisibility(View.VISIBLE);
                    lblLoadingAds.setVisibility(View.GONE);
                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setData(Reward_Screen_Model responseModel1){
//        responseModel = responseModel1;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}