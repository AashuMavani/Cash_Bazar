package com.reward.cashbazar.Activity;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.GiveawayCodesListAdapter;
import com.reward.cashbazar.Adapter.GiveawaySocialAdapter;
import com.reward.cashbazar.Async.Get_Reward_List_Async;
import com.reward.cashbazar.Async.Models.Giveaway_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Store_Give_Away_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

public class EverydayGiveawayRewardActivity extends AppCompatActivity {

    private RecyclerView rvSocialPlatforms;
    private TextView lblLoadingAds, tvPoints, tvNote, tvStarLeft, tvStarRight;
    private LottieAnimationView ivLottieNoData;
    private Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private GiveawaySocialAdapter mAdapter;
    private ImageView ivHistory;
    private LinearLayout layoutPoints, layoutContent;
    private EditText etCouponCode;
    private AppCompatButton btnClaimNow, btnHowToClaim,btnOk;
    private RelativeLayout layoutMain;
    private TextView titleLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(EverydayGiveawayRewardActivity.this);
        setContentView(R.layout.activity_everyday_give_away_social);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

//        tvNote = findViewById(R.id.tvNote);
        layoutMain = findViewById(R.id.layoutMain);
        titleLabel = findViewById(R.id.titleLabel);
        titleLabel.setVisibility(View.GONE);
        Animation rotation = AnimationUtils.loadAnimation(EverydayGiveawayRewardActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);

    /*    tvStarLeft = findViewById(R.id.tvStarLeft);
        tvStarLeft.startAnimation(rotation);

        tvStarRight = findViewById(R.id.tvStarRight);
        tvStarRight.startAnimation(rotation);*/

        etCouponCode = findViewById(R.id.etCouponCode);
        etCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etCouponCode.post(new Runnable() {
                    @Override
                    public void run() {
                        etCouponCode.setLetterSpacing(etCouponCode.getText().toString().length() > 0 ? 0.2f : 0.0f);
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btnClaimNow = findViewById(R.id.btnClaimNow);

        btnClaimNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.setEnableDisable(EverydayGiveawayRewardActivity.this, btnClaimNow);
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    if (etCouponCode.getText().toString().trim().length() > 0) {
                        new Store_Give_Away_Async(EverydayGiveawayRewardActivity.this, etCouponCode.getText().toString().trim());
                    } else {
                        Common_Utils.setToast(EverydayGiveawayRewardActivity.this, "Enter giveaway code");
                    }
                } else {
                    Common_Utils.NotifyLogin(EverydayGiveawayRewardActivity.this);
                }
            }
        });

       /* ImageView layoutTelegram = findViewById(R.id.telegram);
        layoutTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseMain.getTelegramUrl());
            }
        });
        ImageView layoutYoutube = findViewById(R.id.img_youtube);
        layoutYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseMain.getYoutubeUrl());
            }
        });
        ImageView layoutInstagram = findViewById(R.id.img_instagram);
        layoutInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseMain.getInstagramUrl());
            }
        });*/

        layoutContent = findViewById(R.id.layoutContent);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        rvSocialPlatforms = findViewById(R.id.rvSocialPlatforms);

        ivHistory = findViewById(R.id.ivHistory);
      //  Common_Utils.startRoundAnimation(GiveAwaySocialActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(EverydayGiveawayRewardActivity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.GIVE_AWAY)
                            .putExtra("title", " GiveAway History"));
                } else {
                    Common_Utils.NotifyLogin(EverydayGiveawayRewardActivity.this);
                }
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(EverydayGiveawayRewardActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(EverydayGiveawayRewardActivity.this);
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

        View viewShine = findViewById(R.id.viewShine);
        Animation animUpDown = AnimationUtils.loadAnimation(EverydayGiveawayRewardActivity.this, R.anim.left_right);
        animUpDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewShine.startAnimation(animUpDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // start the animation
        viewShine.startAnimation(animUpDown);

        new Get_Reward_List_Async(EverydayGiveawayRewardActivity.this);
    }
/*    @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(EverydayGiveawayRewardActivity.this, responseModel.getAppLuck());
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

    public void changeGiveawayDataValues(Giveaway_Model responseModel) {
        if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
            Common_Utils.logFirebaseEvent(EverydayGiveawayRewardActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Giveaway", "Giveaway Got Reward");
            showWinPopup(responseModel.getCouponPoints());
        } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR) || responseModel.getStatus().equals("2")) {
            showErrorMessage("Daily Giveaway", responseModel);
        }
    }

    private void showErrorMessage(String title, Giveaway_Model responseModel) {
        try {
            final Dialog dialog1 = new Dialog(EverydayGiveawayRewardActivity.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.dialog_notification);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            btnOk.setText(responseModel.getBtn_name());
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(responseModel.getMessage());
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinInterstitialAd(EverydayGiveawayRewardActivity.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                        if (!Common_Utils.isStringNullOrEmpty(responseModel.getScreen_no())) {
                            Common_Utils.Redirect(EverydayGiveawayRewardActivity.this, responseModel.getScreen_no(), "", "", "", "", "");
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

    public void showWinPopup(String point) {
        Dialog dialogWin = new Dialog(EverydayGiveawayRewardActivity.this, android.R.style.Theme_Light);
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
        } catch (Exception e) {
            e.printStackTrace();
            lblPoints.setText("Bucks");
        }

        btnOk.setOnClickListener(v -> {
            Ads_Utils.showAppLovinInterstitialAd(EverydayGiveawayRewardActivity.this, new Ads_Utils.AdShownListener() {
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
                Common_Utils.GetCoinAnimation(EverydayGiveawayRewardActivity.this, layoutMain, layoutPoints);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), EverydayGiveawayRewardActivity.this);
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

    Giveaway_Model responseModel;
    public void setData(Giveaway_Model responseModel1) {
        responseModel = responseModel1;
        Log.e("Social--" , "" + responseModel.getSocialMedia().size());
        if (responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0) {
            Ads_Utils.showAppLovinInterstitialAd(EverydayGiveawayRewardActivity.this, null);

          /*  if (responseModel.getNote() != null) {
                tvNote.setText(Html.fromHtml(responseModel.getNote()));
            }*/

            mAdapter = new GiveawaySocialAdapter(this, responseModel.getSocialMedia(), new GiveawaySocialAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseModel.getSocialMedia().get(position).getUrl());
                }
            });
            rvSocialPlatforms.setAdapter(mAdapter);

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
            try {
                LinearLayout layoutGiveawayCodes = findViewById(R.id.layoutGiveawayCodes);
                if (responseModel.getGiveawayCodeList() != null && responseModel.getGiveawayCodeList().size() > 0) {
                    layoutGiveawayCodes.setVisibility(VISIBLE);
                    titleLabel.setVisibility(VISIBLE);
                    RecyclerView rvGiveawayCodeList = findViewById(R.id.rvGiveawayCodeList);
                    GiveawayCodesListAdapter mAdapter = new GiveawayCodesListAdapter(this, responseModel.getGiveawayCodeList(), new GiveawayCodesListAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                        }

                        @Override
                        public void onCopyButtonClicked(int position, View v) {
                            String val = responseModel.getGiveawayCodeList().get(position).getCouponCode();
                            if (val != null) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied Text", val);
                                clipboard.setPrimaryClip(clip);
                                Common_Utils.setToast(EverydayGiveawayRewardActivity.this, "Copied!");
                                Ads_Utils.showAppLovinRewardedAd(EverydayGiveawayRewardActivity.this, null);
                            }
                        }

                        @Override
                        public void onCompleteTaskButtonClicked(int position, View v) {
                            Common_Utils.Redirect(EverydayGiveawayRewardActivity.this, responseModel.getGiveawayCodeList().get(position).getScreenNo(), "", "", "", "", "");

                        }
                    });
                    rvGiveawayCodeList.setLayoutManager(new LinearLayoutManager(EverydayGiveawayRewardActivity.this));
                    rvGiveawayCodeList.setAdapter(mAdapter);
                } else {
                    layoutGiveawayCodes.setVisibility(View.GONE);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (responseModel.getTopAds() != null && !Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    Common_Utils.loadTopBannerAd(EverydayGiveawayRewardActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* if (!Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
                btnHowToClaim.setVisibility(VISIBLE);
                btnHowToClaim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseModel.getHelpVideoUrl());
                    }
                });
            } else {
                btnHowToClaim.setVisibility(View.GONE);
            }*/

        }



        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        layoutContent.setVisibility(responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0 ? View.VISIBLE : View.GONE);
        ivLottieNoData.setVisibility(responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0 ? View.GONE : View.VISIBLE);
        if (responseModel.getSocialMedia() == null && responseModel.getSocialMedia().size() == 0)
            ivLottieNoData.playAnimation();

       /* if (!Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
            btnHowToClaim.setVisibility(VISIBLE);
            btnHowToClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EverydayGiveawayRewardActivity.this, "claim", Toast.LENGTH_SHORT).show();
                    Common_Utils.openUrl(EverydayGiveawayRewardActivity.this, responseModel.getHelpVideoUrl());
                }
            });
        } else {
            btnHowToClaim.setVisibility(View.GONE);
        }*/

    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), EverydayGiveawayRewardActivity.this);
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