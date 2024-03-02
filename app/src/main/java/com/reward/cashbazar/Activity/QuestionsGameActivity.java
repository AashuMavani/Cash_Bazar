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
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Get_Quiz_Game_Async;
import com.reward.cashbazar.Async.Models.Quiz_Game_Data_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Store_Quiz_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class QuestionsGameActivity extends AppCompatActivity {
    private LinearLayout layoutPoints, layoutContent, layoutNoData, layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD,layoutAds;
    private ImageView ivHistory, ivImage;
    private TextView tvPoints, lblLoadingAds, tvWinningPoints, tvNote, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private Quiz_Game_Data_Model objQuizDataModel;
    private AppCompatButton btnSubmit;
    private LottieAnimationView ivLottieNoData, ivLottie;
    private boolean isEdit = false;
    private String selectedAnswer = "";
    private RelativeLayout layoutMain;
    private CardView cardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(QuestionsGameActivity.this);
        setContentView(R.layout.activity_question_game);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        setViews();
    }
    private void setViews() {
        cardImage = findViewById(R.id.cardImage);
        ivImage = findViewById(R.id.ivImage);
        ivLottie = findViewById(R.id.ivLottie);
        tvNote = findViewById(R.id.tvNote);
        tvQuestion = findViewById(R.id.tvQuestion);

        layoutOptionA = findViewById(R.id.layoutOptionA);
        layoutOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "A";
                selectAnswer();
            }
        });

        layoutOptionB = findViewById(R.id.layoutOptionB);
        layoutOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "B";
                selectAnswer();
            }
        });

        layoutOptionC = findViewById(R.id.layoutOptionC);
        layoutOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "C";
                selectAnswer();
            }
        });

        layoutOptionD = findViewById(R.id.layoutOptionD);
        layoutOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "D";
                selectAnswer();
            }
        });

        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);

        layoutMain = findViewById(R.id.layoutMain);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //AppLogger.getInstance().e("SELECTED ANSWER", "===" + selectedAnswer);
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        if (selectedAnswer.length() > 0) {
                            Ads_Utils.showAppLovinRewardedAd(QuestionsGameActivity.this, new Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    new Store_Quiz_Async(QuestionsGameActivity.this, objQuizDataModel.getId(), selectedAnswer, objQuizDataModel.getPoints());
                                }
                            });
                        } else {
                            Common_Utils.setToast(QuestionsGameActivity.this, "Please select any answer");
                        }
                    } else {
                        Common_Utils.NotifyLogin(QuestionsGameActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tvNote = findViewById(R.id.tvNote);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        layoutNoData = findViewById(R.id.layoutNoData);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);

        tvWinningPoints = findViewById(R.id.tvWinningPoints);

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

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(QuestionsGameActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(QuestionsGameActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
      //  Common_Utils.startRoundAnimation(QuizGameActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(QuestionsGameActivity.this, QuestionsGameHistoryActivity.class));
                } else {
                    Common_Utils.NotifyLogin(QuestionsGameActivity.this);
                }
            }
        });
        new Get_Quiz_Game_Async(QuestionsGameActivity.this);
    }

    private void selectAnswer() {
        if (selectedAnswer.equalsIgnoreCase("A")) {
            layoutOptionA.setBackgroundResource(R.drawable.selected_answer_bg);
            tvOptionA.setTextColor(getColor(R.color.white));

            layoutOptionB.setBackgroundResource(R.drawable.quiz_background);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.quiz_background);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.quiz_background);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("B")) {
            layoutOptionA.setBackgroundResource(R.drawable.quiz_background);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.selected_answer_bg);
            tvOptionB.setTextColor(getColor(R.color.black));

            layoutOptionC.setBackgroundResource(R.drawable.quiz_background);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.quiz_background);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("C")) {
            layoutOptionA.setBackgroundResource(R.drawable.quiz_background);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.quiz_background);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.selected_answer_bg);
            tvOptionC.setTextColor(getColor(R.color.white));

            layoutOptionD.setBackgroundResource(R.drawable.quiz_background);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("D")) {
            layoutOptionA.setBackgroundResource(R.drawable.quiz_background);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.quiz_background);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.quiz_background);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.selected_answer_bg);
            tvOptionD.setTextColor(getColor(R.color.white));
        } else {
            layoutOptionA.setBackgroundResource(R.drawable.quiz_background);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.quiz_background);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.quiz_background);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.quiz_background);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        }
        btnSubmit.setEnabled(isButtonEnabled());
    }

    public void updateDataChanges(Quiz_Game_Data_Model responseModel) {
        try {
            if (!isEdit) {
                if (objQuizDataModel.getIsInstantQuiz().equals("1")) {
                    showWinPopup(objQuizDataModel.getPoints());
                } else {
                    isEdit = true;
                    btnSubmit.setText("Update");
                    Common_Utils.NotifySuccess(QuestionsGameActivity.this, "Submit Quiz", responseModel.getMessage(), false);
                }
                Common_Utils.logFirebaseEvent(QuestionsGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Quiz", "Submit");
            } else {
                Common_Utils.NotifySuccess(QuestionsGameActivity.this, "Update Quiz Answer", responseModel.getMessage(), false);
            }
            btnSubmit.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(QuestionsGameActivity.this, android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinInterstitialAd(QuestionsGameActivity.this, new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinRewardedAd(QuestionsGameActivity.this, new Ads_Utils.AdShownListener() {
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
                    Common_Utils.GetCoinAnimation(QuestionsGameActivity.this, layoutMain, layoutPoints);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), QuestionsGameActivity.this);
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

    public void setData(Quiz_Game_Data_Model responseModel) {
        objQuizDataModel = responseModel;
        try {
            if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            }
            if (objQuizDataModel.getStatus().equals("2")) {
                layoutContent.setVisibility(GONE);
                layoutNoData.setVisibility(VISIBLE);
                ivLottieNoData.playAnimation();
                Ads_Utils.showAppLovinRewardedAd(QuestionsGameActivity.this, null);
            } else {
                layoutContent.setVisibility(VISIBLE);
                layoutNoData.setVisibility(GONE);

                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getIsTodayTaskCompleted()) && objQuizDataModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);
                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(objQuizDataModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if(!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskButton())){
                            btnCompleteTask.setText(objQuizDataModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getScreenNo())) {
//                                    if (!POC_Common_Utils.hasUsageAccessPermission(QuestionsGameActivity.this)) {
//                                        POC_Common_Utils.showUsageAccessPermissionDialog(QuestionsGameActivity.this);
//                                        return;
//                                    } else {
                                        Common_Utils.Redirect(QuestionsGameActivity.this, objQuizDataModel.getScreenNo(), "", "", "", "", "");
//                                    }
                                } else if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskId())) {
                                    Intent intent = new Intent(QuestionsGameActivity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", objQuizDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(QuestionsGameActivity.this, TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", POC_Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });
                        /*btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskId())) {
                                    Intent intent = new Intent(QuizGameActivity.this, TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objQuizDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(QuizGameActivity.this, TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                            }
                        });*/
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvWinningPoints.setText(objQuizDataModel.getPoints());

                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getImage())) {
                    cardImage.setVisibility(VISIBLE);
                    if (objQuizDataModel.getImage().contains(".json")) {
                        ivImage.setVisibility(View.GONE);
                        ivLottie.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(ivLottie, objQuizDataModel.getImage());
                        ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    } else {
                        ivLottie.setVisibility(View.GONE);
                        ivImage.setVisibility(View.VISIBLE);
                        Glide.with(QuestionsGameActivity.this)
                                .load(objQuizDataModel.getImage())
                                .into(ivImage);
                    }
                } else {
                    cardImage.setVisibility(GONE);
                }

                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getNote())) {
                    tvNote.setText("NOTE: " + objQuizDataModel.getNote());
                }

                tvQuestion.setText(objQuizDataModel.getQuestion());

                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionA())) {
                    layoutOptionA.setVisibility(VISIBLE);
                    tvOptionA.setText(objQuizDataModel.getOptionA());
                } else {
                    layoutOptionA.setVisibility(GONE);
                }
                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionB())) {
                    layoutOptionB.setVisibility(VISIBLE);
                    tvOptionB.setText(objQuizDataModel.getOptionB());
                } else {
                    layoutOptionB.setVisibility(GONE);
                }
                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionC())) {
                    layoutOptionC.setVisibility(VISIBLE);
                    tvOptionC.setText(objQuizDataModel.getOptionC());
                } else {
                    layoutOptionC.setVisibility(GONE);
                }
                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionD())) {
                    layoutOptionD.setVisibility(VISIBLE);
                    tvOptionD.setText(objQuizDataModel.getOptionD());
                } else {
                    layoutOptionD.setVisibility(GONE);
                }
                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getUserAnswer())) {
                    selectedAnswer = objQuizDataModel.getUserAnswer();
                    isEdit = true;
                    btnSubmit.setText("Update");
                } else {
                    selectedAnswer = "";
                }

                selectAnswer();

                // Load home note webview top
                try {
                    if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, objQuizDataModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load top ad
                try {
                    if (objQuizDataModel.getTopAds() != null && !Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        Common_Utils.loadTopBannerAd(QuestionsGameActivity.this, layoutTopAds, objQuizDataModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!Common_Utils.isStringNullOrEmpty(selectedAnswer)) {
                    Ads_Utils.showAppLovinInterstitialAd(QuestionsGameActivity.this, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isButtonEnabled() {
        if (isEdit) {
            return (!selectedAnswer.equals(objQuizDataModel.getUserAnswer()));
        } else {
            return (!Common_Utils.isStringNullOrEmpty(selectedAnswer));
        }
    }
/*
    @Override
    public void onBackPressed() {
        try {
            if (objQuizDataModel != null && objQuizDataModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(QuestionsGameActivity.this, objQuizDataModel.getAppLuck());
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), QuestionsGameActivity.this);
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