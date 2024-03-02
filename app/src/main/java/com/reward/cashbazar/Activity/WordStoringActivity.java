package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.reward.cashbazar.Adapter.NumberSortingAdapter;
import com.reward.cashbazar.Async.GetWordSortingDataAsync;
import com.reward.cashbazar.Async.Models.NumberSortingItem;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.WordSortingResponseModel;
import com.reward.cashbazar.Async.SaveWordSortingAsync;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.CountDownAnimation;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordStoringActivity extends AppCompatActivity {
    public List<NumberSortingItem> dataList = new ArrayList<>();
    public ArrayList<String> temp = new ArrayList<>();
    private Response_Model responseMain;
    private RecyclerView rvNumber;
    private ImageView ivHistory;
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private String todayDate, lastDate;
    private int nextGameTime, time, selPos = 0;
    private boolean isTimerSet = false, isFinishGame = false;
    private WordSortingResponseModel countModel;
    private NumberSortingAdapter countUpAdapter;
    private CountDownTimer timer, mainTimer;
    private LinearLayout layoutAds, llLimit, layoutPoints, layoutCountDownTimer;
    private TextView tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvLeftCount, tvNote, tvTimeUp;
    private RelativeLayout layoutMain, layoutData;
    private CountDownAnimation countDownAnimation;

    private TextView textView;
    private Spinner spinner;
    private String betterLuckMessage = "Better luck, next time!";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Common_Utils.setDayNightTheme(WordStoringActivity.this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_word_sorting);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        setViews();
        new GetWordSortingDataAsync(WordStoringActivity.this);
    }

    private void setViews() {
        layoutCountDownTimer = findViewById(R.id.layoutCountDownTimer);
        textView = findViewById(R.id.textView);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.animations_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        layoutData = findViewById(R.id.layoutData);
        layoutData.setVisibility(View.INVISIBLE);
        tvRemaining = findViewById(R.id.tvRemaining);
        rvNumber = findViewById(R.id.rvNumber);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        llLimit = findViewById(R.id.llLimit);
        layoutMain = findViewById(R.id.layoutMain);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        layoutAds = findViewById(R.id.layoutAds);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);

        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(WordStoringActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(WordStoringActivity.this);
                }
            }
        });
        ImageView ivBack = findViewById(R.id.iv_left);
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
//        Common_Utils.startRoundAnimation(WordStoringActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(WordStoringActivity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.WORD_SORTING)
                            .putExtra("title", "Word Sequencing History"));
                } else {
                    Common_Utils.NotifyLogin(WordStoringActivity.this);
                }
            }
        });
        initCountDownAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), WordStoringActivity.this);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), WordStoringActivity.this);
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
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
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

/*    @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null) {

                Common_Utils.dialogShowAppLuck(WordStoringActivity.this, responseModel.getAppLuck());
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

    private WordSortingResponseModel responseModel;

    public void setData(WordSortingResponseModel responseModel1) {
        responseModel = responseModel1;
        try {
            layoutData.setVisibility(VISIBLE);
            countModel = responseModel;
            if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            }
//            AppLogger.getInstance().e("Status",""+responseModel.getStatus());
            if (responseModel.getStatus().equals("2")) {
                Ads_Utils.showAppLovinInterstitialAd(WordStoringActivity.this, null);
                llLimit.setVisibility(VISIBLE);
                tvNote.setText("You have exhausted today's Word Sorting Game limit, please try again tomorrow.");
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
            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastDate() != null) {
                lastDate = responseModel.getLastDate();
            }
            if (responseModel.getNextGameTime() != null) {
                nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
            }
            AdpaterData(dataList);
            if (Integer.parseInt(responseModel.getRemainGameCount()) > 0) {
                setTimer1(true);
            }
            try {
                LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                if (Integer.parseInt(responseModel.getRemainGameCount()) > 0 && !isTimerSet && !responseModel.getStatus().equals("2") && !Common_Utils.isStringNullOrEmpty(countModel.getIsTodayTaskCompleted()) && countModel.getIsTodayTaskCompleted().equals("0")) {
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
//                                if (!Common_Utils.hasUsageAccessPermission(WordStoringActivity.this)) {
//                                    Common_Utils.showUsageAccessPermissionDialog(WordStoringActivity.this);
//                                    return;
//                                } else {
                                Common_Utils.Redirect(WordStoringActivity.this, countModel.getScreenNo(), "", "", "", "", "");
//                                }
                            } else if (!Common_Utils.isStringNullOrEmpty(countModel.getTaskId())) {
                                Intent intent = new Intent(WordStoringActivity.this, TaskDetailsActivity.class);
                                intent.putExtra("taskId", countModel.getTaskId());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(WordStoringActivity.this, TaskPage_Activity.class);
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
            setGameData();
            if (Integer.parseInt(responseModel.getRemainGameCount()) > 0 && !isTimerSet && !responseModel.getStatus().equals("2") && !Common_Utils.isStringNullOrEmpty(countModel.getIsTodayTaskCompleted()) && countModel.getIsTodayTaskCompleted().equals("1") && POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                startCountDownAnimation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setGameData() {
        if (countModel.getRemainGameCount() != null) {
            tvLeftCount.setText(countModel.getRemainGameCount());
        }

        dataList.clear();
        for (int i = 0; i < countModel.getData().size(); i++) {
            dataList.add(new NumberSortingItem("" + i, countModel.getData().get(i)));
        }

        temp.clear();
        temp.addAll(countModel.getData());
        Collections.sort(temp);
//        AppLogger.getInstance().e("SORTED", "SORTED : " + temp.toString());
        countUpAdapter.notifyDataSetChanged();

        if (countModel.getPoints() != null) {
            tvWinPoints.setText(countModel.getPoints());
        }
        tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000L));
    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), WordStoringActivity.this);
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
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
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

  /*  public void setTimer() {
        try {
            if (timer == null) {
                isTimerOver = false;
                textTypingTimer = new CountDownTimer(textTypingTime * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerOver = false;
                        tvRemainingTime.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isTimerOver = true;
                        Common_Utils.logFirebaseEvent(Typing_GameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing", "Time Over");
                        notifyTimeOver(Typing_GameActivity.this, getString(R.string.app_name), "Oops, time is over. Better luck, next time!", true);
                        if (textTypingTimer != null) {
                            textTypingTimer.cancel();
                            textTypingTimer = null;
                        }
                    }

                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void setTimer() {
        try {
            if (timer == null) {
                timer = new CountDownTimer(Integer.parseInt(countModel.getGameTime()) * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isFinishGame = true;
                        Common_Utils.logFirebaseEvent(WordStoringActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting", "Time Over");

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                betterLuckMessage = "Oops, Time is over.\nBetter luck next time!";
                                new SaveWordSortingAsync(WordStoringActivity.this, "0");
                            }
                        }, 100);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeCountDataValues(WordSortingResponseModel responseModel) {
        countModel = responseModel;
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (responseModel.getNextGameTime() != null) {
            nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
        }
        if (responseModel.getWinningPoints().equals("0")) {
            Common_Utils.logFirebaseEvent(WordStoringActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting", "Better Luck");
            showBetterluckPopup();
        } else {
            Common_Utils.logFirebaseEvent(WordStoringActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting", "Word Sorting Got Reward");
            showWinPopup(responseModel.getWinningPoints());
        }
    }

    public void AdpaterData(List<NumberSortingItem> dataList) {
        countUpAdapter = new NumberSortingAdapter(dataList, WordStoringActivity.this, new NumberSortingAdapter.ClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemClick(int position, View v, TextView textView) {
                try {
//                    AppLogger.getInstance().e("CLICKED", "CLICKED : " + dataList.get(position).getValue());
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        if (!isTimerSet && !isFinishGame && !dataList.get(position).isSelected()) {
                            if (temp.get(selPos).equals(textView.getText().toString())) {
                                dataList.get(position).setSelected(true);
                                countUpAdapter.notifyItemChanged(position);
                                temp.remove(selPos);
                                if (temp.isEmpty()) {
                                    isFinishGame = true;
                                    if (timer != null) {
                                        timer.cancel();
                                    }
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SaveWordSortingAsync(WordStoringActivity.this, countModel.getPoints());
                                        }
                                    }, 100);
                                }
                            } else {
                                isFinishGame = true;
                                if (timer != null) {
                                    timer.cancel();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        betterLuckMessage = "Oops, Wrong word selected.\nBetter luck next time!";
                                        new SaveWordSortingAsync(WordStoringActivity.this, "0");
                                    }
                                }, 100);
                            }
                        }
                    } else {
                        Common_Utils.NotifyLogin(WordStoringActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(WordStoringActivity.this, 5);
        rvNumber.setLayoutManager(mGridLayoutManager);
        rvNumber.setAdapter(countUpAdapter);
    }


    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(WordStoringActivity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(WordStoringActivity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinInterstitialAd(WordStoringActivity.this, new Ads_Utils.AdShownListener() {
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
                    Common_Utils.GetCoinAnimation(WordStoringActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    if (!Common_Utils.isStringNullOrEmpty(countModel.getRemainGameCount()) && countModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        isTimerSet = true;
                        tvNote.setText("You have exhausted today's Word Sorting Game limit, please try again tomorrow.");
                        tvLeftCount.setText(countModel.getRemainGameCount());
                        tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000L));
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Next game will be unlocked in");
                        setGameData();
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

    public void showBetterluckPopup() {
        Dialog dilaogBetterluck = new Dialog(WordStoringActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.dialog_better_luck_next_time);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText(betterLuckMessage);

        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            Ads_Utils.showAppLovinInterstitialAd(WordStoringActivity.this, new Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dilaogBetterluck != null) {
                        dilaogBetterluck.dismiss();
                    }
                }
            });
        });

/*        lDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llLimit.setVisibility(VISIBLE);
                Toast.makeText(WordStoringActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });*/

        dilaogBetterluck.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!Common_Utils.isStringNullOrEmpty(countModel.getRemainGameCount()) && countModel.getRemainGameCount().equals("0")) {
                    llLimit.setVisibility(VISIBLE);
                    isTimerSet = true;
                    tvTimeUp.setVisibility(GONE);
                    tvNote.setText("You have exhausted today's Word Sorting Game limit, please try again tomorrow.");
                    tvLeftCount.setText(countModel.getRemainGameCount());
                    tvRemaining.setText(Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(countModel.getGameTime()) * 1000L));
                } else {
                    llLimit.setVisibility(VISIBLE);
                    tvNote.setText("Next game will be unlocked in");
                    setGameData();
                    setTimer1(false);
                }
            }
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    public void setTimer1(boolean isFromOnCreate) {
        if (Common_Utils.timeDiffSeconds(todayDate, lastDate) > (nextGameTime * 60)) {
        } else {
            isTimerSet = true;
            tvTimeUp.setText("Try After Some Time");
            llLimit.setVisibility(VISIBLE);

            if (mainTimer != null) {
                mainTimer.cancel();
            }
            time = Common_Utils.timeDiffSeconds(todayDate, lastDate);
            mainTimer = new CountDownTimer(((nextGameTime * 60L) - time) * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerSet = true;
                    tvTimeUp.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    selPos = 0;
                    timer = null;
                    llLimit.setVisibility(GONE);
                    isTimerSet = false;
                    isFinishGame = false;
                    if (!isShowingAd) {
                        startCountDownAnimation();
                    }
                }
            }.start();
            if (isFromOnCreate) {
                isShowingAd = true;
                Ads_Utils.showAppLovinInterstitialAd(WordStoringActivity.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        isShowingAd = false;
                        if (!isTimerSet) {
                            startCountDownAnimation();
                        }
                    }
                });
            }
        }
    }
    private boolean isShowingAd = false;
    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                cancelCountDownAnimation();
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
                if (nativeAdTask != null && nativeAdLoaderTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initCountDownAnimation() {
        countDownAnimation = new CountDownAnimation(textView, POC_Constants.countDownTimerCount);
        countDownAnimation.setCountDownListener(new CountDownAnimation.CountDownListener() {
            @Override
            public void onCountDownEnd(CountDownAnimation animation) {
                layoutCountDownTimer.setVisibility(GONE);
                setTimer();
            }
        });
    }

    private void startCountDownAnimation() {
        layoutCountDownTimer.setVisibility(VISIBLE);
        // Customizable animation
        if (spinner.getSelectedItemPosition() == 1) { // Scale
            // Use scale animation
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            countDownAnimation.setAnimation(scaleAnimation);
        } else if (spinner.getSelectedItemPosition() == 2) { // Set (Scale +
            // Alpha)
            // Use a set of animations
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            countDownAnimation.setAnimation(animationSet);
        }
        // Customizable start count
        countDownAnimation.setStartCount(POC_Constants.countDownTimerCount);
        countDownAnimation.start();
    }

    private void cancelCountDownAnimation() {
        countDownAnimation.cancel();
    }
}