/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import com.reward.cashbazar.Async.GetMilestoneHistoryAsync;
import com.reward.cashbazar.Async.Models.MilestoneDataMenu;
import com.reward.cashbazar.Async.Models.MilestonesResponseModel;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.StoreMilestoneAsync;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class MilestoneHistoryActivity extends AppCompatActivity {
    private TextView tvPoints, lblLoadingAds, lblCompleted;
    private ImageView ivBack, ivSmallIcon, ivBanner;
    private TextView txtPoints, txtTitle, txtSubtitle;
    private LottieAnimationView ltSmallIcon, ivLottieView;
    private RelativeLayout layoutButton;
    private Button lInstallBtn;
    private LinearLayout lTaskMain;
    private WebView webDisclaimer;
    private RelativeLayout layoutTaskBanner, layoutMain;
    private LinearLayout layoutPoints, layoutAds, layoutDescription;
    private MilestoneDataMenu objMilestone;
    private String milestoneId;
    private View viewShine;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private CardView cardDisclaimer, cardImage;
    private TextView tvTimer, tvPercentage, tvRequire, tvCompleted, tvTime;
    private ProgressBar progressBarCompletion;
    private View view1, view2;
    private CountDownTimer timer;
    private Response_Model responseMain;
    private boolean isClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(MilestoneHistoryActivity.this);
        setContentView(R.layout.activity_milestone_history);
        milestoneId = getIntent().getStringExtra("milestoneId");
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        initView();
        new GetMilestoneHistoryAsync(MilestoneHistoryActivity.this, milestoneId);
    }

    private void initView() {
        lblCompleted = findViewById(R.id.lblCompleted);
        layoutMain = findViewById(R.id.layoutMain);
        tvTimer = findViewById(R.id.tvTimer);
        tvPercentage = findViewById(R.id.tvPercentage);
        tvCompleted = findViewById(R.id.tvCompleted);
        tvRequire = findViewById(R.id.tvRequire);
        tvTime = findViewById(R.id.tvTime);
        progressBarCompletion = findViewById(R.id.progressBarCompletion);
        cardImage = findViewById(R.id.cardImage);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        cardDisclaimer = findViewById(R.id.cardDisclaimer);
        layoutTaskBanner = findViewById(R.id.layoutTaskBanner);
        layoutDescription = findViewById(R.id.layoutDescription);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(MilestoneHistoryActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(MilestoneHistoryActivity.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lInstallBtn = findViewById(R.id.lInstallBtn);
        viewShine = findViewById(R.id.viewShine);
        layoutButton = findViewById(R.id.layoutButton);
        lTaskMain = findViewById(R.id.lTaskMain);
        ivSmallIcon = findViewById(R.id.ivSmallIcon);
        txtPoints = findViewById(R.id.txtPoints);
        txtTitle = findViewById(R.id.txtTitle);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        ivBanner = findViewById(R.id.ivBanner);
        webDisclaimer = findViewById(R.id.webDisclamier);
        ltSmallIcon = findViewById(R.id.ltSmallIcon);
        ivLottieView = findViewById(R.id.ivLottieView);
        lTaskMain.setVisibility(View.INVISIBLE);
        layoutButton.setVisibility(View.GONE);

    }

    public void setData(MilestonesResponseModel responseModel) {
        if (responseModel != null) {
            objMilestone = responseModel.getSingleMilestoneData();
            if (objMilestone.getIsShowInterstitial() != null && objMilestone.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                Ads_Utils.showAppLovinInterstitialAd(MilestoneHistoryActivity.this, null);
            } else if (objMilestone.getIsShowInterstitial() != null && objMilestone.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_REWARD)) {
                Ads_Utils.showAppLovinRewardedAd(MilestoneHistoryActivity.this, null);
            }

            if (Common_Utils.isShowAppLovinNativeAds()) {
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                layoutAds = findViewById(R.id.layoutAds);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                loadAppLovinNativeAds();
            }

            // Load task banner
            try {
                if (!Common_Utils.isStringNullOrEmpty(objMilestone.getBanner())) {
                    layoutTaskBanner.setVisibility(View.VISIBLE);
                    if (objMilestone.getBanner().contains(".json")) {
                        ivBanner.setVisibility(View.GONE);
                        ivLottieView.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(ivLottieView, objMilestone.getBanner());
                        ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                    } else {
                        ivBanner.setVisibility(View.VISIBLE);
                        ivLottieView.setVisibility(View.GONE);
                        Glide.with(getApplicationContext()).load(objMilestone.getBanner()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                ivBanner.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.rectangle_white));
                                return false;
                            }
                        }).into(ivBanner);
                    }
                } else {
                    layoutTaskBanner.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                    Common_Utils.loadTopBannerAd(MilestoneHistoryActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!Common_Utils.isStringNullOrEmpty(objMilestone.getIcon())) {
                if (objMilestone.getIcon().contains(".json")) {
                    ivSmallIcon.setVisibility(View.GONE);
                    ltSmallIcon.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(ltSmallIcon, objMilestone.getIcon());
                    ltSmallIcon.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    ivSmallIcon.setVisibility(View.VISIBLE);
                    ltSmallIcon.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(objMilestone.getIcon()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            ivSmallIcon.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.rectangle_white));
                            return false;
                        }
                    }).into(ivSmallIcon);
                }
            } else {
                cardImage.setVisibility(View.GONE);
            }

            lInstallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClaim) {
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            new StoreMilestoneAsync(MilestoneHistoryActivity.this, objMilestone.getPoints(), objMilestone.getId());
                        } else {
                            Common_Utils.NotifyLogin(MilestoneHistoryActivity.this);
                        }
                    } else {
                        Common_Utils.Redirect(MilestoneHistoryActivity.this, objMilestone.getScreenNo(), objMilestone.getTitle(), "", objMilestone.getId(), objMilestone.getId(), objMilestone.getBanner());
                    }
                }
            });

            if (objMilestone.getTitle() != null) {
                txtTitle.setText(objMilestone.getTitle());
            }

            tvPercentage.setText(objMilestone.getCompletionPercent() + "%");
            if (objMilestone.getType().equals("0"))// number target
            {
                lblCompleted.setText("Completed:");
                tvRequire.setText(objMilestone.getTargetNumber());
                tvCompleted.setText(objMilestone.getNoOfCompleted());
            } else {// points target
                lblCompleted.setText("Earned:");
                tvRequire.setText(objMilestone.getTargetPoints());
                tvCompleted.setText(objMilestone.getEarnedPoints());
            }
            progressBarCompletion.setProgress((int) Double.parseDouble(String.valueOf(objMilestone.getCompletionPercent())));
            view1.setLayoutParams(new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.dim_1), Float.parseFloat(String.valueOf(objMilestone.getCompletionPercent()))));
            view2.setLayoutParams(new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.dim_1), Float.parseFloat(String.valueOf(100 - Double.parseDouble(objMilestone.getCompletionPercent())))));
            int day = Common_Utils.getDaysDiff(objMilestone.getEndDate(), objMilestone.getTodayDate());
            tvTime.setVisibility(day > 1 ? View.VISIBLE : View.GONE);
            tvTimer.setVisibility(day > 1 ? View.GONE : View.VISIBLE);
            if (day > 1) {
                tvTime.setText(day + " days");
            } else {
                if (timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(Common_Utils.timeDiff(objMilestone.getTodayDate(), objMilestone.getEndDate()) * 60000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvTimer.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        tvTimer.setText("Time's Up!");
                    }
                }.start();
            }

            if (!Common_Utils.isStringNullOrEmpty(objMilestone.getDescription())) {
                txtSubtitle.setText(objMilestone.getDescription());
            } else {
                layoutDescription.setVisibility(View.GONE);
            }
            if (!Common_Utils.isStringNullOrEmpty(objMilestone.getPoints())) {
                try {
                    txtPoints.setText(objMilestone.getPoints());
                    TextView tvTaskRupees = findViewById(R.id.tvTaskRupees);
                    tvTaskRupees.setText(Common_Utils.convertPointsInINR(objMilestone.getPoints(), new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getPointValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            lTaskMain.setVisibility(View.VISIBLE);
            layoutButton.setVisibility(View.VISIBLE);

            Animation animUpDown = AnimationUtils.loadAnimation(MilestoneHistoryActivity.this, R.anim.left_right);
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

            if (!Common_Utils.isStringNullOrEmpty(objMilestone.getNotes())) {
                webDisclaimer.loadData(objMilestone.getNotes(), "text/html", "UTF-8");
            } else {
                cardDisclaimer.setVisibility(View.GONE);
            }

            if (objMilestone.getButtonColor() != null && objMilestone.getButtonColor().length() > 0) {
                Drawable mDrawable = ContextCompat.getDrawable(MilestoneHistoryActivity.this, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(objMilestone.getButtonColor()), PorterDuff.Mode.SRC_IN));
                lInstallBtn.setBackground(mDrawable);
                lInstallBtn.setTextColor(Color.parseColor(objMilestone.getButtonTextColor()));
            }
            if (objMilestone.getButtonName() != null) {
                lInstallBtn.setText(objMilestone.getButtonName());
            }
            isClaim = false;
            try {
                if (objMilestone.getType().equals("0"))// number target
                {
                    if (Integer.parseInt(objMilestone.getNoOfCompleted()) >= Integer.parseInt(objMilestone.getTargetNumber())) {
                        isClaim = true;
                    }
                } else {// points target
                    if (Integer.parseInt(objMilestone.getEarnedPoints()) >= Integer.parseInt(objMilestone.getTargetPoints())) {
                        isClaim = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isClaim) {
                lInstallBtn.setText("CLAIM NOW");
                Drawable mDrawable = ContextCompat.getDrawable(MilestoneHistoryActivity.this, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.green), PorterDuff.Mode.SRC_IN));
                lInstallBtn.setBackground(mDrawable);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getLovinNativeID()), MilestoneHistoryActivity.this);
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

                    ////AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    ////AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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
            if (timer != null) {
                timer.cancel();
            }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeMilestoneValues(MilestonesResponseModel responseModel) {
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        Common_Utils.logFirebaseEvent(MilestoneHistoryActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Milestones", "Milestones Got Reward");
        showWinPopup(responseModel.getWinningPoints());
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(MilestoneHistoryActivity.this, android.R.style.Theme_Light);
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
            ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ads_Utils.showAppLovinRewardedAd(MilestoneHistoryActivity.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            if (dialogWin != null) {
                                dialogWin.dismiss();
                            }
                        }
                    });
                }
            });

            TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
            AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
            try {
                int pt = Integer.parseInt(point);
                lblPoints.setText((pt <= 1 ? "Buck" : "Bucks"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblPoints.setText("Bucks");
            }

            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinRewardedAd(MilestoneHistoryActivity.this, new Ads_Utils.AdShownListener() {
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
                    try {
                        setResult(RESULT_OK);
                        Common_Utils.GetCoinAnimation(MilestoneHistoryActivity.this, layoutMain, layoutPoints);
                        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                        layoutButton.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (!MilestoneHistoryActivity.this.isFinishing() && !dialogWin.isShowing()) {
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), MilestoneHistoryActivity.this);
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
                    ////AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    ////AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
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
}