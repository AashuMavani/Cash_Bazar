package com.reward.cashbazar.Activity;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.EarnedPointHistoryAdapter;
import com.reward.cashbazar.Adapter.Withdraw_Point_Detail_Adapter;
import com.reward.cashbazar.Async.Get_Point_Detail_Async;
import com.reward.cashbazar.Async.Models.Point_History_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Wallet_ListMenu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class PointDetailsActivity extends AppCompatActivity {
    private RecyclerView rvHistoryList;
    private List<Wallet_ListMenu> listPointHistory = new ArrayList<>();
    private TextView lblLoadingAds, tvTitle, tvPoints;
    private LottieAnimationView ivLottieNoData;
    private Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private int pageNo = 1;
    private String type = POC_Constants.HistoryType.ALL;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private boolean isAdLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(PointDetailsActivity.this);
        setContentView(R.layout.activity_point_detail);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        tvTitle = findViewById(R.id.tvTitle);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("type")) {
            type = getIntent().getStringExtra("type");
            tvTitle.setText(getIntent().getStringExtra("title"));
        }
        rvHistoryList = findViewById(R.id.rvHistoryList);
        if (type.equals(POC_Constants.HistoryType.WITHDRAW_HISTORY)) {
            rvHistoryList.setAdapter(new Withdraw_Point_Detail_Adapter(listPointHistory, PointDetailsActivity.this));
        } else {
            rvHistoryList.setAdapter(new EarnedPointHistoryAdapter(listPointHistory, PointDetailsActivity.this, type));
        }
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Get_Point_Detail_Async(PointDetailsActivity.this, type, String.valueOf(pageNo));

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Get_Point_Detail_Async(PointDetailsActivity.this, type, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
    }

/*    @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(PointDetailsActivity.this, responseModel.getAppLuck());
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

    private Point_History_Model responseModel;
    public void setData(Point_History_Model responseModel1) {
        responseModel = responseModel1;
        if ((responseModel.getWalletList() != null && responseModel.getWalletList().size() > 0) || (responseModel.getData() != null && responseModel.getData().size() > 0)) {
            int prevItemCount = listPointHistory.size();
            if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                Ads_Utils.showAppLovinInterstitialAd(PointDetailsActivity.this, null);
            } else if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_REWARD)) {
                Ads_Utils.showAppLovinRewardedAd(PointDetailsActivity.this, null);
            }
            if (type.equals(POC_Constants.HistoryType.WITHDRAW_HISTORY) && responseModel.getData() != null && responseModel.getData().size() > 0) {
                listPointHistory.addAll(responseModel.getData());
                if (prevItemCount == 0) {
                    rvHistoryList.getAdapter().notifyDataSetChanged();
                } else {
                    rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getData().size());
                }
            } else {
                listPointHistory.addAll(responseModel.getWalletList());
                if (prevItemCount == 0) {
                    rvHistoryList.getAdapter().notifyDataSetChanged();
                } else {
                    rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getWalletList().size());
                }
            }
            numOfPage = responseModel.getTotalPage();
            pageNo = Integer.parseInt(responseModel.getCurrentPage());
            if (!isAdLoaded) {
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
                        Common_Utils.loadTopBannerAd(PointDetailsActivity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (responseModel.getMiniAds() != null && !Common_Utils.isStringNullOrEmpty(responseModel.getMiniAds().getImage()) && (POC_SharePrefs.getInstance().getString(POC_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).length() == 0 || !POC_SharePrefs.getInstance().getString(POC_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).equals(Common_Utils.getCurrentDate()))) {
                    try {
                        RelativeLayout layoutMiniAd = findViewById(R.id.layoutMiniAd);
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        if (responseModel.getMiniAds() != null) {
                            if (responseModel.getMiniAds().getImage().endsWith(".json")) {
                                LottieAnimationView ivLottieViewMiniAd = findViewById(R.id.ivLottieViewMiniAd);
                                ivLottieViewMiniAd.setVisibility(android.view.View.VISIBLE);
                                Common_Utils.setLottieAnimation(ivLottieViewMiniAd, responseModel.getMiniAds().getImage());
                                ivLottieViewMiniAd.setRepeatCount(LottieDrawable.INFINITE);
                                ivLottieViewMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Common_Utils.Redirect(PointDetailsActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                            } else {
                                ImageView ivMiniAd = findViewById(R.id.ivMiniAd);
                                ivMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Common_Utils.Redirect(PointDetailsActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                    }
                                });
                                Glide.with(this)
                                        .load(responseModel.getMiniAds().getImage())
                                        .override(getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._140sdp), getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._200sdp))
                                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_5))))
                                        .addListener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                ivMiniAd.setVisibility(View.VISIBLE);
                                                return false;
                                            }
                                        }).into(ivMiniAd);
                            }
                            ImageView ivCloseMiniAd = findViewById(R.id.ivCloseMiniAd);
                            ivCloseMiniAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    layoutMiniAd.setVisibility(View.GONE);
                                }
                            });
                            layoutMiniAd.setVisibility(View.VISIBLE);
                            POC_SharePrefs.getInstance().putString(POC_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId(), Common_Utils.getCurrentDate());
                        } else {
                            layoutMiniAd.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            isAdLoaded = true;
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
        rvHistoryList.setVisibility(listPointHistory.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listPointHistory.isEmpty() ? View.VISIBLE : View.GONE);
        if (listPointHistory.isEmpty())
            ivLottieNoData.playAnimation();
        // Load Bottom banner ad
        try {
            if (!listPointHistory.isEmpty() && listPointHistory.size() < 5) {
                LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                layoutBannerAdBottom.setVisibility(View.VISIBLE);
                TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                Common_Utils.loadBannerAds(PointDetailsActivity.this, layoutBannerAdBottom, lblAdSpaceBottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), PointDetailsActivity.this);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}