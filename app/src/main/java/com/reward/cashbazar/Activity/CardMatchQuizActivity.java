package com.reward.cashbazar.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.reward.cashbazar.utils.Common_Utils.convertTimeInMillis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.CardMatchQuizAdapter;
import com.reward.cashbazar.Async.Get_Cards_Async;
import com.reward.cashbazar.Async.Models.Cards_Menu;
import com.reward.cashbazar.Async.Models.Cards_model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Story_Cards_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.Locale;

public class CardMatchQuizActivity extends AppCompatActivity {

    private Response_Model responseMain;
    private RecyclerView rvCards;
    private ImageView ivHistory, ivBack;

    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private String todayDate, lastDate;
    int nextGameTime;
    private boolean isTimerOn = false;
    private long lastClickTime = 0;

    Cards_model cardsModel;
    boolean isWrongSelect = false;

    private CardMatchQuizAdapter cardMatchAdapter;

    private CountDownTimer timer;
    int time;
    String firstValue = "0";
    int wrong;

    int pair = 0;


    private LottieAnimationView ivLottieNoData;

    private LinearLayout layoutAds, llLimit, layoutPoints;
    private TextView  tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvNote, tvTimeUp, tvWrongMoves;

    private RelativeLayout ilAttempt, layoutMain;

    private ArrayList<Cards_Menu> data = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Common_Utils.setDayNightTheme(CardMatchQuizActivity.this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_card_match_quiz);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        tvRemaining = findViewById(R.id.tvRemaining);
        rvCards = findViewById(R.id.rvCards);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        llLimit = findViewById(R.id.llLimit);
        ilAttempt = findViewById(R.id.ilAttempt);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutMain = findViewById(R.id.layoutMain);
        layoutAds = findViewById(R.id.layoutAds);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        ivHistory = findViewById(R.id.ivHistory);
        tvWrongMoves = findViewById(R.id.tvWrongMoves);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);

        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(CardMatchQuizActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(CardMatchQuizActivity.this);
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
            layoutAds.setVisibility(GONE);
        }
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(CardMatchQuizActivity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.Cards)
                            .putExtra("title", "Match Cards History"));
                } else {
                    Common_Utils.NotifyLogin(CardMatchQuizActivity.this);
                }
            }
        });

        new Get_Cards_Async(CardMatchQuizActivity.this);

    }
private Cards_model responseModel;
    public void setData(Cards_model responseModel1) {
        responseModel = responseModel1;
        try {
            if (responseModel.getStatus().equals("2")) {
                Ads_Utils.showAppLovinInterstitialAd(CardMatchQuizActivity.this, null);

                cardMatchAdapter = new CardMatchQuizAdapter(responseModel.getData(), CardMatchQuizActivity.this, new CardMatchQuizAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v, ImageView imageView) {

                    }
                });
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(CardMatchQuizActivity.this, 6);
                rvCards.setLayoutManager(mGridLayoutManager);
                rvCards.setAdapter(cardMatchAdapter);
                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
                if (responseModel.getWrongMoves() != null) {
                    wrong = Integer.parseInt(responseModel.getWrongMoves());
                    tvWrongMoves.setText(String.valueOf(wrong));
                }
                if (responseModel.getRemainGameCount() != null) {
                    tvRemaining.setText(responseModel.getRemainGameCount());
                }
                llLimit.setVisibility(VISIBLE);
                tvTimeUp.setVisibility(View.GONE);
                tvNote.setText("You have exhausted today's Cards Game limit, please try again tomorrow.");

            } else {
                cardsModel = responseModel;
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                }
                try {
                    if (!Common_Utils.isStringNullOrEmpty(cardsModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, cardsModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!Common_Utils.isStringNullOrEmpty(cardsModel.getIsTodayTaskCompleted()) && cardsModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(cardsModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!Common_Utils.isStringNullOrEmpty(cardsModel.getTaskButton())) {
                            btnCompleteTask.setText(cardsModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!Common_Utils.isStringNullOrEmpty(cardsModel.getScreenNo())) {
//                                    if (!POC_Common_Utils.hasUsageAccessPermission(CardMatchQuizActivity.this)) {
//                                        POC_Common_Utils.showUsageAccessPermissionDialog(CardMatchQuizActivity.this);
//                                        return;
//                                    } else {
                                        Common_Utils.Redirect(CardMatchQuizActivity.this, cardsModel.getScreenNo(), "", "", "", "", "");
//                                    }
                                } else if (!Common_Utils.isStringNullOrEmpty(cardsModel.getTaskId())) {
                                    Intent intent = new Intent(CardMatchQuizActivity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", cardsModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(CardMatchQuizActivity.this, TasksCategoryTypeActivity.class);
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


                if (responseModel.getWrongMoves() != null) {
                    wrong = Integer.parseInt(responseModel.getWrongMoves());
                    tvWrongMoves.setText(String.valueOf(wrong));
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

                if (responseModel.getRemainGameCount() != null) {
                    tvRemaining.setText(responseModel.getRemainGameCount());
                }

                data.addAll(responseModel.getData());

                setTimer1(true);
                cardMatchAdapter = new CardMatchQuizAdapter(responseModel.getData(), CardMatchQuizActivity.this, new CardMatchQuizAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v, ImageView imageView) {

                        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                            return;
                        }

                        lastClickTime = SystemClock.elapsedRealtime();
                        if (v.isEnabled() && !tvWrongMoves.getText().toString().equals("0")) {
                            v.setEnabled(false);
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {

                                if (!isTimerOn) {
                                    Glide.with(CardMatchQuizActivity.this)
                                            .load(data.get(position).getImg())
                                            .into(imageView);

                                    if (!firstValue.matches("0")) {
                                        if (firstValue.equals(responseModel.getData().get(position).getValue())) {
                                            //Toast.makeText(CardMatchActivity.this, "matchess", Toast.LENGTH_SHORT).show();
                                            //showResult();
                                            pair++;
                                            firstValue = "0";
                                            if (pair == 12) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new Story_Cards_Async(CardMatchQuizActivity.this, responseModel.getPoints());
                                                    }
                                                }, 500);
                                            }

                                            return;
                                        } else {

                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0f);
                                                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f);
                                                    oa1.setInterpolator(new DecelerateInterpolator());
                                                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                                                    oa1.addListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            imageView.setImageResource(R.drawable.card_game);
                                                            imageView.setAdjustViewBounds(true);
                                                            oa2.start();
                                                        }
                                                    });
                                                    v.setEnabled(true);
                                                    oa1.start();
                                                    wrong--;
                                                    tvWrongMoves.setText(String.valueOf(wrong));

                                                    if (tvWrongMoves.getText().equals("0")) {
                                                        isWrongSelect = true;
                                                        isTimerOn = true;
                                                        new Story_Cards_Async(CardMatchQuizActivity.this, "0");

                                                        v.setEnabled(false);
                                                        wrong = Integer.parseInt(responseModel.getWrongMoves());

                                                    }

                                                }
                                            }, 1000);

                                            // Collections.shuffle(data);
                                            //Toast.makeText(CardMatchActivity.this, "not match", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (firstValue.matches("0")) {
                                        firstValue = (responseModel.getData().get(position).getValue());
                                    }
                                }
                            } else {
                                Common_Utils.NotifyLogin(CardMatchQuizActivity.this);
                            }
                        }


                    }
                });
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(CardMatchQuizActivity.this, 6);
                rvCards.setLayoutManager(mGridLayoutManager);
                rvCards.setAdapter(cardMatchAdapter);
                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
            }
        } catch (Exception e) {

        }


    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), CardMatchQuizActivity.this);
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

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(CardMatchQuizActivity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(CardMatchQuizActivity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinInterstitialAd(CardMatchQuizActivity.this, new Ads_Utils.AdShownListener() {
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

                    tvRemaining.setText(cardsModel.getRemainGameCount());
                    tvWrongMoves.setText(String.valueOf(wrong));

                    llLimit.setVisibility(View.GONE);
                    firstValue = "0";
                    pair = 0;
                    wrong = Integer.parseInt(cardsModel.getWrongMoves());

                    data.addAll(cardsModel.getData());
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(CardMatchQuizActivity.this, 6);
                    rvCards.setLayoutManager(mGridLayoutManager);
                    rvCards.setAdapter(cardMatchAdapter);


                    Common_Utils.GetCoinAnimation(CardMatchQuizActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                    llLimit.setVisibility(VISIBLE);
                    if (!Common_Utils.isStringNullOrEmpty(cardsModel.getRemainGameCount()) && cardsModel.getRemainGameCount().equals("0")) {
                        tvNote.setText("You have exhausted today's Cards Game limit, please try again tomorrow.");
                        tvTimeUp.setVisibility(GONE);
                    } else {
                        tvNote.setText("Card Match Game will be unlocked in");
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
            final Dialog dilaogBetterluck = new Dialog(CardMatchQuizActivity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.dialog_notification);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(CardMatchQuizActivity.this, new Ads_Utils.AdShownListener() {
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

                    tvRemaining.setText(cardsModel.getRemainGameCount());
                    tvWrongMoves.setText(String.valueOf(wrong));
                    llLimit.setVisibility(View.GONE);
                    firstValue = "0";
                    pair = 0;
                    wrong = Integer.parseInt(cardsModel.getWrongMoves());

                    data.addAll(cardsModel.getData());
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(CardMatchQuizActivity.this, 6);
                    rvCards.setLayoutManager(mGridLayoutManager);
                    rvCards.setAdapter(cardMatchAdapter);

                    if (!Common_Utils.isStringNullOrEmpty(cardsModel.getRemainGameCount()) && cardsModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        isTimerOn = true;
                        tvTimeUp.setVisibility(GONE);
                        tvNote.setText("You have exhausted today's Cards Game limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Card Match Game will be unlocked in");
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), CardMatchQuizActivity.this);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), CardMatchQuizActivity.this);
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


    public void setTimer1(boolean isFromOnCreate) {

        if (timeDiff(todayDate, lastDate) > nextGameTime) {

        } else {
            isTimerOn = true;
            tvTimeUp.setText("Card Match Game will be unlocked in");
            llLimit.setVisibility(VISIBLE);

            if (timer != null) {
                timer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            timer = new CountDownTimer((nextGameTime - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerOn = true;
                    tvTimeUp.setText(updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    isTimerOn = false;
                    llLimit.setVisibility(View.GONE);
                    firstValue = "0";
                    pair = 0;
                    wrong = Integer.parseInt(cardsModel.getWrongMoves());

                    data.addAll(cardsModel.getData());
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(CardMatchQuizActivity.this, 6);
                    rvCards.setLayoutManager(mGridLayoutManager);
                    rvCards.setAdapter(cardMatchAdapter);
                }
            }.start();
            if (isFromOnCreate) {
                Ads_Utils.showAppLovinInterstitialAd(CardMatchQuizActivity.this, null);
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

    public void changeCountDataValues(Cards_model responseModel) {
        cardsModel = responseModel;
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (responseModel.getWrongMoves() != null) {
            wrong = Integer.parseInt(responseModel.getWrongMoves());

        }
        if (responseModel.getNextGameTime() != null) {
            nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
        }
//        if (cardsModel.getRemainGameCount() != null) {
//            tvRemaining.setText(cardsModel.getRemainGameCount());
//        }
        if (responseModel.getWinningPoints().equals("0")) {
            Common_Utils.logFirebaseEvent(CardMatchQuizActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Card_Match", "Better Luck");
            if (isWrongSelect) {
                showBetterluckPopup("Oops, you are out of move. Better luck next time!");
            } else {
                showBetterluckPopup("Oops, time is over. Better luck, next time!");
            }
        } else {
            Common_Utils.logFirebaseEvent(CardMatchQuizActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Card_Match", "Card Match Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());
        }
        isWrongSelect=false;
    }
   /* @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null ) {
                Common_Utils.dialogShowAppLuck(CardMatchQuizActivity.this, responseModel.getAppLuck());
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