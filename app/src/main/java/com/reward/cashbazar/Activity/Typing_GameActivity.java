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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.GetTypeTextDataAsync;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.TypeTextDataModel;
import com.reward.cashbazar.Async.SaveTypeTextGameAsync;
import com.reward.cashbazar.Async.SaveWordSortingAsync;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;


public class Typing_GameActivity extends AppCompatActivity {
    private TextView tvDailyLimit, tvText, tvRemainCount, tvRemainingTime, tvWinningPoints, tvPoints, lblLoadingAds, tvMainTimer, lblTimer, tvAttemptsLeft;

    public ImageView iv_left, ivHistory, ivHelp;
    private LinearLayout layoutPoints, layoutContent, layoutTimer, layoutCompleteTask, layoutAds;
    private String todayDate, lastDate, mainTimerTime;
    private int remainCount, time, lifeline, textTypingTime;
    private TypeTextDataModel objTextTypingData;
    Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask, nativeAdTimer;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask, nativeAdLoaderTimer;
    private boolean isTimerSet = false, isTimerOver = false;
    private CountDownTimer mainTimer, textTypingTimer;
    private EditText etText;
    private Button btnClaimNow;

    private FrameLayout frameLayoutNativeAd;

    private RelativeLayout layoutMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Typing_GameActivity.this);
        setContentView(R.layout.activity_typing_game);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        initview();
        onClick();
    }

    private void initview() {

        iv_left = findViewById(R.id.iv_left);
        layoutPoints = findViewById(R.id.layoutPoints);
        ivHistory = findViewById(R.id.ivHistory);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);
        layoutMain = findViewById(R.id.layoutMain);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        ivHelp = findViewById(R.id.ivHelp);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        tvWinningPoints = findViewById(R.id.tvWinningPoints);
        tvDailyLimit = findViewById(R.id.tvDailyLimit);
        tvRemainCount = findViewById(R.id.tvRemainCount);
        tvRemainingTime = findViewById(R.id.tvRemainingTime);
        tvMainTimer = findViewById(R.id.tvMainTimer);
        lblTimer = findViewById(R.id.lblTimer);
        tvAttemptsLeft = findViewById(R.id.tvAttemptsLeft);
        tvText = findViewById(R.id.tvText);
        etText = findViewById(R.id.etText);
        layoutTimer = findViewById(R.id.layoutTimer);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (etText.getText().toString().length() > 0) {
                        setTimer();
                    }
                    if (tvText.getText().toString().trim().length() > 0 && etText.getText().toString().length() >= tvText.getText().toString().trim().length()) {
                        btnClaimNow.setEnabled(true);
                    } else {
                        btnClaimNow.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        etText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        etText.setLongClickable(false);
        etText.setTextIsSelectable(false);

        btnClaimNow = findViewById(R.id.btnClaimNow);
        btnClaimNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Common_Utils.setEnableDisable(Typing_GameActivity.this, v);
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        if (remainCount > 0) {
                            if (lifeline > 0 && !tvText.getText().toString().trim().equals(etText.getText().toString())) {
                                lifeline = lifeline - 1;
                                tvAttemptsLeft.setText(String.valueOf(lifeline));
                                Common_Utils.NotifyMessage(Typing_GameActivity.this, "Text Didn't Match!", "Sorry, typed text did not match with given text, please try again.\n\n" + lifeline + " attempt is left.", false);
                            } else {
//                                AppLogger.getInstance().e("TAG----", " " + objTextTypingData.getData().getPoints());
                                if (!isTimerOver) {
                                    if (textTypingTimer != null) {
                                        textTypingTimer.cancel();
                                        textTypingTimer = null;
                                    }
                                    new SaveTypeTextGameAsync(Typing_GameActivity.this, objTextTypingData.getData().getPoints(), objTextTypingData.getData().getId(), etText.getText().toString());
                                } else {
                                    Common_Utils.Notify(Typing_GameActivity.this, getString(R.string.app_name), "Time is over. Better luck, next time!", false);
                                }

                            }
                        } else {
                            Common_Utils.Notify(Typing_GameActivity.this, "Text Typing Limit Over", "You have exhausted your text typing daily limit, please try again tomorrow.", false);
                        }
                    } else {
                        Common_Utils.NotifyLogin(Typing_GameActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        new GetTypeTextDataAsync(Typing_GameActivity.this);

    }

    public void setTimer() {
        try {
            if (textTypingTimer == null) {
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
    }
    private void notifyTimeOver(Typing_GameActivity activity, String title, String message, boolean isFinish) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.dialog_better_luck_next_time);
                dialog1.setCancelable(false);

                Button btnOk = dialog1.findViewById(R.id.btnOk);
                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
                tvMessage.setText(message);
                btnOk.setOnClickListener(v -> {
                    Ads_Utils.showAppLovinInterstitialAd(Typing_GameActivity.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            try {
                                dialog1.dismiss();
                                if (isFinish && !activity.isFinishing()) {
                                    activity.finish();
                                }
                                new SaveTypeTextGameAsync(Typing_GameActivity.this, objTextTypingData.getData().getPoints(), objTextTypingData.getData().getId(), etText.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                                showBetterluckPopup();
                            }
                        }
                    });
                });
                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onClick() {
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Typing_GameActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Typing_GameActivity.this);
                }
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Typing_GameActivity.this, PointDetailsActivity.class).putExtra("type", POC_Constants.HistoryType.TYPE_TEXT_TYPING).putExtra("title", "Typing Game History"));
                } else {
                    Common_Utils.NotifyLogin(Typing_GameActivity.this);
                }
            }
        });

    }

    public void setData(TypeTextDataModel responseModel) {
        objTextTypingData = responseModel;
//        AppLogger.getInstance().e("#point",objTextTypingData.getData().getPoints());
        if (responseModel.getData() != null) {
            layoutContent.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
            try {
                if (objTextTypingData.getTodayDate() != null) {
                    todayDate = objTextTypingData.getTodayDate();
                }
                if (objTextTypingData.getLastDate() != null) {
                    lastDate = objTextTypingData.getLastDate();
                }

                if (objTextTypingData.getMainTimer() != null) {
                    mainTimerTime = objTextTypingData.getMainTimer();
                }

                if (objTextTypingData.getRemainCount() != null) {
                    tvRemainCount.setText(objTextTypingData.getRemainCount());
                    remainCount = Integer.parseInt(objTextTypingData.getRemainCount());
                }
                if (objTextTypingData.getData().getTimer() != null) {
                    textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                }
                tvRemainingTime.setText(Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                tvDailyLimit.setText(objTextTypingData.getTotalCount());
                tvText.setText(objTextTypingData.getData().getText());
                tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                if (objTextTypingData.getLifeline() != null) {
                    lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                    tvAttemptsLeft.setText(String.valueOf(lifeline));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setMainTimer(true);
            checkForTaskCompletion();


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
                    Common_Utils.loadTopBannerAd(Typing_GameActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!Common_Utils.isStringNullOrEmpty(objTextTypingData.getHelpVideoUrl())) {
                ivHelp.setVisibility(VISIBLE);
                ivHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common_Utils.openUrl(Typing_GameActivity.this, objTextTypingData.getHelpVideoUrl());
                    }
                });
            }
            loadNativeAdInMainScreen();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTypeTextDataAsync(Typing_GameActivity.this);
    }

    /*    @Override
    public void onBackPressed() {
        try {
            if (objTextTypingData != null && objTextTypingData.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(Typing_GameActivity.this, objTextTypingData.getAppLuck());
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

    public void setMainTimer(boolean isFromOnCreate) {
        if (remainCount == 0) {
            isTimerSet = false;
            showTimerView(true);

            if (isFromOnCreate) {
                Ads_Utils.showAppLovinInterstitialAd(Typing_GameActivity.this, null);
            }
            return;
        }
        if (Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(mainTimerTime)) {
            isTimerSet = false;

        } else {
            isTimerSet = true;
            showTimerView(false);

            if (mainTimer != null) {
                mainTimer.cancel();
            }
            time = Common_Utils.timeDiff(todayDate, lastDate);
            mainTimer = new CountDownTimer((Integer.parseInt(mainTimerTime) - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvMainTimer.setText(Common_Utils.updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    try {
                        isTimerSet = false;
                        layoutTimer.setVisibility(View.GONE);
                        tvMainTimer.setText("");
                        checkForTaskCompletion();
                        if (layoutAds.getVisibility() == GONE) {
                            loadNativeAdInMainScreen();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            if (isFromOnCreate) {
                Ads_Utils.showAppLovinInterstitialAd(Typing_GameActivity.this, null);
            }
        }
    }
    private void checkForTaskCompletion() {
        try {
            layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
            if (remainCount > 0 && !isTimerSet && !Common_Utils.isStringNullOrEmpty(objTextTypingData.getIsTodayTaskCompleted()) && objTextTypingData.getIsTodayTaskCompleted().equals("0")) {
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
                tvTaskNote.setText(objTextTypingData.getTaskNote());
                Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                if (!Common_Utils.isStringNullOrEmpty(objTextTypingData.getTaskButton())) {
                    btnCompleteTask.setText(objTextTypingData.getTaskButton());
                }
                btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Common_Utils.isStringNullOrEmpty(objTextTypingData.getScreenNo())) {
//                            if (!POC_Common_Utils.hasUsageAccessPermission(Typing_GameActivity.this)) {
//                                POC_Common_Utils.showUsageAccessPermissionDialog(Typing_GameActivity.this);
//                                return;
//                            } else {
                                Common_Utils.Redirect(Typing_GameActivity.this, objTextTypingData.getScreenNo(), "", "", "", "", "");
//                            }
                        } else if (!Common_Utils.isStringNullOrEmpty(objTextTypingData.getTaskId())) {
                            Intent intent = new Intent(Typing_GameActivity.this, TaskPage_Activity.class);
                            intent.putExtra("taskId", objTextTypingData.getTaskId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Typing_GameActivity.this, TaskDetailsActivity.class);
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
    }

    public void changeTextTypingDataValues(TypeTextDataModel responseModel) {
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        tvDailyLimit.setText(responseModel.getTotalCount());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }

        if (responseModel.getMainTimer() != null) {
            mainTimerTime = responseModel.getMainTimer();
        }
        if (responseModel.getRemainCount() != null) {
            tvRemainCount.setText(responseModel.getRemainCount());
            remainCount = Integer.parseInt(responseModel.getRemainCount());
        }
        objTextTypingData.setData(responseModel.getData());
        if (responseModel.getWinningPoints().equals("0")) {
            Common_Utils.logFirebaseEvent(Typing_GameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing", "Better Luck");
            showBetterluckPopup();
        } else {
            Common_Utils.logFirebaseEvent(Typing_GameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing", "Text Typing Got Reward");
            showWinPopup(responseModel.getWinningPoints());
        }
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Typing_GameActivity.this, android.R.style.Theme_Light);
            dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogWin.setCancelable(false);
            dialogWin.setCanceledOnTouchOutside(false);
            dialogWin.setContentView(R.layout.dialog_win_spinner);
            dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            FrameLayout fl_adplaceholder = dialogWin.findViewById(R.id.fl_adplaceholder);
//            TextView lblLoadingAds = dialogWin.findViewById(R.id.lblLoadingAds);
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
                    Ads_Utils.showAppLovinRewardedAd(Typing_GameActivity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinRewardedAd(Typing_GameActivity.this, new Ads_Utils.AdShownListener() {
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
                    Common_Utils.GetCoinAnimation(Typing_GameActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    tvText.setText(objTextTypingData.getData().getText());
                    tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                    if (objTextTypingData.getData().getTimer() != null) {
                        textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                    }
                    tvRemainingTime.setText(Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                    etText.setText("");
                    lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                    tvAttemptsLeft.setText(String.valueOf(lifeline));
                    setMainTimer(false);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Typing_GameActivity.this);
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

    public void showBetterluckPopup() {
        Dialog dilaogBetterluck = new Dialog(Typing_GameActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.dialog_better_luck_next_time);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText("Sorry, text didn't match.\nBetter luck next time!");
        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);


        lDone.setOnClickListener(v -> {
            Ads_Utils.showAppLovinInterstitialAd(Typing_GameActivity.this, new Ads_Utils.AdShownListener() {
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
                tvText.setText(objTextTypingData.getData().getText());
                tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                if (objTextTypingData.getData().getTimer() != null) {
                    textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                }
                tvRemainingTime.setText(Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                etText.setText("");
                lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                tvAttemptsLeft.setText(String.valueOf(lifeline));
                setMainTimer(false);
            }
        });
        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }


    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Typing_GameActivity.this);
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

    private void showTimerView(boolean isLimitOver) {

        try {
            if (nativeAd != null && nativeAdLoader != null) {
                nativeAdLoader.destroy(nativeAd);
                nativeAd = null;
                frameLayoutNativeAd = null;
                layoutAds.setVisibility(GONE);
            }
            layoutTimer.setVisibility(VISIBLE);
            if (isLimitOver) {
                tvMainTimer.setVisibility(GONE);
                lblTimer.setText("You have exhausted your typing daily limit, please try again tomorrow.");
            } else {
                tvMainTimer.setVisibility(VISIBLE);
                lblTimer.setText("Please wait, Typing will get unlock in ");
            }
                LinearLayout layoutAdsTimer = findViewById(R.id.layoutAdsTimer);
            TextView lblLoadingAdsTimer = findViewById(R.id.lblLoadingAdsTimer);
            FrameLayout nativeAdTimer = findViewById(R.id.fl_adplaceholder_timer);

            if (Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAdsTimer(layoutAdsTimer, nativeAdTimer, lblLoadingAdsTimer);
            } else {
                layoutAdsTimer.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAdsTimer(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTimer = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Typing_GameActivity.this);
            nativeAdLoaderTimer.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdTimer != null) {
                        nativeAdLoaderTimer.destroy(nativeAdTimer);
                    }
                    nativeAdTimer = ad;
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
            nativeAdLoaderTimer.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNativeAdInMainScreen() {
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (layoutTimer.getVisibility() == GONE && layoutCompleteTask.getVisibility() == GONE) {
            if (Common_Utils.isShowAppLovinNativeAds()) {
                lblLoadingAds.setVisibility(VISIBLE);
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(GONE);
            }
        } else {
            layoutAds.setVisibility(GONE);
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Typing_GameActivity.this);
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

}
