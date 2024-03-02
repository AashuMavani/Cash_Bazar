package com.reward.cashbazar.Activity;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.EveryDayRewardAdapter;
import com.reward.cashbazar.Async.Get_EveryDay_Reward_Async;
import com.reward.cashbazar.Async.Models.EveryDay_Reward_Data_Item;
import com.reward.cashbazar.Async.Models.EveryDay_Reward_Response_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Story_Daily_Reward_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class ActivityEveryDayReward extends AppCompatActivity {

    private RecyclerView rvList;
    private List<EveryDay_Reward_Data_Item> list = new ArrayList<>();
    private TextView lblLoadingAds, tvPoints, tvNote, tvTimer;
    private LottieAnimationView ivLottieNoData;
    private Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds, layoutData, layoutPoints, layoutTimer;
    private ImageView ivHistory;
    private AppCompatButton btnClaim;
    private RelativeLayout layoutMain;
    private EveryDay_Reward_Response_Model objDailyReward;
    private String todayDate, endDate;
    private CountDownTimer timer;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(ActivityEveryDayReward.this);
        setContentView(R.layout.activity_daily_reward_challenge);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        rvList = findViewById(R.id.rvList);
        rvList.setAdapter(new EveryDayRewardAdapter(list, ActivityEveryDayReward.this, new EveryDayRewardAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    if (list.get(position).getIsCompleted().equals("0")) {
                        finish();
                        Common_Utils.Redirect(ActivityEveryDayReward.this, list.get(position).getScreenNo(), list.get(position).getTitle(), list.get(position).getUrl(), list.get(position).getId(), list.get(position).getTaskId(), list.get(position).getImage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        layoutMain = findViewById(R.id.layoutMain);

        layoutTimer = findViewById(R.id.layoutTimer);
        tvTimer = findViewById(R.id.tvTimer);

        layoutData = findViewById(R.id.layoutData);
        layoutData.setVisibility(View.INVISIBLE);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);

        tvNote = findViewById(R.id.tvNote);

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityEveryDayReward.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ActivityEveryDayReward.this);
                }
            }
        });

        btnClaim = findViewById(R.id.btnClaim);
        btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClaim.setEnabled(false);
                new Story_Daily_Reward_Async(ActivityEveryDayReward.this);
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //   Common_Utils.startRoundAnimation(ActivityDailyReward.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityEveryDayReward.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.DAILY_REWARD)
                            .putExtra("title", "Daily Reward History"));
                } else {
                    Common_Utils.NotifyLogin(ActivityEveryDayReward.this);
                }
            }
        });

        new Get_EveryDay_Reward_Async(ActivityEveryDayReward.this);

    }

/*    @Override
    public void onBackPressed() {
        try {
            AppLogger.getInstance().d("TAG","gnweklh"+responseModel.getAppLuck());
            if (responseModel != null && responseModel.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(ActivityEveryDayReward.this, responseModel.getAppLuck());
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

    private EveryDay_Reward_Response_Model responseModel;
    public void setData(EveryDay_Reward_Response_Model responseModel1) {
        responseModel = responseModel1;
        if (responseModel != null) {
            objDailyReward = responseModel;
            layoutData.setVisibility(View.VISIBLE);
            tvNote.setText(objDailyReward.getNote());

            if ((responseModel.getData() != null && responseModel.getData().size() > 0) || (responseModel.getData() != null && responseModel.getData().size() > 0)) {
                if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                    Ads_Utils.showAppLovinInterstitialAd(ActivityEveryDayReward.this, null);
                } else if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_REWARD)) {
                    Ads_Utils.showAppLovinRewardedAd(ActivityEveryDayReward.this, null);
                }
                list.addAll(responseModel.getData());
                rvList.getAdapter().notifyDataSetChanged();
                boolean isClaimedAll = true;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIsCompleted().equals("0")) {
                        isClaimedAll = false;
                        break;
                    }
                }


                btnClaim.setEnabled(isClaimedAll);
                if (!isClaimedAll) {
                    if (objDailyReward.getTodayDate() != null) {
                        todayDate = objDailyReward.getTodayDate();
                    }
                    if (objDailyReward.getEndDate() != null) {
                        endDate = objDailyReward.getEndDate();
                    }
                    layoutTimer.setVisibility(VISIBLE);
                    setTimer();
                } else {
                    tvNote.setText("⭐ Wow! You have completed today's daily reward list ⭐");
                    layoutTimer.setVisibility(View.GONE);
                }

                if (!Common_Utils.isStringNullOrEmpty(responseModel.getIsClaimedDailyReward()) && responseModel.getIsClaimedDailyReward().equals("1")) {
                    btnClaim.setText("Claimed!");
                    btnClaim.setEnabled(false);
                }


                // Load home note webview top
                try {
                    if (!Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
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
                    if (responseModel.getTopAds() != null && !Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        Common_Utils.loadTopBannerAd(ActivityEveryDayReward.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                layoutAds = findViewById(R.id.layoutAds);
                layoutAds.setVisibility(View.VISIBLE);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                if (Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds();
                } else {
                    layoutAds.setVisibility(View.GONE);
                }
            }




            rvList.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
            ivLottieNoData.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            if (list.isEmpty())
                ivLottieNoData.playAnimation();
            // Load Bottom banner ad
            try {
                if (!list.isEmpty()) {
                    LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                    layoutBannerAdBottom.setVisibility(View.VISIBLE);
                    TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                    Common_Utils.loadBannerAds(ActivityEveryDayReward.this, layoutBannerAdBottom, lblAdSpaceBottom);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setTimer() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            time = Common_Utils.timeDiff(endDate, todayDate);
            timer = new CountDownTimer(time * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText("");
                    layoutTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityEveryDayReward.this);
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
                    lblLoadingAds.setVisibility(View.GONE);
                    layoutAds.setVisibility(View.VISIBLE);

                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                    layoutAds.setVisibility(View.GONE);
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
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (timer != null) {
                    timer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityEveryDayReward.this);
            nativeAdLoaderWin.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdWin != null) {
                        nativeAdLoaderWin.destroy(nativeAdWin);
                    }
                    nativeAdWin = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    lblLoadingAds.setVisibility(View.GONE);
                    frameLayoutNativeAd.addView(nativeAdView);
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    //AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
                    frameLayoutNativeAd.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderWin.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinPopup(String point, EveryDay_Reward_Response_Model responseModel) {
        try {
            Dialog dialogWin = new Dialog(ActivityEveryDayReward.this, android.R.style.Theme_Light);
            dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogWin.setCancelable(false);
            dialogWin.setCanceledOnTouchOutside(false);
            dialogWin.setContentView(R.layout.dialog_win_spinner);
            dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            FrameLayout fl_adplaceholder = dialogWin.findViewById(R.id.fl_adplaceholder);
            TextView lblLoadingAds = dialogWin.findViewById(R.id.lblLoadingAds);
            if (Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
            } else {
                lblLoadingAds.setVisibility(View.GONE);
            }

            TextView tvPoint = dialogWin.findViewById(R.id.tvPoints);

            LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);
            Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());
            animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                    Common_Utils.startTextCountAnimation(tvPoint, point);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation_view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });

            TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
            try {
                int pt = Integer.parseInt(point);
                lblPoints.setText((pt <= 1 ? "Buck" : "Bucks"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblPoints.setText("Bucks");
            }

            AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
            ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ads_Utils.showAppLovinRewardedAd(ActivityEveryDayReward.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            if (dialogWin != null) {
                                dialogWin.dismiss();
                            }
                        }
                    });
                }
            });

            if (!Common_Utils.isStringNullOrEmpty(responseModel.getBtnName())) {
                btnOk.setText(responseModel.getBtnName());
            }

            if (!Common_Utils.isStringNullOrEmpty(responseModel.getBtnColor())) {
                Drawable mDrawable = ContextCompat.getDrawable(ActivityEveryDayReward.this, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getBtnColor()), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            }

            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinRewardedAd(ActivityEveryDayReward.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialogWin != null) {
                            dialogWin.dismiss();
                        }
                    }
                });
            });
            dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Common_Utils.GetCoinAnimation(ActivityEveryDayReward.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                }
            });
            if (!isFinishing() && !dialogWin.isShowing()) {
                dialogWin.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation_view.setVisibility(View.VISIBLE);
                        animation_view.playAnimation();
                    }
                }, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData(EveryDay_Reward_Response_Model responseModel) {
        try {
            btnClaim.setText("Claimed!");
            Common_Utils.logFirebaseEvent(ActivityEveryDayReward.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Daily_Rewards_Challenge", "Got Reward");
            showWinPopup(objDailyReward.getTotalPoints(), responseModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}