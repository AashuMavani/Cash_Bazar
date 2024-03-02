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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Get_Dice_Async;
import com.reward.cashbazar.Async.Models.Dice_Data_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Store_Dice_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.Locale;
import java.util.Random;

public class Craps_Game_Activity extends AppCompatActivity {

    LottieAnimationView btnSpinNow3, btnSpinNow2, btnSpinNow1;
    private long lastClickTime = 0;
    private Response_Model responseMain;
    private LinearLayout layoutAds, llLimit, layoutPoints,lnr_all_task;
    private String todayDate, lastDate;
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    RelativeLayout llPlay, layoutMain,ilAttempt;
    private ImageView ivHistory;
    int counter = 0, ran1, ran2, ran3, todayLeft = 0, gameTime;
    Dice_Data_Model diceDataModel;
    int today_left;
    boolean isWin = false, isTimerOn = false;
    TextView tvMakeTotal, tvDice3, tvDice2, tvDice1, tvTotal2, tvPlayGame, lblLoadingAds, tvLeftCount, tvWinPoints, tvPoints, tvTotalPlay, tvRemaining;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Common_Utils.setDayNightTheme(Craps_Game_Activity.this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_craps_game);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        tvMakeTotal = findViewById(R.id.tvMakeTotal);
        tvDice3 = findViewById(R.id.tvDice3);
        tvDice2 = findViewById(R.id.tvDice2);
        tvDice1 = findViewById(R.id.tvDice1);
        tvRemaining = findViewById(R.id.tvRemaining);
        tvTotal2 = findViewById(R.id.tvTotal2);
        layoutAds = findViewById(R.id.layoutAds);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutMain = findViewById(R.id.layoutMain);
        btnSpinNow3 = findViewById(R.id.btnSpinNow3);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvTotalPlay = findViewById(R.id.tvTotalPlay);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        tvWinPoints = findViewById(R.id.tvWinPoints);
        ivHistory = findViewById(R.id.ivHistory);
        tvPoints = findViewById(R.id.tvPoints);
        llLimit = findViewById(R.id.llLimit);
        llPlay = findViewById(R.id.llPlay);
        btnSpinNow2 = findViewById(R.id.btnSpinNow2);
        tvPlayGame = findViewById(R.id.tvPlayGame);
        btnSpinNow1 = findViewById(R.id.btnSpinNow1);
        ilAttempt = findViewById(R.id.ilAttempt);


        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Craps_Game_Activity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Craps_Game_Activity.this);
                }
            }
        });
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //  Common_Utils.startRoundAnimation(Dice_Roll_Activity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Craps_Game_Activity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.Dice_Game)
                            .putExtra("title", "Craps Game History"));
                } else {
                    Common_Utils.NotifyLogin(Craps_Game_Activity.this);
                }
            }
        });

        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        new Get_Dice_Async(Craps_Game_Activity.this);

        btnSpinNow1.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });


    }

    private CountDownTimer timer;
    int time;

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Craps_Game_Activity.this);
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

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Craps_Game_Activity.this);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Craps_Game_Activity.this);
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

    private Dice_Data_Model responseModel;
    @SuppressLint("SetTextI18n")
    public void setData(Dice_Data_Model responseModel1) {
        responseModel = responseModel1;
        if (responseModel.getStatus().equals("2")) {
            Ads_Utils.showAppLovinInterstitialAd(Craps_Game_Activity.this, null);
            llLimit.setVisibility(VISIBLE);
            ilAttempt.setVisibility(GONE);
            llPlay.setVisibility(View.GONE);
        } else {
            //AppLogger.getInstance().e("SETDATA", "" + responseModel.getData().size());
            diceDataModel = responseModel;

            if (responseModel.getData() != null && responseModel.getData().size() > 0) {
                try {
                    if (responseModel.getTodayDate() != null) {
                        todayDate = responseModel.getTodayDate();
                    }
                    if (responseModel.getLastDate() != null) {
                        lastDate = responseModel.getLastDate();
                    }
                    if (responseModel.getTotalGameCount() != null) {
                        tvTotalPlay.setText(responseModel.getTotalGameCount());
                    }
                    if (responseModel.getRemainGameCount() != null) {
                        tvRemaining.setText(responseModel.getRemainGameCount());
                    }

                    if (responseModel.getGameTime() != null) {
                        gameTime = Integer.parseInt(responseModel.getGameTime());
                    }

                    if (responseModel.getTotalCount() != null) {
                        todayLeft = Integer.parseInt(responseModel.getTotalCount());
                        counter = Integer.parseInt(responseModel.getTotalCount());
                        tvLeftCount.setText(responseModel.getTotalCount());

                    }

                    if (responseModel.getMakeTotal() != null) {
                        tvMakeTotal.setText(responseModel.getMakeTotal());
                    }
                    if (responseModel.getPoints() != null) {
                        tvWinPoints.setText(responseModel.getPoints());
                    }

                    tvTotal2.setText("0");

                    playDiesLottie(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Common_Utils.dismissProgressLoader();
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
                        Common_Utils.loadTopBannerAd(Craps_Game_Activity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load Bottom banner ad
                try {
                    LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                    TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                    Common_Utils.loadBannerAds(Craps_Game_Activity.this, layoutBannerAdBottom, lblAdSpaceBottom);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getIsTodayTaskCompleted()) && diceDataModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(diceDataModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getTaskButton())) {
                            btnCompleteTask.setText(diceDataModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getScreenNo())) {
//                                    if (!POC_Common_Utils.hasUsageAccessPermission(Craps_Game_Activity.this)) {
//                                        POC_Common_Utils.showUsageAccessPermissionDialog(Craps_Game_Activity.this);
//                                        return;
//                                    } else {
                                        Common_Utils.Redirect(Craps_Game_Activity.this, diceDataModel.getScreenNo(), "", "", "", "", "");
//                                    }
                                } else if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getTaskId())) {
                                    Intent intent = new Intent(Craps_Game_Activity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", diceDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Craps_Game_Activity.this, TasksCategoryTypeActivity.class);
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

                // Load floating ad
                try {
                    if (responseModel.getFloatingAds() != null && !Common_Utils.isStringNullOrEmpty(responseModel.getFloatingAds().getImage())) {
                        if (responseModel.getFloatingAds().getImage().endsWith(".json")) {
                            LottieAnimationView floatAdLottie = findViewById(R.id.floatAdLottie);
                            floatAdLottie.setVisibility(android.view.View.VISIBLE);
                            Common_Utils.setLottieAnimation(floatAdLottie, responseModel.getFloatingAds().getImage());
                            floatAdLottie.setRepeatCount(LottieDrawable.INFINITE);
                            floatAdLottie.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.Redirect(Craps_Game_Activity.this, responseModel.getFloatingAds().getScreenNo(), responseModel.getFloatingAds().getTitle(), responseModel.getFloatingAds().getUrl(), responseModel.getFloatingAds().getId(), responseModel.getFloatingAds().getTaskId(), responseModel.getFloatingAds().getImage());
                                }
                            });
                        } else {
                            ImageView floatAd = findViewById(R.id.floatAd);
                            floatAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.Redirect(Craps_Game_Activity.this, responseModel.getFloatingAds().getScreenNo(), responseModel.getFloatingAds().getTitle(), responseModel.getFloatingAds().getUrl(), responseModel.getFloatingAds().getId(), responseModel.getFloatingAds().getTaskId(), responseModel.getFloatingAds().getImage());
                                }
                            });
                            Glide.with(this)
                                    .load(responseModel.getFloatingAds().getImage())
                                    .override(getResources().getDimensionPixelSize(R.dimen.dim_65), getResources().getDimensionPixelSize(R.dimen.dim_65))
                                    .addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            floatAd.setVisibility(View.VISIBLE);
                                            return false;
                                        }
                                    }).into(floatAd);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            llPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        if (!isTimerOn) {

                            Log.e("Playgame" , "" +Integer.parseInt(tvLeftCount.getText().toString()) +" "+ counter + Integer.parseInt(tvLeftCount.getText().toString()) + " " + isWin );
                            if (Integer.parseInt(tvLeftCount.getText().toString()) <= counter && Integer.parseInt(tvLeftCount.getText().toString()) != 0 && !isWin)
                            {
                                playDiesLottie(true);

                                tvDice1.setText(String.valueOf(ran1));
                                tvDice2.setText(String.valueOf(ran2));
                                tvDice3.setText(String.valueOf(ran3));
                                int sum_add = ran1+ ran2 + ran3;

                                tvTotal2.setText(String.valueOf(sum_add));

                                if (tvMakeTotal.getText().toString().matches(String.valueOf(sum_add))) {
                                    isWin = true;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new Store_Dice_Async(Craps_Game_Activity.this, diceDataModel.getPoints());
                                        }
                                    }, 500);
                                }
                                todayLeft--;
                                tvLeftCount.setText(String.valueOf(todayLeft));

                                if (todayLeft == 0 && !tvMakeTotal.getText().toString().matches(String.valueOf(sum_add))) {
                                    isWin = true;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            new Store_Dice_Async(Craps_Game_Activity.this, "0");
                                        }
                                    }, 1000);

                                }
                            }
                            else {
                                isWin = true;
                                new Store_Dice_Async(Craps_Game_Activity.this, "0");
                            }
                        } else {
                            Toast.makeText(Craps_Game_Activity.this, "Please Wait ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Common_Utils.NotifyLogin(Craps_Game_Activity.this);
                    }
                }
            });

            setTimer(true);
        }


    }

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
                if (nativeAdTask != null && nativeAdTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setTimer(boolean isFromOnCreate) {

        if (timeDiff(todayDate, lastDate) > gameTime) {

            llPlay.setEnabled(true);
            tvPlayGame.setText("Play Game");
        } else {
            llPlay.setEnabled(false);
            if (timer != null) {
                timer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            timer = new CountDownTimer((gameTime - time) * 60000L, 1000) {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onTick(long millisUntilFinished) {

                    tvPlayGame.setText(updateTimeRemaining(millisUntilFinished));
                    tvPlayGame.setTextColor(getResources().getColor(R.color.red));
                }

                @Override
                public void onFinish() {
                    isTimerOn = false;
                    llPlay.setEnabled(true);
                    tvPlayGame.setTextColor(getResources().getColor(R.color.black));
                    tvPlayGame.setText("Play Game");
                }
            }.start();
            if (isFromOnCreate) {
                Ads_Utils.showAppLovinInterstitialAd(Craps_Game_Activity.this, null);
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

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Craps_Game_Activity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(Craps_Game_Activity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinInterstitialAd(Craps_Game_Activity.this, new Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(diceDataModel.getRemainGameCount());

                    if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getRemainGameCount()) && diceDataModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        llPlay.setVisibility(View.GONE);
                        ilAttempt.setVisibility(View.GONE);

                    }
                    Common_Utils.GetCoinAnimation(Craps_Game_Activity.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    setTimer(false);
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


    public void playDiesLottie(boolean isPlay) {
        ran1 = Common_Utils.getRandomNumberBetweenRange(1, 6);
        ran2 = Common_Utils.getRandomNumberBetweenRange(1, 6);
        ran3 = Common_Utils.getRandomNumberBetweenRange(1, 6);

        Common_Utils.setLottieAnimation(btnSpinNow1, diceDataModel.getData().get(ran1 - 1).getDice());

        Common_Utils.setLottieAnimation(btnSpinNow2, diceDataModel.getData().get(ran2 - 1).getDice());

        Common_Utils.setLottieAnimation(btnSpinNow3, diceDataModel.getData().get(ran3 - 1).getDice());


        if (isPlay) {
            btnSpinNow1.playAnimation();
            btnSpinNow2.playAnimation();
            btnSpinNow3.playAnimation();
        }
    }

    public int getRandom() {
        return new Random().nextInt((6 - 1) + 1) + 1;
    }

    public void changeDiceDataValues(Dice_Data_Model responseModel) {
        diceDataModel = responseModel;

        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (diceDataModel.getMakeTotal() != null) {
            tvMakeTotal.setText(diceDataModel.getMakeTotal());
        }
        if (diceDataModel.getTotalCount() != null) {
            todayLeft = Integer.parseInt(diceDataModel.getTotalCount());
            counter = Integer.parseInt(diceDataModel.getTotalCount());
            tvLeftCount.setText(diceDataModel.getTotalCount());

        }
//        if (responseModel.getRemainGameCount() != null) {
//            tvRemaining.setText(responseModel.getRemainGameCount());
//        }

        if (responseModel.getWinningPoints().equals("0")) {
            Common_Utils.logFirebaseEvent(Craps_Game_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Dice_Roll", "Better Luck");
            showBetterluckPopup("Oops, your attempts are over. Better luck next time!");
        } else {
            Common_Utils.logFirebaseEvent(Craps_Game_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Dice_Roll", "Dice Roll Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());
        }
        isWin = false;
    }

    public void showBetterluckPopup(String message) {
        try {
            final Dialog dilaogBetterluck = new Dialog(Craps_Game_Activity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.dialog_notification);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(Craps_Game_Activity.this, new Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(diceDataModel.getRemainGameCount());

                    if (!Common_Utils.isStringNullOrEmpty(diceDataModel.getRemainGameCount()) && diceDataModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        llPlay.setVisibility(View.GONE);
                        ilAttempt.setVisibility(View.GONE);
                    }

                    setTimer(false);
                }
            });

            if (!isFinishing() && !dilaogBetterluck.isShowing()) {
                dilaogBetterluck.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
/*
    @Override
    public void onBackPressed() {
        try {
            AppLogger.getInstance().d("TAG","gnweklh"+responseModel.getAppLuck());
            if (responseModel != null && responseModel.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(Craps_Game_Activity.this, responseModel.getAppLuck());
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