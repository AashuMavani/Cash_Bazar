/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Refer_User_Detail_Adapter;
import com.reward.cashbazar.Async.Get_Point_Detail_Async;
import com.reward.cashbazar.Async.Models.Point_History_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Wallet_ListMenu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class Invite_User_Detail_Fragment extends Fragment {
    private View view;
    private int pageNo = 1;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private TextView lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvHistoryList;
    private final List<Wallet_ListMenu> listPointHistory = new ArrayList<>();
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Response_Model responseMain;
    private boolean isAdLoaded = false;

    public Invite_User_Detail_Fragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_refer_point_detail, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        rvHistoryList = view.findViewById(R.id.rvHistoryList);
        rvHistoryList.setAdapter(new Refer_User_Detail_Adapter(listPointHistory, getActivity()));
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ivLottieNoData = view.findViewById(R.id.ivLottieNoData);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Get_Point_Detail_Async(getActivity(), POC_Constants.HistoryType.REFER_USERS, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
        new Get_Point_Detail_Async(getActivity(), POC_Constants.HistoryType.REFER_USERS, String.valueOf(pageNo));
    }

    public void setData(Point_History_Model responseModel) {
        if (responseModel != null && responseModel.getData() != null && responseModel.getData().size() > 0) {
            int prevItemCount = listPointHistory.size();
            listPointHistory.addAll(responseModel.getData());
            if (prevItemCount == 0) {
                rvHistoryList.getAdapter().notifyDataSetChanged();
            } else {
                rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getData().size());
            }

            numOfPage = responseModel.getTotalPage();
            pageNo = Integer.parseInt(responseModel.getCurrentPage());
            if (!isAdLoaded) {
                // Load home note webview top
                try {
                    if (!Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                        WebView webNote = view.findViewById(R.id.webNote);
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
                        LinearLayout layoutTopAds = view.findViewById(R.id.layoutTopAds);
                        Common_Utils.loadTopBannerAd(getActivity(), layoutTopAds,  responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            isAdLoaded = true;
        }
        if (listPointHistory.isEmpty()) {
            layoutAds = view.findViewById(R.id.layoutAds);
            layoutAds.setVisibility(View.VISIBLE);
            frameLayoutNativeAd = view.findViewById(R.id.fl_adplaceholder);
            lblLoadingAds = view.findViewById(R.id.lblLoadingAds);
            if (Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(View.GONE);
            }
        }
        rvHistoryList.setVisibility(listPointHistory.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listPointHistory.isEmpty() ? View.VISIBLE : View.GONE);
        if (listPointHistory.isEmpty() )
            ivLottieNoData.playAnimation();
        // Load Bottom banner ad
        try {
            if (!listPointHistory.isEmpty() && listPointHistory.size() < 5) {
                LinearLayout layoutBannerAdBottom = view.findViewById(R.id.layoutBannerAdBottom);
                layoutBannerAdBottom.setVisibility(View.VISIBLE);
                TextView lblAdSpaceBottom = view.findViewById(R.id.lblAdSpaceBottom);
                Common_Utils.loadBannerAds(getActivity(), layoutBannerAdBottom, lblAdSpaceBottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadAppLovinNativeAds() {
        try {
                nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), getActivity());
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                        frameLayoutNativeAd = view.findViewById(R.id.fl_adplaceholder);
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
    public void onDestroyView() {
        super.onDestroyView();
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
