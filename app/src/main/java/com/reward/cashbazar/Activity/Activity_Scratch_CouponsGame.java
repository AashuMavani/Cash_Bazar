package com.reward.cashbazar.Activity;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Scratch_Ticket_Game_List_Adapter;
import com.reward.cashbazar.Async.Get_Scratch_Ticket_List_Async;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Scratch_Ticket_List;
import com.reward.cashbazar.Async.Models.Scratch_Ticket_Model;
import com.reward.cashbazar.Async.Store_Scratch_Card_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

import in.myinnos.androidscratchcard.ScratchCard;

public class Activity_Scratch_CouponsGame extends AppCompatActivity {
    private RecyclerView rvScratchCardList;
    private TextView tvPoints, lblLoadingAds, tvTimer;
    private LottieAnimationView ivLottieNoData;
    private Response_Model responseMain;
    private LinearLayout layoutPoints;
    private CountDownTimer timer;
    private int time, selectedPos = -1;
    private String todayDate, lastDate, scratchTime;
    private Scratch_Ticket_Model objScratchCardModel;
    private ImageView ivHistory, parentBackgroundImage;
    private MaxAd nativeAd, nativeAdWin, nativeAdScratchDialog;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderScratchDialog;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private RelativeLayout layoutMain;
    private List<Scratch_Ticket_List> listScratchCards = new ArrayList<>();
    private Scratch_Ticket_Game_List_Adapter adapter;
    private boolean isTimerSet = false;
    private Dialog dialogScratchCard;
    private ScratchCard scratchCard;
    private ImageView ivFrontImage, ivHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Activity_Scratch_CouponsGame.this);
        setContentView(R.layout.activity_scratch_coupons);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        ivHelp = findViewById(R.id.ivHelp);
        rvScratchCardList = findViewById(R.id.rvScratchCardList);
        layoutMain = findViewById(R.id.layoutMain);
        ivHistory = findViewById(R.id.ivHistory);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Activity_Scratch_CouponsGame.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(Activity_Scratch_CouponsGame.this);
                }
            }
        });
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        parentBackgroundImage = findViewById(R.id.parentBackgroundImage);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Activity_Scratch_CouponsGame.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.SCRATCH_CARD)
                            .putExtra("title", "Scratch Coupons History"));
                } else {
                    Common_Utils.NotifyLogin(Activity_Scratch_CouponsGame.this);
                }
            }
        });
        new Get_Scratch_Ticket_List_Async(Activity_Scratch_CouponsGame.this);
    }
/*    @Override
    public void onBackPressed() {
        try {
            if (objScratchCardModel != null && objScratchCardModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(Activity_Scratch_CouponsGame.this, objScratchCardModel.getAppLuck());
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

    public void changeScratchCardDataValues(Scratch_Ticket_Model responseModel) {
        try {
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastScratchedDate() != null) {
                lastDate = responseModel.getLastScratchedDate();
            }
            if (responseModel.getScratchTime() != null) {
                scratchTime = responseModel.getScratchTime();
            }

            Common_Utils.logFirebaseEvent(Activity_Scratch_CouponsGame.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Scratch_Card", "Scratch Card Got Reward");
            showWinPopup(listScratchCards.get(selectedPos).getScratchCardPoints());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinPopup(String point) {
        Dialog dialogWin = new Dialog(Activity_Scratch_CouponsGame.this, android.R.style.Theme_Light);
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
                if (dialogWin != null) {
                    dialogWin.dismiss();
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
            if (dialogWin != null) {
                dialogWin.dismiss();
            }
        });
        dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dialogScratchCard != null && dialogScratchCard.isShowing()) {
                    dialogScratchCard.dismiss();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Common_Utils.GetCoinAnimation(Activity_Scratch_CouponsGame.this, layoutMain, layoutPoints);
                            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                            listScratchCards.get(selectedPos).setIsScratched("1");
                            adapter.notifyItemChanged(selectedPos);
                            selectedPos = -1;
                            setTimer(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 200);
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Activity_Scratch_CouponsGame.this);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Activity_Scratch_CouponsGame.this);
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

    private void loadAppLovinNativeAdsScratchDialog(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderScratchDialog = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Activity_Scratch_CouponsGame.this);
            nativeAdLoaderScratchDialog.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdScratchDialog != null) {
                        nativeAdLoaderScratchDialog.destroy(nativeAdScratchDialog);
                    }
                    nativeAdScratchDialog = ad;
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
            nativeAdLoaderScratchDialog.loadAd();
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
                }
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (nativeAdScratchDialog != null && nativeAdLoaderScratchDialog != null) {
                    nativeAdLoaderScratchDialog.destroy(nativeAdScratchDialog);
                    nativeAdScratchDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setData(Scratch_Ticket_Model responseModel) {
        objScratchCardModel = responseModel;
        if (!Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
            ivHelp.setVisibility(VISIBLE);
            ivHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common_Utils.openUrl(Activity_Scratch_CouponsGame.this, responseModel.getHelpVideoUrl());
                }
            });
        }
        if (responseModel.getScratchCardList() != null && responseModel.getScratchCardList().size() > 0) {
            try {
                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastScratchedDate() != null) {
                    lastDate = responseModel.getLastScratchedDate();
                }

                if (responseModel.getScratchTime() != null) {
                    scratchTime = responseModel.getScratchTime();
                }
                Glide.with(Activity_Scratch_CouponsGame.this).load(responseModel.getBackgroundImage()).into(parentBackgroundImage);

                listScratchCards.addAll(responseModel.getScratchCardList());
                if (Common_Utils.isShowAppLovinNativeAds()) {
                    if (listScratchCards.size() <= 4) {
                        listScratchCards.add(listScratchCards.size(), new Scratch_Ticket_List());
                    } else {
                        for (int i2 = 0; i2 < this.listScratchCards.size(); i2++) {
                            if ((i2 + 1) % 5 == 0) {
                                //AppLogger.getInstance().e("POSITION AD", "==================" + i2);
                                listScratchCards.add(i2, new Scratch_Ticket_List());
                            }
                        }
                    }
                }
                adapter = new Scratch_Ticket_Game_List_Adapter(listScratchCards, Activity_Scratch_CouponsGame.this, responseModel.getBackImage(), responseModel.getFrontImage(), new Scratch_Ticket_Game_List_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        try {
                            if (listScratchCards.get(position).getIsScratched() != null && listScratchCards.get(position).getIsScratched().equals("0")) {
                                selectedPos = position;
                                showScratchCardDialog();
                            } else {
                                Ads_Utils.showAppLovinInterstitialAd(Activity_Scratch_CouponsGame.this, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                GridLayoutManager mGridLayoutManager = new GridLayoutManager(Activity_Scratch_CouponsGame.this, 2);
                mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (adapter.getItemViewType(position) == Scratch_Ticket_Game_List_Adapter.ITEM_AD) {
                            return 2;
                        }
                        return 1;
                    }
                });
                rvScratchCardList.setLayoutManager(mGridLayoutManager);
                rvScratchCardList.setAdapter(adapter);

                boolean isAllCardsScratched = true;
                for (int i = 0; i < listScratchCards.size(); i++) {
                    if (listScratchCards.get(i).getIsScratched() != null && listScratchCards.get(i).getIsScratched().equals("0")) {
                        isAllCardsScratched = false;
                        break;
                    }
                }
                if (!isAllCardsScratched) {
                    setTimer(true);
                } else {
                    Ads_Utils.showAppLovinInterstitialAd(Activity_Scratch_CouponsGame.this, null);
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
                    Common_Utils.loadTopBannerAd(Activity_Scratch_CouponsGame.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ivLottieNoData = findViewById(R.id.ivLottieNoData);
            ivLottieNoData.setVisibility(View.VISIBLE);
            ivLottieNoData.playAnimation();
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

    }

    private void showScratchCardDialog() {
        try {
            dialogScratchCard = new Dialog(Activity_Scratch_CouponsGame.this, android.R.style.Theme_Light);
            dialogScratchCard.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogScratchCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogScratchCard.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogScratchCard.setContentView(R.layout.dialog_scratch_ticket);
            dialogScratchCard.setCancelable(true);

            FrameLayout fl_adplaceholder = dialogScratchCard.findViewById(R.id.fl_adplaceholder);
            TextView lblLoadingAds = dialogScratchCard.findViewById(R.id.lblLoadingAds);
            if (Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAdsScratchDialog(fl_adplaceholder, lblLoadingAds);
            } else {
                lblLoadingAds.setVisibility(View.GONE);
            }

            tvTimer = dialogScratchCard.findViewById(R.id.tvTimer);
            tvTimer.setVisibility(isTimerSet ? VISIBLE : View.GONE);

            ivFrontImage = dialogScratchCard.findViewById(R.id.ivFrontImage);
            ivFrontImage.setVisibility(isTimerSet ? VISIBLE : View.GONE);

            TextView tvMessage = dialogScratchCard.findViewById(R.id.tvMessage);
            TextView tvTaskName = dialogScratchCard.findViewById(R.id.tvTaskName);
            tvTaskName.setText(listScratchCards.get(selectedPos).getTaskTitle());
            TextView tvPoints = dialogScratchCard.findViewById(R.id.tvPoints);
            tvPoints.setText(listScratchCards.get(selectedPos).getScratchCardPoints() + " Bucks");

            ArrayList<String> listMessage = new ArrayList<>();
            listMessage.add("Amazing!");
            listMessage.add("Fantastic!");
            listMessage.add("Cool!");
            listMessage.add("Super!");
            listMessage.add("Nice!");
            listMessage.add("Wow!");
            listMessage.add("Great!");
            listMessage.add("Congratulations!");

            ImageView ivClose = dialogScratchCard.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogScratchCard.dismiss();
                }
            });

            ImageView ivBackImage = dialogScratchCard.findViewById(R.id.ivBackImage);
            Glide.with(Activity_Scratch_CouponsGame.this)
                    .load(objScratchCardModel.getBackImage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(ivBackImage);

            scratchCard = dialogScratchCard.findViewById(R.id.scratchCard);
            scratchCard.setVisibility(isTimerSet ? View.INVISIBLE : VISIBLE);
            scratchCard.setScratchWidth(getResources().getDimensionPixelSize(R.dimen.dim_25));
            scratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.6) {
                        scratchCard.setVisibility(View.GONE);
                        tvMessage.setText(Common_Utils.getRandomAdUnitId(listMessage));
                        tvMessage.setVisibility(VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Ads_Utils.showAppLovinInterstitialAd(Activity_Scratch_CouponsGame.this, new Ads_Utils.AdShownListener() {
                                    @Override
                                    public void onAdDismiss() {
                                        try {
                                            new Store_Scratch_Card_Async(Activity_Scratch_CouponsGame.this, listScratchCards.get(selectedPos).getId(), listScratchCards.get(selectedPos).getScratchCardPoints());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }, 500);
                    }
                }
            });


            Glide.with(this)
                    .asBitmap()
                    .load(objScratchCardModel.getFrontImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_10))))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                ivFrontImage.setImageBitmap(resource);
                                scratchCard.setScratchDrawable(new BitmapDrawable(getResources(), resource));
                                dialogScratchCard.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimer(boolean isFromOnCreate) {
        try {
            if (Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(scratchTime)) {
                isTimerSet = false;
                // allow scratch
            } else {
                isTimerSet = true;
                // disable scratch
                if (timer != null) {
                    timer.cancel();
                }
                time = Common_Utils.timeDiff(todayDate, lastDate);
                timer = new CountDownTimer((Integer.parseInt(scratchTime) - time) * 60000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {
                            if (tvTimer != null) {
                                tvTimer.setText(Common_Utils.updateTimeRemainingScratch(millisUntilFinished));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        isTimerSet = false;
                        if (tvTimer != null) {
                            tvTimer.setVisibility(View.GONE);
                            scratchCard.setVisibility(VISIBLE);
                            ivFrontImage.setVisibility(View.INVISIBLE);
                        }
                    }
                }.start();
                if (isFromOnCreate) {
                    Ads_Utils.showAppLovinInterstitialAd(Activity_Scratch_CouponsGame.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}