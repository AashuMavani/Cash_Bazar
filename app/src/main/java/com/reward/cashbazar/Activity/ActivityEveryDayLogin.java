package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.EveryDayClaimAdapter;
import com.reward.cashbazar.Async.Models.EveryDay_Bonus;
import com.reward.cashbazar.Async.Models.EveryDay_Bonus_Item;
import com.reward.cashbazar.Async.Models.Everyday_Bonus_Data_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Reward_Screen_Model;
import com.reward.cashbazar.Async.Store_Daily_Login_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import org.jetbrains.annotations.Async;

import java.util.ArrayList;

public class ActivityEveryDayLogin extends AppCompatActivity {
    private RecyclerView rvDailyLoginList;
    private LinearLayout layoutPoints;
    private ImageView ivHistory;
    private ArrayList<EveryDay_Bonus_Item> listData = new ArrayList<>();
    private TextView tvPoints, lblDailyLogin, lblLoadingAds;
    private Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd;
    private int selectedPos = -1, lastClaimDay;
    private BroadcastReceiver watchWebsiteBroadcast;
    private IntentFilter intentFilter;
    private EveryDayClaimAdapter dailyLoginAdapter;
    private RelativeLayout layoutMain;
    private Reward_Screen_Model objRewardScreenModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(ActivityEveryDayLogin.this);
        setContentView(R.layout.activity_every_day_login);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        objRewardScreenModel = (Reward_Screen_Model) getIntent().getSerializableExtra("objRewardScreenModel");

        layoutMain = findViewById(R.id.layoutMain);
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lblDailyLogin = findViewById(R.id.lblDailyLogin);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityEveryDayLogin.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ActivityEveryDayLogin.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
   //     Common_Utils.startRoundAnimation(ActivityDailyLogin.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityEveryDayLogin.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.DAILY_LOGIN)
                            .putExtra("title", "Daily Login History"));
                } else {
                    Common_Utils.NotifyLogin(ActivityEveryDayLogin.this);
                }
            }
        });

        try {
            TextView lblNote = findViewById(R.id.lblNote);
            LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
            if (!Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getIsTodayTaskCompleted()) && objRewardScreenModel.getIsTodayTaskCompleted().equals("0")) {
                layoutCompleteTask.setVisibility(VISIBLE);
                lblNote.setVisibility(GONE);
                TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                tvTaskNote.setText(objRewardScreenModel.getTaskNote());
                Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                if(!Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskButton())){
                    btnCompleteTask.setText(objRewardScreenModel.getTaskButton());
                }

                btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      if (!Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getScreenNo())) {

                                Common_Utils.Redirect(ActivityEveryDayLogin.this, objRewardScreenModel.getScreenNo(), "", "", "", "", "");
                        } else if (!Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskId())) {
                            Intent intent = new Intent(ActivityEveryDayLogin.this, TaskDetailsActivity.class);
                            intent.putExtra("taskId", objRewardScreenModel.getTaskId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ActivityEveryDayLogin.this, TasksCategoryTypeActivity.class);
                            intent.putExtra("taskTypeId", POC_Constants.TASK_TYPE_ALL);
                            intent.putExtra("title", "Tasks");
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            } else {
                layoutCompleteTask.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            listData.addAll(objRewardScreenModel.getDailyBonus().getData());
            lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
            if (lastClaimDay > 0) {
                lblDailyLogin.setText("Checked in for " + lastClaimDay + (lastClaimDay == 1 ? " consecutive day" : " consecutive days"));
            } else {
                lblDailyLogin.setText("Check in for the first day to get reward bucks!");
            }
            rvDailyLoginList = findViewById(R.id.rvDailyLoginList);
            dailyLoginAdapter = new EveryDayClaimAdapter(listData, ActivityEveryDayLogin.this, lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()), new EveryDayClaimAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        if ((Integer.parseInt(listData.get(position).getDay_id())) <= lastClaimDay) {
                            Common_Utils.setToast(ActivityEveryDayLogin.this, "You have already collected reward for day " + listData.get(position).getDay_id());
                        } else if ((objRewardScreenModel.getDailyBonus().getIsTodayClaimed() != null && objRewardScreenModel.getDailyBonus().getIsTodayClaimed().equals("1"))) {
                            Common_Utils.setToast(ActivityEveryDayLogin.this, "You have already collected reward for today");
                        } else if (Integer.parseInt(listData.get(position).getDay_id()) > (lastClaimDay + 1)) {
                            Common_Utils.setToast(ActivityEveryDayLogin.this, "Please claim reward for day " + (lastClaimDay + 1));
                        } else {
                            Ads_Utils.showAppLovinInterstitialAd(ActivityEveryDayLogin.this, new Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    selectedPos = position;
                                    dailyLoginAdapter.setClicked();
                                    new Store_Daily_Login_Async(ActivityEveryDayLogin.this, listData.get(position).getDay_points(), listData.get(position).getDay_id());
                                }
                            });
                        }
                    } else {
                        Common_Utils.NotifyLogin(ActivityEveryDayLogin.this);
                    }
                }
            });

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(ActivityEveryDayLogin.this, 5);
            mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rvDailyLoginList.setLayoutManager(mGridLayoutManager);
            rvDailyLoginList.setItemAnimator(new DefaultItemAnimator());
            rvDailyLoginList.setAdapter(dailyLoginAdapter);

            // Load home note webview top
            try {
                if (!Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getDailyBonus().getHomeNote())) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(View.VISIBLE);
                    webNote.loadDataWithBaseURL(null, objRewardScreenModel.getDailyBonus().getHomeNote(), "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (objRewardScreenModel.getDailyBonus().getTopAds() != null && !Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getDailyBonus().getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    Common_Utils.loadTopBannerAd(ActivityEveryDayLogin.this, layoutTopAds, objRewardScreenModel.getDailyBonus().getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (objRewardScreenModel.getDailyBonus().getIsTodayClaimed() != null && objRewardScreenModel.getDailyBonus().getIsTodayClaimed().equals("1")) {
                Ads_Utils.showAppLovinInterstitialAd(ActivityEveryDayLogin.this, null);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

 /*   @Override
    public void onBackPressed() {
        try {
            if (responseMain != null && responseMain.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(ActivityEveryDayLogin.this, responseMain.getAppLuck());
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }*/

/*    @Override
    protected void onResume() {
        super.onResume();
        objRewardScreenModel = (Reward_Screen_Model) getIntent().getSerializableExtra("objRewardScreenModel");
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onDailyLoginDataChanged(Everyday_Bonus_Data_Model responseModel) {
        if (lastClaimDay > 0) {
            lblDailyLogin.setText("Checked in for " + lastClaimDay + (lastClaimDay == 1 ? " consecutive day" : " consecutive days"));
        } else {
            lblDailyLogin.setText("Check in for the first day to get reward bucks!");
        }
        if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        }
        if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS) || responseModel.getStatus().equals("3")) {
            Common_Utils.logFirebaseEvent(ActivityEveryDayLogin.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Daily_Login", "Got Reward");
            showWinPopup(listData.get(selectedPos).getDay_points(), responseModel);
        } else if (responseModel.getStatus().equals("2")) {// missed login
            Common_Utils.logFirebaseEvent(ActivityEveryDayLogin.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Daily_Login", "Missed Daily Login");
            showMissedLoginDialog("Daily Login is Missed", responseModel);
        }
        lastClaimDay = Integer.parseInt(responseModel.getLastClaimedDay());
        dailyLoginAdapter.setLastClaimedData(lastClaimDay, Integer.parseInt(responseModel.getIsTodayClaimed()));
        EveryDay_Bonus obj = objRewardScreenModel.getDailyBonus();
        obj.setLastClaimedDay(responseModel.getLastClaimedDay());
        obj.setIsTodayClaimed(responseModel.getIsTodayClaimed());
        objRewardScreenModel.setDailyBonus(obj);
        RewardPage_Activity.objRewardScreenModel = objRewardScreenModel;
    }

    public void showErrorMessage(String title, String message) {
        try {
            final Dialog dialog1 = new Dialog(ActivityEveryDayLogin.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.dialog_notification);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(ActivityEveryDayLogin.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                    }
                });
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMissedLoginDialog(String title, Everyday_Bonus_Data_Model responseModel) {
        try {
            final Dialog dialog1 = new Dialog(ActivityEveryDayLogin.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.dialog_notification);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(responseModel.getMessage());
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(ActivityEveryDayLogin.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                    }
                });
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showWinPopup(String point, Everyday_Bonus_Data_Model responseModel) {
        Dialog dialogWin = new Dialog(ActivityEveryDayLogin.this, android.R.style.Theme_Light);
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
                if (dialogWin != null) {
                    dialogWin.dismiss();
                    objRewardScreenModel = (Reward_Screen_Model) getIntent().getSerializableExtra("objRewardScreenModel");
                }
            }
        });

        if (!Common_Utils.isStringNullOrEmpty(responseModel.getBtnName())) {
            btnOk.setText(responseModel.getBtnName());
        }

        if (!Common_Utils.isStringNullOrEmpty(responseModel.getBtnColor())) {
            Drawable mDrawable = ContextCompat.getDrawable(ActivityEveryDayLogin.this, R.drawable.ic_btn_gradient_rounded_corner_red);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getBtnColor()), PorterDuff.Mode.SRC_IN));
            btnOk.setBackground(mDrawable);
        }

        btnOk.setOnClickListener(v -> {
            Ads_Utils.showAppLovinRewardedAd(ActivityEveryDayLogin.this, new Ads_Utils.AdShownListener() {
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
                selectedPos = -1;
                Common_Utils.GetCoinAnimation(ActivityEveryDayLogin.this, layoutMain, layoutPoints);
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
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityEveryDayLogin.this);
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

    public void registerBroadcast() {
        watchWebsiteBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unRegisterReceivers();
                //AppLogger.getInstance().e("WATCH WEBSITE", "Broadcast Received==" + intent.getAction());
                if (intent.getAction().equals(POC_Constants.WATCH_WEBSITE_RESULT)) {
                    if (intent.getExtras().getString("status").equals(POC_Constants.STATUS_SUCCESS)) {
                        dailyLoginAdapter.setClicked();
                        new Store_Daily_Login_Async(ActivityEveryDayLogin.this, listData.get(selectedPos).getDay_points(), listData.get(selectedPos).getDay_id());
                    } else {
                        Common_Utils.Notify(Ads_Utils.getCurrentActivity(), "Visit Website", "Oops - " + objRewardScreenModel.getDailyBonus().getWatchWebsiteTime() + " Seconds NOT completed!", false);
                    }
                }
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(POC_Constants.WATCH_WEBSITE_RESULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(watchWebsiteBroadcast, intentFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(watchWebsiteBroadcast, intentFilter);
        }
    }
    private void unRegisterReceivers() {
        if (watchWebsiteBroadcast != null) {
//            //AppLogger.getInstance().e("SplashActivity", "Unregister Broadcast");
            unregisterReceiver(watchWebsiteBroadcast);
            watchWebsiteBroadcast = null;
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityEveryDayLogin.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd.setVisibility(View.VISIBLE);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}