package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Show_Video_List_Adapter;
import com.reward.cashbazar.Async.Get_Show_Video_List_Async;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Show_Video_List;
import com.reward.cashbazar.Async.Models.Show_Video_Model;
import com.reward.cashbazar.Async.Store_Watch_Video_List_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class Win_by_Watching_VideosActivity extends AppCompatActivity {
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvVideoList;
    private TextView lblLoadingAds, tvPoints;
    private Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Show_Video_List_Adapter mAdapter;
    private ImageView ivHistory;
    private LinearLayout layoutPoints;
    private String todayDate, lastDate, watchTime;
    private CountDownTimer timer;
    private int time, activeVideoPos = -1;
    public List<Show_Video_List> listVideos = new ArrayList<>();
    private boolean isTimerSet = false, isSetTimerValue = false;
    private RelativeLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Win_by_Watching_VideosActivity.this);
        setContentView(R.layout.activity_win_by_watch_video);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        layoutMain = findViewById(R.id.layoutMain);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        rvVideoList = findViewById(R.id.rvVideoList);

        ivHistory = findViewById(R.id.ivHistory);
      //  Common_Utils.startRoundAnimation(WatchVideoActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Win_by_Watching_VideosActivity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.WATCH_VIDEO)
                            .putExtra("title", "Win Watching History"));
                } else {
                    Common_Utils.NotifyLogin(Win_by_Watching_VideosActivity.this);
                }
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Win_by_Watching_VideosActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Win_by_Watching_VideosActivity.this);
                }
            }
        });
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Get_Show_Video_List_Async(Win_by_Watching_VideosActivity.this);
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void changeVideoDataValues(Show_Video_Model responseModel) {
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastVideoWatchedDate() != null) {
            lastDate = responseModel.getLastVideoWatchedDate();
        }
        if (responseModel.getWatchTime() != null) {
            watchTime = responseModel.getWatchTime();
        }
        Common_Utils.logFirebaseEvent(Win_by_Watching_VideosActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Watch_Video", "Watch Video Got Reward");
        showWinPopup(listVideos.get(activeVideoPos).getVideoPoints(), responseModel.getIsShowAds());
    }

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Win_by_Watching_VideosActivity.this, android.R.style.Theme_Light);
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
                    if (isShowAds != null && isShowAds.equals("1")) {
                        Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                if (dialogWin != null) {
                                    dialogWin.dismiss();
                                }
                            }
                        });
                    } else {
                        if (dialogWin != null) {
                            dialogWin.dismiss();
                        }
                    }
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
                Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (isShowAds != null && isShowAds.equals("1")) {
                            Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, new Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    if (dialogWin != null) {
                                        dialogWin.dismiss();
                                    }
                                }
                            });
                        } else {
                            if (dialogWin != null) {
                                dialogWin.dismiss();
                            }
                        }
                    }
                });
            });
            dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    try {
                        Common_Utils.GetCoinAnimation(Win_by_Watching_VideosActivity.this, layoutMain, layoutPoints);
                        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                        listVideos.get(activeVideoPos).setWatchedVideoPoints(point);
                        listVideos.get(activeVideoPos).setButtonText(null);
                        mAdapter.updateLastWatchedVideo(Integer.parseInt(listVideos.get(activeVideoPos).getVideoId()));
                        mAdapter.notifyItemChanged(activeVideoPos);
                        activeVideoPos = activeVideoPos + 1;
                        mAdapter.notifyItemChanged(activeVideoPos);
                        setTimer(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Win_by_Watching_VideosActivity.this);
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

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Win_by_Watching_VideosActivity.this);
            nativeAdLoaderTask.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdTask != null) {
                        nativeAdLoaderTask.destroy(nativeAdTask);
                    }
                    nativeAdTask = ad;
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
                    layoutAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderTask.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setData(Show_Video_Model responseModel) {
        if (responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0) {
            listVideos.addAll(responseModel.getWatchVideoList());
            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastVideoWatchedDate() != null) {
                lastDate = responseModel.getLastVideoWatchedDate();
            }
            if (responseModel.getWatchTime() != null) {
                watchTime = responseModel.getWatchTime();
            }
            for (int i = 0; i < listVideos.size(); i++) {
                if (Integer.parseInt(listVideos.get(i).getVideoId()) == (Integer.parseInt(responseModel.getLastWatchedVideoId()) + 1)) {
                    activeVideoPos = i;
                }
                if (responseModel.getWatchedVideoList() != null && responseModel.getWatchedVideoList().size() > 0) {
                    for (int j = 0; j < responseModel.getWatchedVideoList().size(); j++) {
                        if (listVideos.get(i).getVideoId().equals(responseModel.getWatchedVideoList().get(j).getVideoId())) {
                            listVideos.get(i).setWatchedVideoPoints(responseModel.getWatchedVideoList().get(j).getWatchedVideoPoints());
                        }
                    }
                }
            }
            mAdapter = new Show_Video_List_Adapter(listVideos, this, Integer.parseInt(responseModel.getLastWatchedVideoId()), (!Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("1")), new Show_Video_List_Adapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        try {
                            if (Integer.parseInt(listVideos.get(position).getVideoId()) == (Integer.parseInt(listVideos.get(activeVideoPos).getVideoId())) && !isTimerSet) {
                                if (Common_Utils.isNetworkAvailable(Win_by_Watching_VideosActivity.this)) {
                                    if (listVideos.get(position).getAdsType().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                                        Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, new Ads_Utils.VideoAdShownListener() {
                                            @Override
                                            public void onAdDismiss(boolean isAdShown) {
                                                if (isAdShown) {
                                                    // call api
                                                    new Store_Watch_Video_List_Async(Win_by_Watching_VideosActivity.this, listVideos.get(position).getVideoId(), listVideos.get(position).getVideoPoints());
                                                } else {
                                                    Common_Utils.setToast(Win_by_Watching_VideosActivity.this, "Problem while displaying video");
                                                }
                                            }
                                        }, true);
                                    } else if (listVideos.get(position).getAdsType().equals(POC_Constants.APPLOVIN_REWARD)) {
                                        Ads_Utils.showAppLovinRewardedAd(Win_by_Watching_VideosActivity.this, new Ads_Utils.VideoAdShownListener() {
                                            @Override
                                            public void onAdDismiss(boolean isAdShown) {
                                                if (isAdShown) {
                                                    // call api
                                                    new Store_Watch_Video_List_Async(Win_by_Watching_VideosActivity.this, listVideos.get(position).getVideoId(), listVideos.get(position).getVideoPoints());
                                                } else {
                                                    Common_Utils.setToast(Win_by_Watching_VideosActivity.this, "Problem while displaying video");
                                                }
                                            }
                                        }, true);
                                    }
                                } else {
                                    Common_Utils.setToast(Win_by_Watching_VideosActivity.this, "No internet connection");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Common_Utils.NotifyLogin(Win_by_Watching_VideosActivity.this);
                    }
                }
            });
            rvVideoList.setLayoutManager(new LinearLayoutManager(this));
            rvVideoList.setAdapter(mAdapter);
            if (!responseModel.getLastWatchedVideoId().equals(listVideos.get(listVideos.size() - 1).getVideoId())) {
                setTimer(true);
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!isTimerSet && !Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);
                        LinearLayout layoutAdsTask = findViewById(R.id.layoutAdsTask);
                        TextView lblLoadingAdsTask = findViewById(R.id.lblLoadingAdsTask);
                        FrameLayout nativeAdTask = findViewById(R.id.fl_adplaceholder_task);

                        if (Common_Utils.isShowAppLovinNativeAds()) {
                            loadAppLovinNativeAdsTask(layoutAdsTask, nativeAdTask, lblLoadingAdsTask);
                        } else {
                            layoutAdsTask.setVisibility(GONE);
                        }
                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(responseModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if(!Common_Utils.isStringNullOrEmpty(responseModel.getTaskButton())){
                            btnCompleteTask.setText(responseModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!Common_Utils.isStringNullOrEmpty(responseModel.getScreenNo())) {
//                                    if (!POC_Common_Utils.hasUsageAccessPermission(Win_by_Watching_VideosActivity.this)) {
//                                        POC_Common_Utils.showUsageAccessPermissionDialog(Win_by_Watching_VideosActivity.this);
//                                        return;
//                                    } else {
                                        Common_Utils.Redirect(Win_by_Watching_VideosActivity.this, responseModel.getScreenNo(), "", "", "", "", "");
//                                    }
                                } else if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskId())) {
                                    Intent intent = new Intent(Win_by_Watching_VideosActivity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", responseModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Win_by_Watching_VideosActivity.this, TasksCategoryTypeActivity.class);
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
            } else {
                Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, null);
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
                    Common_Utils.loadTopBannerAd(Win_by_Watching_VideosActivity.this, layoutTopAds, responseModel.getTopAds());
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
        rvVideoList.setVisibility(responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0 ? View.VISIBLE : View.GONE);
        ivLottieNoData.setVisibility(responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0 ? View.GONE : View.VISIBLE);
        if (responseModel.getWatchVideoList() == null && responseModel.getWatchVideoList().size() == 0)
            ivLottieNoData.playAnimation();
    }

    public void setTimer(boolean isFromOnCreate) {
        try {
            isSetTimerValue = false;
            if (Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(watchTime)) {
                isTimerSet = false;
                listVideos.get(activeVideoPos).setButtonText("Watch Now");
                mAdapter.notifyItemChanged(activeVideoPos);
            } else {
                isTimerSet = true;
                if (timer != null) {
                    timer.cancel();
                }
                time = Common_Utils.timeDiff(todayDate, lastDate);
                timer = new CountDownTimer((Integer.parseInt(watchTime) - time) * 60000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {
                            listVideos.get(activeVideoPos).setButtonText(Common_Utils.updateTimeRemainingWatchVideo(millisUntilFinished));
                            if (!isSetTimerValue) {
                                mAdapter.notifyItemChanged(activeVideoPos);
                            } else {
                                RecyclerView.ViewHolder v = rvVideoList.findViewHolderForAdapterPosition(activeVideoPos);
                                TextView tv = v.itemView.findViewById(R.id.tvButton);
                                tv.setText(Common_Utils.updateTimeRemainingWatchVideo(millisUntilFinished));
                            }
                            isSetTimerValue = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        try {
                            isTimerSet = false;
                            listVideos.get(activeVideoPos).setButtonText("Watch Now");
                            mAdapter.notifyItemChanged(activeVideoPos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                if (isFromOnCreate) {
                    Ads_Utils.showAppLovinInterstitialAd(Win_by_Watching_VideosActivity.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Win_by_Watching_VideosActivity.this);
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
                if (timer != null) {
                    timer.cancel();
                }
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (nativeAdTask != null && nativeAdLoaderTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}