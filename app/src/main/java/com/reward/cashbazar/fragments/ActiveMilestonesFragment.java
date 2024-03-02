/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.fragments;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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
import com.reward.cashbazar.Activity.Activity_Milestone;
import com.reward.cashbazar.Activity.MilestoneHistoryActivity;
import com.reward.cashbazar.Adapter.ActiveMilestonesAdapter;
import com.reward.cashbazar.Async.GetMilestoneListAsync;
import com.reward.cashbazar.Async.Models.MilestoneDataMenu;
import com.reward.cashbazar.Async.Models.MilestonesResponseModel;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.StoreMilestoneAsync;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActiveMilestonesFragment extends Fragment {
    private View view;
    private TextView lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvMilestoneList;
    private final List<MilestoneDataMenu> listMilestones = new ArrayList<>();
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Response_Model responseMain;
    private int selectedPos = -1;
    private ActiveMilestonesAdapter adapter;

    public ActiveMilestonesFragment() {
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

        rvMilestoneList = view.findViewById(R.id.rvHistoryList);

        rvMilestoneList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ivLottieNoData = view.findViewById(R.id.ivLottieNoData);
        new GetMilestoneListAsync(getActivity());
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        updateDataAfterClaim();
                    }
                    selectedPos = -1;
                }
            });

    private void updateDataAfterClaim() {
        listMilestones.remove(selectedPos);
        adapter.notifyItemRemoved(selectedPos);
        adapter.notifyItemRangeChanged(selectedPos, listMilestones.size());
        ((Activity_Milestone) Objects.requireNonNull(getActivity())).updateHistoryScreen();
        hideShowList();
    }

    public void setData(MilestonesResponseModel responseModel) {
        if (responseModel != null && responseModel.getMilestoneData() != null && responseModel.getMilestoneData().size() > 0) {
            Ads_Utils.showAppLovinInterstitialAd(getActivity(), null);
            listMilestones.clear();
            listMilestones.addAll(responseModel.getMilestoneData());
            adapter = new ActiveMilestonesAdapter(listMilestones, getActivity(), new ActiveMilestonesAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getIsShowDetails()) && listMilestones.get(position).getIsShowDetails().equals("1")) {
                        selectedPos = position;
                        Intent intent = new Intent(getActivity(), MilestoneHistoryActivity.class).putExtra("milestoneId", listMilestones.get(position).getId());
                        someActivityResultLauncher.launch(intent);
                    }
                }

                @Override
                public void onActionClick(int position, View v) {
                    boolean isClaim = false;
                    if (listMilestones.get(position).getType().equals("0"))// number target
                    {
                        if (Integer.parseInt(listMilestones.get(position).getNoOfCompleted()) >= Integer.parseInt(listMilestones.get(position).getTargetNumber())) {
                            isClaim = true;
                        }
                    } else {// points target
                        if (Integer.parseInt(listMilestones.get(position).getEarnedPoints()) >= Integer.parseInt(listMilestones.get(position).getTargetPoints())) {
                            isClaim = true;
                        }
                    }
                    if (isClaim) {
                        selectedPos = position;
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            new StoreMilestoneAsync(getActivity(), listMilestones.get(position).getPoints(), listMilestones.get(position).getId());
                        } else {
                            Common_Utils.NotifyLogin(getActivity());
                        }
                    } else {
                        Common_Utils.Redirect(getActivity(), listMilestones.get(position).getScreenNo(), listMilestones.get(position).getTitle(), "", listMilestones.get(position).getId(), listMilestones.get(position).getId(), listMilestones.get(position).getBanner());
                    }
                }
            });
            rvMilestoneList.setAdapter(adapter);

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
                    Common_Utils.loadTopBannerAd(getActivity(), layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hideShowList();


    }

    private void hideShowList() {
        if (listMilestones.isEmpty()) {
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
        rvMilestoneList.setVisibility(listMilestones.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listMilestones.isEmpty() ? View.VISIBLE : View.GONE);
        if (listMilestones.isEmpty())
            ivLottieNoData.playAnimation();
    }


    public void changeMilestoneValues(MilestonesResponseModel responseModel) {
        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        Common_Utils.logFirebaseEvent(getActivity(), "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Milestones", "Milestones Got Reward");
        showWinPopup(responseModel.getWinningPoints());
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(getActivity(), android.R.style.Theme_Light);
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
                    Ads_Utils.showAppLovinRewardedAd(getActivity(), new Ads_Utils.AdShownListener() {
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
                Ads_Utils.showAppLovinRewardedAd(getActivity(), new Ads_Utils.AdShownListener() {
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
                    try {
                        ((Activity_Milestone) Objects.requireNonNull(getActivity())).updateBalance();
                        updateDataAfterClaim();
                        selectedPos = -1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (!getActivity().isFinishing() && !dialogWin.isShowing()) {
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
            nativeAdLoaderWin = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), getActivity());
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
                    ////AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    ////AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
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
                    ////AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    ////AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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
            if (nativeAdWin != null && nativeAdLoaderWin != null) {
                nativeAdLoaderWin.destroy(nativeAdWin);
                nativeAdWin = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
