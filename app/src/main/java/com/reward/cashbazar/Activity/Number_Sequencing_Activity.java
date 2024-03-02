package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.reward.cashbazar.utils.Common_Utils.convertTimeInMillis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.CountDownAdapter;
import com.reward.cashbazar.Async.Get_Count_Down_Async;
import com.reward.cashbazar.Async.Models.Count_Down_Item;
import com.reward.cashbazar.Async.Models.Count_Down_model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Store_Count_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;

public class Number_Sequencing_Activity extends AppCompatActivity {
    private Response_Model responseMain;
    private RecyclerView rvNumber;
    private ImageView ivHistory, ivBack;

    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private String todayDate, lastDate;
    int gameTime, nextGameTime;

    private boolean isAssending = true, isTimerOver, isTimerOn = false;

    Count_Down_model countModel;

    boolean isWrongSelect = false;

    int gameType;

    private CountDownAdapter countUpAdapter;

    private CountDownTimer timer, mainTimer;

    private long lastClickTime = 0;
    int time, selPos = 0;

    public List<Count_Down_Item> dataList;
    private LottieAnimationView ivLottieNoData, ltStartTimer;

    private LinearLayout layoutAds, llLimit, layoutPoints, layoutNoData, llRecycle, layoutCompleteTask;
    private TextView tvInfo, tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvLeftCount, tvNote, tvTimeUp;
    public ArrayList<Integer> temp = new ArrayList<>();
    private RelativeLayout ilAttempt, layoutMain, relStartTimer;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Common_Utils.setDayNightTheme(CountUpActivity.this);
        super.onCreate(savedInstanceState);
        Common_Utils.setDayNightTheme(Number_Sequencing_Activity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_number_sequencing);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        tvRemaining = findViewById(R.id.tvRemaining);
        rvNumber = findViewById(R.id.rvNumber);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        relStartTimer = findViewById(R.id.relStartTimer);
        llLimit = findViewById(R.id.llLimit);
        ilAttempt = findViewById(R.id.ilAttempt);
//        llRecycle = findViewById(R.id.llRecycle);
        layoutMain = findViewById(R.id.layoutMain);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        layoutAds = findViewById(R.id.layoutAds);
        layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
        ltStartTimer = findViewById(R.id.ltStartTimer);
        ivHistory = findViewById(R.id.ivHistory);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        tvInfo = findViewById(R.id.tvInfo);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);

        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Number_Sequencing_Activity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Number_Sequencing_Activity.this);
                }
            }
        });
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }
        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Number_Sequencing_Activity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.Count)
                            .putExtra("title", "Number Sequencing History"));
                } else {
                    Common_Utils.NotifyLogin(Number_Sequencing_Activity.this);
                }
            }
        });

        new Get_Count_Down_Async(Number_Sequencing_Activity.this);


    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Number_Sequencing_Activity.this);
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
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Number_Sequencing_Activity.this);
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

    private Count_Down_model responseModel;
    public void setData(Count_Down_model responseModel1) {
        responseModel = responseModel1;
        try {
            if (responseModel.getStatus().equals("2")) {
                Ads_Utils.showAppLovinInterstitialAd(Number_Sequencing_Activity.this, null);
                llLimit.setVisibility(VISIBLE);
                tvNote.setText("You have exhausted today's Number Sequencing Game limit, please try again tomorrow.");

                if (responseModel.getRemainGameCount() != null) {
                    tvLeftCount.setText( responseModel.getRemainGameCount());
                }
                if (responseModel.getGameType() != null) {
                    gameType = Integer.parseInt(responseModel.getGameType());
                }
                if (gameType == 0) {
                    tvInfo.setText("Tap the Numbers From Lowest to Highest");
                    isAssending = true;
                } else {
                    tvInfo.setText("Tap the Numbers From Highest to Lowest");
                    isAssending = false;
                }

                temp = new ArrayList<>();

                for (int i = 0; i < responseModel.getData().size(); i++) {
                    String value;
                    value = String.valueOf(responseModel.getData().get(i).getValue());

                    if (!value.isEmpty()) {
                        temp.add((Integer.parseInt(value)));
                    }
                }

                if (isAssending) {
                    Collections.sort(temp);
                } else {
                    Collections.sort(temp, Collections.reverseOrder());
                }

                dataList = new ArrayList<>();
                dataList.addAll(responseModel.getData());
                isTimerOn = true;

                AdpaterData(dataList);

                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
                tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000));

            } else {
                countModel = responseModel;
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                }
                try {
                    if (!Common_Utils.isStringNullOrEmpty(countModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, countModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!Common_Utils.isStringNullOrEmpty(countModel.getIsTodayTaskCompleted()) && countModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(countModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!Common_Utils.isStringNullOrEmpty(countModel.getTaskButton())) {
                            btnCompleteTask.setText(countModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!Common_Utils.isStringNullOrEmpty(countModel.getScreenNo())) {
//                                    if (!POC_Common_Utils.hasUsageAccessPermission(Number_Sequencing_Activity.this)) {
//                                        POC_Common_Utils.showUsageAccessPermissionDialog(Number_Sequencing_Activity.this);
//                                        return;
//                                    } else {
                                        Common_Utils.Redirect(Number_Sequencing_Activity.this, countModel.getScreenNo(), "", "", "", "", "");
//                                    }
                                } else if (!Common_Utils.isStringNullOrEmpty(countModel.getTaskId())) {
                                    Intent intent = new Intent(Number_Sequencing_Activity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", countModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Number_Sequencing_Activity.this, TasksCategoryTypeActivity.class);
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

                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastDate() != null) {
                    lastDate = responseModel.getLastDate();
                }
                if (responseModel.getGameTime() != null) {
                    gameTime = Integer.parseInt(responseModel.getGameTime());
                }
                if (responseModel.getNextGameTime() != null) {
                    nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvLeftCount.setText(responseModel.getRemainGameCount());
                }
                if (responseModel.getGameType() != null) {
                    gameType = Integer.parseInt(responseModel.getGameType());
                }
                if (gameType == 0) {
                    tvInfo.setText("Tap the Numbers From Lowest to Highest");
                    isAssending = true;
                } else {
                    tvInfo.setText("Tap the Numbers From Highest to Lowest");
                    isAssending = false;
                }

                temp = new ArrayList<>();

                for (int i = 0; i < responseModel.getData().size(); i++) {
                    String value;
                    value = String.valueOf(responseModel.getData().get(i).getValue());

                    if (!value.isEmpty()) {
                        temp.add((Integer.parseInt(value)));
                    }
                }

                if (isAssending) {
                    Collections.sort(temp);
                } else {
                    Collections.sort(temp, Collections.reverseOrder());
                }
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    setTimer1(true);
                }


                dataList = new ArrayList<>();
                dataList.addAll(responseModel.getData());

                AdpaterData(dataList);

                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
                tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000));
            }
        } catch (Exception e) {


        }
    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Number_Sequencing_Activity.this);
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

    public void setTimer() {
        try {
            if (timer == null) {
                timer = new CountDownTimer(Integer.parseInt(countModel.getGameTime()) * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerOver = false;
                        tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isTimerOver = true;
                        Common_Utils.logFirebaseEvent(Number_Sequencing_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Count_Up", "Time Over");

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Store_Count_Async(Number_Sequencing_Activity.this, "0");
                            }
                        }, 100);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameStartTimer() {
        if (layoutCompleteTask.getVisibility() == GONE) {
            relStartTimer.setVisibility(VISIBLE);
            ltStartTimer.setMinFrame(60);
            ltStartTimer.playAnimation();
            ltStartTimer.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    relStartTimer.setVisibility(GONE);
                    setTimer();

                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {

                }
            });
        }
    }

    public void changeCountDataValues(Count_Down_model responseModel) {
        countModel = responseModel;
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (responseModel.getGameTime() != null) {
            gameTime = Integer.parseInt(responseModel.getGameTime());
        }
        if (responseModel.getNextGameTime() != null) {
            nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
        }

        //AppLogger.getInstance().e("POints--!", "" + responseModel.getWinningPoints());
        if (responseModel.getWinningPoints().equals("0")) {
//            AppLogger.getInstance().e("Bucks", "" + responseModel.getWinningPoints());
            Common_Utils.logFirebaseEvent(Number_Sequencing_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Count_Up", "Better Luck");
            if (isWrongSelect) {
                showBetterluckPopup("Oops, you selected wrong number. Better luck next time!");
            } else {
                showBetterluckPopup("Oops, time is over. Better luck, next time!");
            }
        } else {

            Common_Utils.logFirebaseEvent(Number_Sequencing_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Count_Up", "Count Up Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());
        }
        isWrongSelect = false;
    }

    public void AdpaterData(List<Count_Down_Item> dataList) {

        countUpAdapter = new CountDownAdapter(dataList, Number_Sequencing_Activity.this, new CountDownAdapter.ClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemClick(int position, View v, TextView textView) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (relStartTimer.getVisibility() == GONE) {
                    if (!isTimerOn) {
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            if (temp.get(selPos) == (Integer.parseInt(textView.getText().toString()))) {
                                textView.setBackground(getResources().getDrawable(R.drawable.bg_lucky_number_selected));
                                textView.setTextColor(getResources().getColor(R.color.white));
                                v.setEnabled(false);
                                temp.remove(selPos);

                                if (temp.isEmpty()) {
                                    timer.cancel();
                                    new Store_Count_Async(Number_Sequencing_Activity.this, countModel.getPoints());
                                    return;
                                }
                                return;
                            } else {
                                isWrongSelect = true;
                                timer.cancel();
                                new Store_Count_Async(Number_Sequencing_Activity.this, "0");
                                return;

                            }
                            /* }*/
                        } else {

                            Common_Utils.NotifyLogin(Number_Sequencing_Activity.this);
                        }
                    }
                }
            }
        });
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(Number_Sequencing_Activity.this, 6);
        rvNumber.setLayoutManager(mGridLayoutManager);
        rvNumber.setAdapter(countUpAdapter);
    }

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Number_Sequencing_Activity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(Number_Sequencing_Activity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinInterstitialAd(Number_Sequencing_Activity.this, new Ads_Utils.AdShownListener() {
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
                    tvLeftCount.setText( countModel.getRemainGameCount());
                    tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000));


                    Common_Utils.GetCoinAnimation(Number_Sequencing_Activity.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    if (!Common_Utils.isStringNullOrEmpty(countModel.getRemainGameCount()) && countModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        isTimerOn = true;
                        tvNote.setText("You have exhausted today's Number Sequencing Game limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Try After Some Time");
                        setTimer1(false);
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

    public void showBetterluckPopup(String message) {
        try {
            final Dialog dilaogBetterluck = new Dialog(Number_Sequencing_Activity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.dialog_notification);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(Number_Sequencing_Activity.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dilaogBetterluck != null) {
                            dilaogBetterluck.dismiss();
                        }
                    }
                });

            });

            dilaogBetterluck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    tvLeftCount.setText(countModel.getRemainGameCount());
                    tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000));

                    if (!Common_Utils.isStringNullOrEmpty(countModel.getRemainGameCount()) && countModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        isTimerOn = true;
                        tvTimeUp.setVisibility(GONE);
                        tvNote.setText("You have exhausted today's Number Sequencing Game limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Try After Some Time");
                        setTimer1(false);
                    }


                }
            });

            if (!isFinishing() && !dilaogBetterluck.isShowing()) {
                dilaogBetterluck.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTimer1(boolean isFromOnCreate) {


        if (timeDiff(todayDate, lastDate) > nextGameTime) {
            setGameStartTimer();

        } else {
            isTimerOn = true;
            tvTimeUp.setText("Try After Some Time");
            llLimit.setVisibility(VISIBLE);

            if (mainTimer != null) {
                mainTimer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            mainTimer = new CountDownTimer((nextGameTime - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerOn = true;
                    tvTimeUp.setText(updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    setGameStartTimer();
                    selPos = 0;
                    timer = null;
                    isTimerOn = false;
                    llLimit.setVisibility(GONE);
                    if (countModel.getGameType() != null) {
                        gameType = Integer.parseInt(countModel.getGameType());
                    }
                    if (gameType == 0) {
                        tvInfo.setText("Tap the Numbers From Lowest to Highest");
                        isAssending = true;
                    } else {
                        tvInfo.setText("Tap the Numbers From Highest to Lowest");
                        isAssending = false;
                    }

                    temp = new ArrayList<>();
                    for (int i = 0; i < countModel.getData().size(); i++) {
                        String value;
                        value = String.valueOf(countModel.getData().get(i).getValue());

                        if (!value.isEmpty()) {
                            temp.add((Integer.parseInt(value)));
                        }
                    }

                    if (isAssending) {
                        Collections.sort(temp);
                    } else {
                        Collections.sort(temp, Collections.reverseOrder());
                    }

                    dataList = new ArrayList<>();
                    dataList.addAll(countModel.getData());

                    AdpaterData(dataList);

                }
            }.start();
            if (isFromOnCreate && relStartTimer.getVisibility() == GONE) {
                Ads_Utils.showAppLovinInterstitialAd(Number_Sequencing_Activity.this, null);
            }
        }
    }

    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (timer != null) {
                    timer.cancel();
                }
                if (mainTimer != null) {
                    mainTimer.cancel();
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
                if (nativeAdTask != null && nativeAdTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String updateTimeRemaining(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "Time's up!!";
        }
    }

    private int timeDiff(String date1, String Date2) {
        long diff = convertTimeInMillis("yyyy-MM-dd HH:mm:ss", date1) - convertTimeInMillis("yyyy-MM-dd HH:mm:ss", Date2);
        double seconds = Math.abs(diff) / 1000;
        int minutes = (int) (seconds / 60);
        return minutes;
    }
/*    @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(Number_Sequencing_Activity.this, responseModel.getAppLuck());
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