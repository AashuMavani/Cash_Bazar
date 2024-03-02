package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
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
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.MinesweeperAdapter;
import com.reward.cashbazar.Async.Async_Minesweeper_Get;
import com.reward.cashbazar.Async.Async_Minesweeper_save;
import com.reward.cashbazar.Async.Models.Model_MinesweeperData;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class Bombsweeper_Activity extends AppCompatActivity {
    public static boolean isVisible = false;
    private ImageView iv_left, ivHistory, ivHelp;
    private LinearLayout layoutPoints1;
    public Response_Model responseMain;
    public RelativeLayout layoutMain;
    MediaPlayer bombvoice;
    MinesweeperAdapter mineadp;
    int numberText = 0;
    private TextView tvDailyPuzzle, tvRemainPuzzle;
    private MaxAd nativeAdWin;
    private MaxNativeAdLoader nativeAdLoaderWin;
    private LinearLayout layoutRemainingTime;
    private TextView tvRemainingTime, tvWinningPoints;
    private boolean isTimerSet = false;
    private String gameTime;
    private int time;
    private CountDownTimer timer;
    private String todayDate;
    private String lastDate;
    private TextView tvPoints, lblLoadingAds;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd, frameLovinBanner;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    private TextView lblFindAll;
    private RecyclerView listdatagame;
    private Model_MinesweeperData objMinesweeperModel;
    private boolean isClick = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Bombsweeper_Activity.this);
        setContentView(R.layout.activity_bombsweeper);
        isVisible = false;
        initview();
        onClick();

    }


    private void initview() {
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        iv_left = findViewById(R.id.iv_left);
        layoutPoints1 = findViewById(R.id.layoutPoints1);
        ivHistory = findViewById(R.id.ivHistory);

        frameLovinBanner = findViewById(R.id.frameLovinBanner);
        ivHelp = findViewById(R.id.ivHelp);
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        lblFindAll = findViewById(R.id.lblFindAll);
        layoutMain = findViewById(R.id.layoutMain);


        tvDailyPuzzle = findViewById(R.id.tvDailyPuzzle);
        tvRemainPuzzle = findViewById(R.id.tvRemainPuzzle);

        layoutRemainingTime = findViewById(R.id.layoutRemainingTime);
        tvRemainingTime = findViewById(R.id.tvRemainingTime);

        listdatagame = findViewById(R.id.listdatagame);
        tvPoints = findViewById(R.id.tvPoints);
        tvWinningPoints = findViewById(R.id.tvWinningPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

    }

    private void onClick() {
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        layoutPoints1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Bombsweeper_Activity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Bombsweeper_Activity.this);
                }
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Bombsweeper_Activity.this, PointDetailsActivity.class).putExtra("type", POC_Constants.HistoryType.MINE_SWEEPER).putExtra("title", "Bombsweeper History"));
                } else {
                    Common_Utils.NotifyLogin(Bombsweeper_Activity.this);
                }
            }
        });


       /* ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Minesweeper_Activity.this, EarnedPointHistoryActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.MINE_SWEEPER)
                            .putExtra("title", "Minesweeper History"));
                } else {
                    POC_Common_Utils.NotifyLogin(Minesweeper_Activity.this);
                }
            }
        });*/
        new Async_Minesweeper_Get(Bombsweeper_Activity.this);
    }

    public void setData(Model_MinesweeperData responseModel) {
        objMinesweeperModel = responseModel;

        if (responseModel.getStatus().equals("2")) {
            showLimitOverView();

            Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, null);
        } else {

            if ((!Common_Utils.isStringNullOrEmpty(responseModel.getTotalGameCount()) && responseModel.getTotalGameCount().equals("0")) || (!Common_Utils.isStringNullOrEmpty(responseModel.getRemainGameCount()) && responseModel.getRemainGameCount().equals("0"))) {
                showLimitOverView();
                Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, null);
            } else {

                isVisible = false;
                isClick = true;
                if (responseModel.getPoints() != null) {
                    tvWinningPoints.setText(responseModel.getPoints());
                }
                setAd(responseModel.getAdType());
                if (responseModel.getMainNote() != null) {
                    lblFindAll.setText(responseModel.getMainNote());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvRemainPuzzle.setText(responseModel.getRemainGameCount());
                }
                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastDate() != null) {
                    lastDate = responseModel.getLastDate();
                }
                if (responseModel.getTotalGameCount() != null) {
                    tvDailyPuzzle.setText(responseModel.getTotalGameCount());
                }

                if (!Common_Utils.isStringNullOrEmpty(responseModel.getNextGameTimer())) {
                    gameTime = responseModel.getNextGameTimer();
                }
                setTimer(true);

                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!isTimerSet && !Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);

                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(responseModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskButton())) {
                            btnCompleteTask.setText(responseModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskId())) {
                                    Intent intent = new Intent(Bombsweeper_Activity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", responseModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Bombsweeper_Activity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskTypeId", POC_Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
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
                        Common_Utils.loadTopBannerAd(Bombsweeper_Activity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
                    ivHelp.setVisibility(VISIBLE);
                    ivHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common_Utils.openUrl(Bombsweeper_Activity.this, responseModel.getHelpVideoUrl());
                        }
                    });
                }
            }
        }

        setMinesData();




    }

    private void setMinesData() {
        if (listdatagame != null) {
            listdatagame.removeAllViews();
        }
        mineadp = new MinesweeperAdapter(objMinesweeperModel.getData(), Bombsweeper_Activity.this, new MinesweeperAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, ImageView ivimgbox, LottieAnimationView ivimgboxlottie) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    if (v.isEnabled()) {
                        if (isClick) {
                            v.setEnabled(false);

                            if (objMinesweeperModel.getData().get(position).getIcon().contains(".json")) {
                                ivimgbox.setVisibility(View.INVISIBLE);
                                ivimgboxlottie.setVisibility(View.VISIBLE);
                                ivimgboxlottie.setMinAndMaxFrame(30, 70);
                                Common_Utils.setLottieAnimation(ivimgboxlottie, objMinesweeperModel.getData().get(position).getIcon());
                                ivimgboxlottie.playAnimation();
                            } else {
                                ivimgboxlottie.setVisibility(View.INVISIBLE);
                                ivimgbox.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(objMinesweeperModel.getData().get(position).getIcon())
                                        .into(ivimgbox);
                            }

                            if (objMinesweeperModel.getData().get(position).getCount() != null) {
                                if (objMinesweeperModel.getData().get(position).getCount().equals("b")) {

                                    isClick = false;
                                    isVisible = true;
                                    mineadp.notifyDataSetChanged();
                                    bombvoice = MediaPlayer.create(Bombsweeper_Activity.this, R.raw.bombvoice);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            bombvoice.start();
                                        }
                                    }, 800);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (numberText <= 0) {
                                                new Async_Minesweeper_save(Bombsweeper_Activity.this, "0");
                                            } else {
                                                new Async_Minesweeper_save(Bombsweeper_Activity.this, String.valueOf(numberText));
                                            }
                                        }
                                    }, 1500);
                                } else {
                                    numberText += Integer.parseInt(objMinesweeperModel.getData().get(position).getCount());
                                    tvWinningPoints.setText(numberText + "");
                                    objMinesweeperModel.getData().get(position).setShown(true);
                                    mineadp.notifyItemChanged(position);
                                }
                            }
                        }
                    }
                } else {
                    Common_Utils.NotifyLogin(Bombsweeper_Activity.this);
                }

            }
        });
        listdatagame.setLayoutManager(new GridLayoutManager(Bombsweeper_Activity.this, 6));
        listdatagame.setAdapter(mineadp);
    }

    private void showLimitOverView() {
        setAd(objMinesweeperModel.getAdType());
        tvRemainPuzzle.setText(objMinesweeperModel.getRemainGameCount());
        tvDailyPuzzle.setText(objMinesweeperModel.getTotalGameCount());
        layoutRemainingTime.setVisibility(VISIBLE);
        TextView lblTimer = findViewById(R.id.lblTimer);
        lblTimer.setText("You have exhausted today's  Game limit, please try again tomorrow.");
        tvRemainingTime.setVisibility(GONE);
    }

    public void setAd(String adType) {
        if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1")) {
            if (adType != null) {
                if (adType.equals("1")) {
                    frameLovinBanner.setVisibility(GONE);
                    layoutAds.setVisibility(VISIBLE);
                    loadAppLovinNativeAds(false);
                } else if (adType.equals("2")) {
                    layoutAds.setVisibility(VISIBLE);
                    frameLovinBanner.setVisibility(GONE);
                    loadAppLovinNativeAds(true);
                } else if (adType.equals("3")) {
                    layoutAds.setVisibility(View.GONE);
                    frameLovinBanner.setVisibility(View.VISIBLE);
                    loadBannerAds(Bombsweeper_Activity.this, frameLovinBanner);
                } else {
                    loadAppLovinNativeAds(false);
                }
            } else {
                loadAppLovinNativeAds(false);
            }
        }
    }

    private void loadBannerAds(Activity c, FrameLayout layoutBannerAd) {
        try {
            if (Common_Utils.isShowAppLovinBannerAds()) {
                MaxAdView adView = new MaxAdView(Common_Utils.getRandomAdUnitId(new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getLovinBannerID()), c);
                adView.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdLoaded==", "===" + ad);
                        layoutBannerAd.removeAllViews();
                        layoutBannerAd.addView(adView);
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdDisplayed==", "===" + ad);
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdLoadFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdDisplayFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }
                });
                adView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, c.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height)));
                adView.loadAd();
            } else {
                layoutBannerAd.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            layoutBannerAd.setVisibility(View.GONE);
        }
    }

    public void updateDataChanges(Model_MinesweeperData responseModel) {
        objMinesweeperModel = responseModel;
        if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        }
        if (!Common_Utils.isStringNullOrEmpty(responseModel.getNextGameTimer())) {
            gameTime = responseModel.getNextGameTimer();
        }
        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }

        if (!Common_Utils.isStringNullOrEmpty(responseModel.getWinningPoints())) {
            if (responseModel.getWinningPoints().equals("0")) {
                Common_Utils.logFirebaseEvent(Bombsweeper_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Minesweeper_Game", "Minesweeper Game - Better Luck");
                showBetterluckPopup();
            } else {
                Common_Utils.logFirebaseEvent(Bombsweeper_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Minesweeper_Game", "Minesweeper Game - Win");
                showWinPopup(responseModel.getWinningPoints());
            }
        }
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Bombsweeper_Activity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, new Ads_Utils.AdShownListener() {
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
                    Common_Utils.GetCoinAnimation(Bombsweeper_Activity.this, layoutMain, layoutPoints1);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    if (!point.equals("0")) {
                        Common_Utils.GetCoinAnimation(Bombsweeper_Activity.this, layoutMain, layoutPoints1);
                    }
                    tvPoints.setText(objMinesweeperModel.getEarningPoint());
                    if (!Common_Utils.isStringNullOrEmpty(objMinesweeperModel.getRemainGameCount()) && objMinesweeperModel.getRemainGameCount().equals("0")) {
                        showLimitOverView();
                    } else {
                        tvWinningPoints.setText(numberText+"");
                        setTimer(false);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Bombsweeper_Activity.this);
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
//                    Logger_App.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
//                    Logger_App.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
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
        Dialog dilaogBetterluck = new Dialog(Bombsweeper_Activity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.dialog_better_luck_next_time);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText("Oops! Game is over. Better luck next time!");

        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, new Ads_Utils.AdShownListener() {
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
                if (!Common_Utils.isStringNullOrEmpty(objMinesweeperModel.getRemainGameCount()) && objMinesweeperModel.getRemainGameCount().equals("0")) {
                    showLimitOverView();
                } else {
                    tvWinningPoints.setText(numberText+"");
                    setTimer(false);
                }
            }
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    public void setTimer(boolean isFromOnCreate) {
        if (Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(gameTime)) {
            isTimerSet = false;
        } else {
            isTimerSet = true;
            layoutRemainingTime.setVisibility(VISIBLE);
            if (timer != null) {
                timer.cancel();
            }
            time = Common_Utils.timeDiff(todayDate, lastDate);
            timer = new CountDownTimer((Integer.parseInt(gameTime) - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRemainingTime.setText(Common_Utils.updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    layoutRemainingTime.setVisibility(GONE);
                    numberText = 0;
                    isClick = true;
                    isVisible = false;
                    setMinesData();
                    setAd(objMinesweeperModel.getAdType());
                    tvRemainPuzzle.setText(objMinesweeperModel.getRemainGameCount());
                }
            }.start();
            if (isFromOnCreate) {
                Ads_Utils.showAppLovinInterstitialAd(Bombsweeper_Activity.this, null);
            }
        }
    }

    private void loadAppLovinNativeAds(boolean isSmall) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(isSmall ? Common_Utils.getRandomAdUnitId(responseMain.getLovinSmallNativeID()) : Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Bombsweeper_Activity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = isSmall ? getResources().getDimensionPixelSize(R.dimen.dim_150) : getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutNativeAd.addView(nativeAdView);
                    lblLoadingAds.setVisibility(View.GONE);
                    //Logger_App.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    //Logger_App.getInstance().e("AppLovin Failed: ", error.getMessage());
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

  /*  @Override
    public void onBackPressed() {
        try {
            if (objMinesweeperModel != null && objMinesweeperModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(Bombsweeper_Activity.this, objMinesweeperModel.getAppLuck());
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
