package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Withdraw_Category_Adapter;
import com.reward.cashbazar.Async.Get_Withdraw_Category_Async;
import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Withdraw_Category;
import com.reward.cashbazar.Async.Models.Withdraw_Category_Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.recyclerviewpager.Recycler_ViewPager;
import com.reward.cashbazar.customviews.recyclerviewpager.ViewPager_Adapter;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithdrawTypes extends AppCompatActivity {
    private RecyclerView rvList;
    private List<Withdraw_Category> listWithdrawTypes = new ArrayList<>();
    private TextView tvPoints, lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private LinearLayout layoutPoints;
    private ImageView ivHistory;
    private RelativeLayout layoutSlider;
    private Recycler_ViewPager rvSlider;
    private Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(ActivityWithdrawTypes.this);
        setContentView(R.layout.activity_reedeam_types);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityWithdrawTypes.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ActivityWithdrawTypes.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    Intent intent = new Intent(ActivityWithdrawTypes.this, PointDetailsActivity.class);
                    intent.putExtra("type", POC_Constants.HistoryType.WITHDRAW_HISTORY);
                    intent.putExtra("title", "Withdrawal History");
                    startActivity(intent);
                } else {
                    Common_Utils.NotifyLogin(ActivityWithdrawTypes.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        rvList = findViewById(R.id.rvList);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);

        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Get_Withdraw_Category_Async(ActivityWithdrawTypes.this);
    }
  /*  @Override
    public void onBackPressed() {
        try {
            if (responseModel != null && responseModel.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(ActivityWithdrawTypes.this, responseModel.getAppLuck());
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

    private Withdraw_Category_Response_Model responseModel;
    public void setData(Withdraw_Category_Response_Model responseModel1) {
        responseModel = responseModel1;
        if (responseModel.getType() != null && responseModel.getType().size() > 0) {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            listWithdrawTypes.addAll(responseModel.getType());
            if (Common_Utils.isShowAppLovinNativeAds()) {
                if (listWithdrawTypes.size() <= 4) {
                    listWithdrawTypes.add(listWithdrawTypes.size(), new Withdraw_Category());
                } else {
                    for (int i2 = 0; i2 < this.listWithdrawTypes.size(); i2++) {
                        if ((i2 + 1) % 5 == 0) {
                            listWithdrawTypes.add(i2, new Withdraw_Category());
                            break; // add only 1 ad view
                        }
                    }
                }
            }
            Withdraw_Category_Adapter adapter = new Withdraw_Category_Adapter(listWithdrawTypes, ActivityWithdrawTypes.this, new Withdraw_Category_Adapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (listWithdrawTypes.get(position).getIsActive() != null && listWithdrawTypes.get(position).getIsActive().equals("1")) {
                        startActivity(new Intent(ActivityWithdrawTypes.this, ActivityWithdrawTypesList.class)
                                .putExtra("type", listWithdrawTypes.get(position).getType()));
                    }
                }
            });

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(ActivityWithdrawTypes.this, 2);
            mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == Withdraw_Category_Adapter.ITEM_AD) {
                        return 2;
                    }
                    return 1;
                }
            });
            rvList.setLayoutManager(mGridLayoutManager);
            rvList.setAdapter(adapter);
        }
        try {
            if (responseModel.getHomeSlider() != null && responseModel.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Home_Slider_Menu>) responseModel.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new ViewPager_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Common_Utils.Redirect(ActivityWithdrawTypes.this, responseModel.getHomeSlider().get(position).getScreenNo(), responseModel.getHomeSlider().get(position).getTitle()
                                , responseModel.getHomeSlider().get(position).getUrl(), responseModel.getHomeSlider().get(position).getId(), null, responseModel.getHomeSlider().get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (listWithdrawTypes.isEmpty() && Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        rvList.setVisibility(listWithdrawTypes.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listWithdrawTypes.isEmpty() ? View.VISIBLE : View.GONE);
        if (listWithdrawTypes.isEmpty())
            ivLottieNoData.playAnimation();

    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityWithdrawTypes.this);
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
    public void onResume() {
        super.onResume();
        updateUserPoints();
    }

    private void updateUserPoints() {
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}