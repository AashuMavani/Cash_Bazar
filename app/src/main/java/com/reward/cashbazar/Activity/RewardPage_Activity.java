/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXCardStyle;
import com.makeopinion.cpxresearchlib.models.SurveyItem;
import com.makeopinion.cpxresearchlib.models.TransactionItem;
import com.reward.cashbazar.Adapter.ActiveMilestonesAdapter;
import com.reward.cashbazar.Adapter.DailyTargetAdapter;
import com.reward.cashbazar.Adapter.Easy_EarningAdapterReward;
import com.reward.cashbazar.Adapter.EveryDayClaimAdapter;
import com.reward.cashbazar.Adapter.HomeGiveawayCodesAdapter;
import com.reward.cashbazar.Adapter.Home_Single_Slider_Adapter;
import com.reward.cashbazar.Adapter.LiveMilestonesAdapter;
import com.reward.cashbazar.Adapter.QuickTasksAdapter;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Get_Reward_Screen_Async;
import com.reward.cashbazar.Async.Models.Home_Data_List_Menu;
import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Reward_Screen_Model;
import com.reward.cashbazar.Async.SaveDailyTargetAsync;
import com.reward.cashbazar.Async.SaveQuickTaskAsync;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Activity_Manager;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.Arrays;
import java.util.List;

public class RewardPage_Activity extends AppCompatActivity implements CPXResearchListener {

    private Response_Model responseMain;
    private ImageView iv_left, ivNotification, ivAdjoe, ivDice;
    private TextView tvPoints, tvTimer, tvTimerDailyTarget;
    private LinearLayout layoutInflate, layoutPoints, layoutTimer,layoutGiveawayCode;
    private RecyclerView rvDailyLoginList;
    private EveryDayClaimAdapter dailyLoginAdapter;
    public static Reward_Screen_Model objRewardScreenModel;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private DailyTargetAdapter dailyTargetAdapter;
    private int selectedQuickTaskPos = -1, selectedDailyTargetPos = -1;
    private LiveMilestonesAdapter adapter;

    Animation blinkAnimation;
    private ImageView ivAdjoeLeaderboard;
    private String todayDate, endDate, todayDateDailyTarget, endDateDailyTarget;
    private CountDownTimer timer, timerDailyTarget;
    private int time, timeDailyTarget;
    private NestedScrollView scroll;
    private LottieAnimationView lottieAdjoe;
    private TextView txtSurvey,lblGiveawayCode;
    LinearLayout relSlider;
    private int selectedPos = -1;
    private View viewMilestones;
    private QuickTasksAdapter quickTasksAdapter;
    private View quickTaskView;
    private CountDownTimer timerQuickTask;
    private boolean isTimerSet = false, isTimerOver = false;


    private ActiveMilestonesAdapter milestoneAdapter;

    public RewardPage_Activity() {
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            milestoneAdapter.listMilestones.remove(selectedPos);
                            milestoneAdapter.notifyItemRemoved(selectedPos);
                            milestoneAdapter.notifyItemRangeChanged(selectedPos, milestoneAdapter.listMilestones.size());
                            if (milestoneAdapter.listMilestones.size() == 0) {
                                layoutInflate.removeView(viewMilestones);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    selectedPos = -1;
                }
            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(RewardPage_Activity.this);
        setContentView(R.layout.activity_reward_page);
        initView();

        blinkAnimation = new AlphaAnimation(0.3f, 1.0f);
        blinkAnimation.setDuration(500); //You can manage the blinking time with this parameter
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

    }

    private void initView() {
        try {
            responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
            iv_left = findViewById(R.id.iv_left);
            lottieAdjoe = findViewById(R.id.lottieAdjoe);
            ivAdjoe = findViewById(R.id.ivAdjoe);
            layoutGiveawayCode = findViewById(R.id.layoutGiveawayCode);
            lblGiveawayCode = findViewById(R.id.lblGiveawayCode);

            iv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            layoutPoints = findViewById(R.id.layoutPoints);
            layoutPoints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        startActivity(new Intent(RewardPage_Activity.this, MyWalletActivity.class));
                    } else {
                        Common_Utils.NotifyLogin(RewardPage_Activity.this);
                    }
                }
            });
            ivAdjoeLeaderboard = findViewById(R.id.ivAdjoeLeaderboard);
            ivAdjoeLeaderboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        startActivity(new Intent(RewardPage_Activity.this, AdjoeLeaderboardActivity.class));
                 /*   } else {
                        Common_Utils.NotifyLogin(RewardPage_Activity.this);
                    }*/
                }
            });
            try {
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowPlaytimeIcone()) && responseMain.getIsShowPlaytimeIcone().equalsIgnoreCase("1")) {
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getImageAdjoeIcon())) {
                        if (responseMain.getImageAdjoeIcon().endsWith(".json")) {
                            lottieAdjoe.setVisibility(View.VISIBLE);
                            lottieAdjoe.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.openAdjoeOfferWall(RewardPage_Activity.this);
                                }
                            });
                            Common_Utils.setLottieAnimation(lottieAdjoe, responseMain.getImageAdjoeIcon());
                        } else {
                            ivAdjoe.setVisibility(View.VISIBLE);
                            ivAdjoe.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.openAdjoeOfferWall(RewardPage_Activity.this);
                                }
                            });
                            Glide.with(RewardPage_Activity.this)
                                    .load(responseMain.getImageAdjoeIcon())
                                    .override(getResources().getDimensionPixelSize(R.dimen.dim_32))
                                    .into(ivAdjoe);
                        }
                    }
                } else {
                    ivAdjoe.setVisibility(View.GONE);
                    lottieAdjoe.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            scroll = findViewById(R.id.scroll);
            layoutInflate = findViewById(R.id.layoutInflate);
            tvPoints = findViewById(R.id.tvPoints);
            layoutPoints = findViewById(R.id.layoutPoints);

            layoutPoints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        startActivity(new Intent(RewardPage_Activity.this, MyWalletActivity.class));
                    } else {
                        Common_Utils.NotifyLogin(RewardPage_Activity.this);
                    }
                }
            });


            new Get_Reward_Screen_Async(RewardPage_Activity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //AppLogger.getInstance().e("REWARD===", "ON RESUME===");
        try {
            if (!isTimerSet && !isTimerOver && selectedQuickTaskPos < 0) {
                new Get_Reward_Screen_Async(RewardPage_Activity.this);
            }
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            int lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
            dailyLoginAdapter.setLastClaimedData(lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()));
            if (lastClaimDay > 3) {
                LinearLayoutManager llm = (LinearLayoutManager) rvDailyLoginList.getLayoutManager();
                llm.scrollToPositionWithOffset(lastClaimDay - 1, 0);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        try {
            updateUserPoints();
            int lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
            dailyLoginAdapter.setLastClaimedData(lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()));
            if (lastClaimDay > 3) {
                LinearLayoutManager llm = (LinearLayoutManager) rvDailyLoginList.getLayoutManager();
                llm.scrollToPositionWithOffset(lastClaimDay - 1, 0);
            }
            if (isTimerSet && isTimerOver && selectedQuickTaskPos >= 0) {
                new SaveQuickTaskAsync(RewardPage_Activity.this, quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getPoints(), quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getId());

            }
            isTimerSet = false;
            isTimerOver = false;
//            new GetWalletBalanceAsync(RewardPage_Activity.this);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateUserPoints() {
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Reward_Screen_Model responseModel;

    public void setData(Reward_Screen_Model responseModel1) {
        responseModel = responseModel1;
//        try {

//        AppLogger.getInstance().e("RESPONSE_activity", "" + new Gson().toJson(responseModel));
        scroll.scrollTo(0, 0);
        objRewardScreenModel = responseModel;
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
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowGiveawayCode()) && responseMain.getIsShowGiveawayCode().equals("1")) {
                layoutGiveawayCode.setVisibility(View.VISIBLE);
                RecyclerView rvGiveawayCodes = findViewById(R.id.rvGiveawayCodes);
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getGiveawayCode())) {
                    lblGiveawayCode.setText("Apply Giveaway Code");
                    rvGiveawayCodes.setVisibility(View.VISIBLE);
                    List<String> list = Arrays.asList(responseMain.getGiveawayCode().split(","));
                    rvGiveawayCodes.setLayoutManager(new LinearLayoutManager(RewardPage_Activity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvGiveawayCodes.setAdapter(new HomeGiveawayCodesAdapter(list, RewardPage_Activity.this, new HomeGiveawayCodesAdapter.ClickListener() {
                        @Override
                        public void onClick(int position, View v, LinearLayout linearLayout) {
                            startActivity(new Intent(RewardPage_Activity.this, EverydayGiveawayRewardActivity.class));
                        }
                    }));
                } else {
                    lblGiveawayCode.setText("Have a Giveaway Code?");
                    rvGiveawayCodes.setVisibility(View.GONE);
                }
                layoutGiveawayCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RewardPage_Activity.this, EverydayGiveawayRewardActivity.class));
                    }
                });
            } else {
                layoutGiveawayCode.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (layoutInflate != null) {
            layoutInflate.removeAllViews();
        }

        layoutInflate.setVisibility(View.VISIBLE);

//        AppLogger.getInstance().d("TAG", "inflateRewardScreenData: >>>>=>>>>>>>=========" + responseModel.getRewardDataList());

        if (responseModel.getRewardDataList() != null && responseModel.getRewardDataList().size() > 0) {
            for (int i = 0; i < responseModel.getRewardDataList().size(); i++) {
                try {
                    inflateRewardScreenData(responseModel.getRewardDataList().get(i).getType(), responseModel.getRewardDataList().get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//        } catch (Exception e) {
//            e.printStackTrace();        }

    }

    private void inflateRewardScreenData(String type, Home_Data_List_Menu categoryModel) {
        switch (type) {
            case POC_Constants.RewardDataType.DAILY_BONUS:
//                POC_AppLogger.getInstance().d("TAG", "inflateRewardScreenData: >>>>===========" + type);
                View viewDailyLogin = getLayoutInflater().inflate(R.layout.inflate_reward_everyday_login, null);
                TextView lblDailyLogin = viewDailyLogin.findViewById(R.id.lblTitle);
                TextView lblDailyLoginSubTitle = viewDailyLogin.findViewById(R.id.lblSubTitle);
                lblDailyLogin.setText(categoryModel.getTitle());
                lblDailyLoginSubTitle.setText(categoryModel.getSubTitle());
                rvDailyLoginList = viewDailyLogin.findViewById(R.id.rvDailyLoginList);
                if (objRewardScreenModel.getDailyBonus() != null && objRewardScreenModel.getDailyBonus().getData() != null && objRewardScreenModel.getDailyBonus().getData().size() > 0) {
                    int lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
                    dailyLoginAdapter = new EveryDayClaimAdapter(objRewardScreenModel.getDailyBonus().getData(), RewardPage_Activity.this, lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()), new EveryDayClaimAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                            } else {
                                startActivity(new Intent(RewardPage_Activity.this, ActivityEveryDayLogin.class)
                                        .putExtra("objRewardScreenModel", objRewardScreenModel)
                                        .putExtra("title", categoryModel.getTitle())
                                        .putExtra("subTitle", categoryModel.getSubTitle()));
                            }
                        }
                    });
                    rvDailyLoginList.setAdapter(dailyLoginAdapter);
                    if (lastClaimDay > 3) {
                        LinearLayoutManager llm = (LinearLayoutManager) rvDailyLoginList.getLayoutManager();
                        llm.scrollToPositionWithOffset(lastClaimDay - 1, 0);
                    }
                } else {
                    lblDailyLogin.setVisibility(View.GONE);
                    rvDailyLoginList.setVisibility(View.GONE);
                }
                layoutInflate.addView(viewDailyLogin);
                break;
            case POC_Constants.RewardDataType.GRID:
                View viewEarnGrid = getLayoutInflater().inflate(R.layout.inflate_home_layout, null);
                RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);
                earnGridHeader.setVisibility(View.GONE);

                Easy_EarningAdapterReward earnGridAdapter = new Easy_EarningAdapterReward(RewardPage_Activity.this, categoryModel.getData(), new Easy_EarningAdapterReward.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                    }
                });
                gridList.setLayoutManager(new GridLayoutManager(RewardPage_Activity.this, 2));
                gridList.setAdapter(earnGridAdapter);
                layoutInflate.addView(viewEarnGrid);

                break;

            case POC_Constants.RewardDataType.LIVE_CONTEST:
                View viewLiveContest = getLayoutInflater().inflate(R.layout.inflate_home_live_contest, null);

                TextView lblLiveContest = viewLiveContest.findViewById(R.id.lblTitle);
                RelativeLayout layoutIconLiveContest = viewLiveContest.findViewById(R.id.layoutIcon);
                ProgressBar probrLiveContest = viewLiveContest.findViewById(R.id.probr);
                ImageView ivIconLiveContest = viewLiveContest.findViewById(R.id.ivIcon);
                LottieAnimationView ivLottieLiveContest = viewLiveContest.findViewById(R.id.ivLottie);
                TextView tvYourRank = viewLiveContest.findViewById(R.id.tvYourRank);
                TextView lblPoints = viewLiveContest.findViewById(R.id.lblPoints);
                TextView tvPointsLiveContest = viewLiveContest.findViewById(R.id.tvPoints);

                TextView lblSubTitle = viewLiveContest.findViewById(R.id.lblSubTitle);
                if (!Common_Utils.isStringNullOrEmpty(categoryModel.getSubTitle())) {
                    lblSubTitle.setText(categoryModel.getSubTitle());
                } else {
                    lblSubTitle.setVisibility(View.VISIBLE);
                }

                TextView tvLabel = viewLiveContest.findViewById(R.id.tvLabel);
                if (!Common_Utils.isStringNullOrEmpty(categoryModel.getLabel())) {
                    tvLabel.setText(categoryModel.getLabel());
                    tvLabel.setVisibility(View.VISIBLE);
                    tvLabel.startAnimation(blinkAnimation);
                } else {
                    tvLabel.setVisibility(View.VISIBLE);
                }

                tvPointsLiveContest.setText(categoryModel.getWinningPoints());
                tvYourRank.setText(categoryModel.getLeaderboardRank());
                try {
                    if (Integer.parseInt(categoryModel.getLeaderboardRank()) > 3) {
                        lblPoints.setText("Chance to Win");
                    }
                } catch (NumberFormatException e) {
                    lblPoints.setText("Chance to Win");
                }
                lblLiveContest.setText(categoryModel.getTitle());
                if (categoryModel.getIcon() != null) {
                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getIconBGColor())) {
                        Drawable mDrawable = ContextCompat.getDrawable(RewardPage_Activity.this, R.drawable.rectangle_white);
                        mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(categoryModel.getIconBGColor()), PorterDuff.Mode.SRC_IN));
                        layoutIconLiveContest.setBackground(mDrawable);
                    }
                    layoutIconLiveContest.setVisibility(View.VISIBLE);
                    if (categoryModel.getIcon().contains(".json")) {
                        ivIconLiveContest.setVisibility(View.GONE);
                        ivLottieLiveContest.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(ivLottieLiveContest, categoryModel.getIcon());
                        ivLottieLiveContest.setRepeatCount(LottieDrawable.INFINITE);
                        probrLiveContest.setVisibility(View.GONE);
                    } else {
                        ivIconLiveContest.setVisibility(View.VISIBLE);
                        ivLottieLiveContest.setVisibility(View.GONE);
                        Glide.with(RewardPage_Activity.this).load(categoryModel.getIcon()).override(getResources().getDimensionPixelSize(R.dimen.dim_50)).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                probrLiveContest.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(ivIconLiveContest);
                    }
                } else {
                    layoutIconLiveContest.setVisibility(View.GONE);
                }

//                CardView cardContent = viewLiveContest.findViewById(R.id.cardContent);
//                cardContent.setCardBackgroundColor(Color.parseColor(categoryModel.getIconBGColor()));

                LinearLayout layoutContentClick = viewLiveContest.findViewById(R.id.layoutContentClick);
                layoutContentClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                    }
                });
                layoutInflate.addView(viewLiveContest);
                break;


            case POC_Constants.RewardDataType.SINGLE_SLIDER:
                View iconSingleSlider = getLayoutInflater().inflate(R.layout.inflate_home_layout, null);
                RecyclerView rvSliderlist = iconSingleSlider.findViewById(R.id.rvIconlist);
                TextView txtHeader = iconSingleSlider.findViewById(R.id.txtTitleHeader);
                LinearLayout linearcard = iconSingleSlider.findViewById(R.id.relSlider);
                FrameLayout.LayoutParams fram1 = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                fram1.setMargins(getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_10));
                linearcard.setLayoutParams(fram1);

                if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                    txtHeader.setVisibility(View.VISIBLE);
                    txtHeader.setText(categoryModel.getTitle());
                } else {
                    txtHeader.setVisibility(View.GONE);
                }
                ////Loge("Size--)", "" + categoryModel.getData().size());
                Home_Single_Slider_Adapter homeSingleSilderAdapter = new Home_Single_Slider_Adapter(RewardPage_Activity.this, categoryModel.getData(), false, new Home_Single_Slider_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                            Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                        } else {
                            Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    }
                });
                rvSliderlist.setLayoutManager(new LinearLayoutManager(RewardPage_Activity.this, LinearLayoutManager.VERTICAL, false));
                rvSliderlist.setAdapter(homeSingleSilderAdapter);
                layoutInflate.addView(iconSingleSlider);
                break;

        /*    case POC_Constants.RewardDataType.CPXSurvey:
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowSurvey()) && responseMain.getIsShowSurvey().equals("1")) {
                    View viewCPXSurvey = getLayoutInflater().inflate(R.layout.inflate_survey_item_menu, null);
                    TextView tvTopSurveys = viewCPXSurvey.findViewById(R.id.tvTopSurveys);
                    LinearLayout layoutSurveys = viewCPXSurvey.findViewById(R.id.layoutSurveys);
                    LinearLayout layoutSurveyList = viewCPXSurvey.findViewById(R.id.layoutSurveyList);

                    TextView txtNoSurvey = viewCPXSurvey.findViewById(R.id.txtNoSurvey);
                    txtNoSurvey.setVisibility(View.VISIBLE);
                    txtNoSurvey.setText("Loading Surveys...");
                    TextView tvSeeSurveyHistory = viewCPXSurvey.findViewById(R.id.tvSeeSurveyHistory);
                    TextView tvTopOffers = viewCPXSurvey.findViewById(R.id.tvTopOffers);
                    RecyclerView rvTopOffers = viewCPXSurvey.findViewById(R.id.rvTopOffers);
                    tvSeeSurveyHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                startActivity(new Intent(RewardPage_Activity.this, PointDetailsActivity.class)
                                        .putExtra("type", POC_Constants.HistoryType.CPXSurvey)
                                        .putExtra("title", "Survey History"));
                            } else {
                                Common_Utils.NotifyLogin(RewardPage_Activity.this);
                            }
                        }
                    });
                    tvTopSurveys.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutSurveys.setVisibility(View.VISIBLE);
                            rvTopOffers.setVisibility(View.GONE);
                            tvTopSurveys.setBackgroundResource(R.drawable.bg_survey_selected);
                            tvTopSurveys.setTextColor(getResources().getColor(R.color.white));

                            tvTopOffers.setBackgroundResource(0);
                            tvTopOffers.setTextColor(getResources().getColor(R.color.black));
                        }
                    });
                    tvTopOffers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutSurveys.setVisibility(View.GONE);
                            rvTopOffers.setVisibility(View.VISIBLE);

                            tvTopOffers.setBackgroundResource(R.drawable.bg_survey_selected);
                            tvTopOffers.setTextColor(getResources().getColor(R.color.white));

                            tvTopSurveys.setBackgroundResource(0);
                            tvTopSurveys.setTextColor(getResources().getColor(R.color.black));
                        }
                    });

                    if (responseMain.getTop_offers() != null && responseMain.getTop_offers().size() > 0) {
                        TopOffersListAdapter dailyLoginAdapter = new TopOffersListAdapter(RewardPage_Activity.this, responseMain.getTop_offers(), new TopOffersListAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                if (responseMain.getTop_offers().get(position).getIsShowDetails() != null && responseMain.getTop_offers().get(position).getIsShowDetails().equals("1")) {
                                    Intent intent = new Intent(RewardPage_Activity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", responseMain.getTop_offers().get(position).getId());
                                    startActivity(intent);
                                } else {
                                    Common_Utils.Redirect(RewardPage_Activity.this, responseMain.getTop_offers().get(position).getScreenNo(), responseMain.getTop_offers().get(position).getTitle()
                                            , responseMain.getTop_offers().get(position).getUrl(), null, responseMain.getTop_offers().get(position).getId(), responseMain.getTop_offers().get(position).getIcon());
                                }
                            }
                        });
                        rvTopOffers.setLayoutManager(new LinearLayoutManager(RewardPage_Activity.this, LinearLayoutManager.HORIZONTAL, false));
                        rvTopOffers.setAdapter(dailyLoginAdapter);
                    } else {
                        tvTopOffers.setVisibility(View.GONE);
                        rvTopOffers.setVisibility(View.GONE);
                    }
                    setupCPX(layoutSurveyList, txtNoSurvey);
                    layoutInflate.addView(viewCPXSurvey);
                }
                break;*/

            case POC_Constants.RewardDataType.LIVE_MILESTONES:
//                AppLogger.getInstance().e("TAG===", "==<=>>" + categoryModel.getMilestoneData().size());
                if (categoryModel.getMilestoneData() != null && categoryModel.getMilestoneData().size() > 0) {
                    View viewMilestones = getLayoutInflater().inflate(R.layout.inflate_home_live_milestones, null);
                    viewMilestones.setTag(POC_Constants.RewardDataType.LIVE_MILESTONES);
                    TextView lblMilestones = viewMilestones.findViewById(R.id.lblTitle);
                    TextView tvshowmilestonelist = viewMilestones.findViewById(R.id.tvshowmilestonelist);
                    TextView lblMilestonesSubTitle = viewMilestones.findViewById(R.id.lblSubTitle);
//                    AppLogger.getInstance().e("TAG", "==<<>>??" + categoryModel.getTitle());
                    lblMilestones.setText(categoryModel.getTitle());
                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getSubTitle())) {
//                        AppLogger.getInstance().e("TAG===", "==<>>" + categoryModel.getSubTitle());
                        lblMilestonesSubTitle.setText(categoryModel.getSubTitle());
                    } else {
                        lblMilestonesSubTitle.setVisibility(View.GONE);
                    }

                    TextView tvLabelMilestone = viewMilestones.findViewById(R.id.tvLabel);
                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getLabel())) {
//                        AppLogger.getInstance().e("TAG", "<<>>>==" + categoryModel.getLabel());
                        tvLabelMilestone.setText(categoryModel.getLabel());
                        tvLabelMilestone.setVisibility(View.VISIBLE);
                        tvLabelMilestone.startAnimation(blinkAnimation);
                    } else {
                        tvLabelMilestone.setVisibility(View.GONE);
                    }

                    tvshowmilestonelist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                startActivity(new Intent(RewardPage_Activity.this, Activity_Milestone.class)
                                        .putExtra("type", POC_Constants.HistoryType.LIVE_MILESTONES)
                                        .putExtra("title", "Milestone History"));
                           /* } else {
                                Common_Utils.NotifyLogin(RewardPage_Activity.this);
                            }*/
                        }
                    });

                    RecyclerView rvList = viewMilestones.findViewById(R.id.rvList);

                    adapter = new LiveMilestonesAdapter(categoryModel.getMilestoneData(), RewardPage_Activity.this, categoryModel.getIconBGColor(), new LiveMilestonesAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.setEnableDisable(RewardPage_Activity.this, v);
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                            } else {
                                boolean isClaim = false;
                                if (categoryModel.getMilestoneData().get(position).getType().equals("0"))// number target
                                {
                                    if (Integer.parseInt(categoryModel.getMilestoneData().get(position).getNoOfCompleted()) >= Integer.parseInt(categoryModel.getMilestoneData().get(position).getTargetNumber())) {
                                        isClaim = true;
                                    }
                                } else {// points target
                                    if (Integer.parseInt(categoryModel.getMilestoneData().get(position).getEarnedPoints()) >= Integer.parseInt(categoryModel.getMilestoneData().get(position).getTargetPoints())) {
                                        isClaim = true;
                                    }
                                }
                                if (isClaim) {
                                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getMilestoneData().get(position).getIsShowDetails()) && categoryModel.getMilestoneData().get(position).getIsShowDetails().equals("1")) {
//                                        selectedPos = position;
                                        Intent intent = new Intent(RewardPage_Activity.this, MilestoneHistoryActivity.class).putExtra("milestoneId", categoryModel.getMilestoneData().get(position).getId());
                                        startActivity(intent);
//                                        someActivityResultLauncher.launch(intent);
                                    }
                                } else {
                                    Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getMilestoneData().get(position).getScreenNo(), categoryModel.getMilestoneData().get(position).getTitle(), null, categoryModel.getMilestoneData().get(position).getId(), categoryModel.getMilestoneData().get(position).getId(), categoryModel.getMilestoneData().get(position).getIcon());
                                }
                            }
                        }
                    });
                    rvList.setAdapter(adapter);
                    layoutInflate.addView(viewMilestones);
                }
                break;

            case POC_Constants.RewardDataType.NATIVE_AD:
                View viewNativeAd = getLayoutInflater().inflate(R.layout.inflate_native_ad_layout, null);
                FrameLayout fl_adplaceholder = viewNativeAd.findViewById(R.id.fl_adplaceholder);
                TextView lblLoadingAds = viewNativeAd.findViewById(R.id.lblLoadingAds);
                if (Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
                    layoutInflate.addView(viewNativeAd);
                }
                break;
            default:
         /*       View spinView;
//                row_home_one_slider
                if (categoryModel.getType().equals(POC_Constants.RewardDataType.DAILY_REWARDS_CHALLENGE)) {
                    spinView = getLayoutInflater().inflate(R.layout.inflate_reward_spinner,layoutInflate, false);
            }else{
                spinView = getLayoutInflater().inflate(R.layout.common_lottie_gif_img, layoutInflate, false);
            }*/

                View spinView = getLayoutInflater().inflate(R.layout.inflate_reward_spinner, null);
//                ImageView ivBanner = spinView.findViewById(R.id.ivBanner);

                if (!Common_Utils.isStringNullOrEmpty(categoryModel.getimages())) {
                    CardView cardContent = spinView.findViewById(R.id.cardContent);
                    cardContent.setVisibility(View.GONE);
                    ImageView ivIconFullImage = spinView.findViewById(R.id.ivIconFullImage);
                    LottieAnimationView ivLottieFullImage = spinView.findViewById(R.id.ivLottieFullImage);
                    ProgressBar progressBarFullImage = spinView.findViewById(R.id.progressBarFullImage);
                    if (categoryModel.getimages().contains(".json")) {
                        ivIconFullImage.setVisibility(View.GONE);
                        ivLottieFullImage.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(ivLottieFullImage, categoryModel.getimages());
                        ivLottieFullImage.setRepeatCount(LottieDrawable.INFINITE);
                        progressBarFullImage.setVisibility(View.GONE);
                    } else {
                        ivIconFullImage.setVisibility(View.VISIBLE);
                        ivLottieFullImage.setVisibility(View.GONE);
                        Glide.with(RewardPage_Activity.this).load(categoryModel.getimages()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                progressBarFullImage.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(ivIconFullImage);
                    }
                    CardView cardFullImage = spinView.findViewById(R.id.cardFullImage);
                    cardFullImage.setVisibility(View.VISIBLE);
                    cardFullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                try {
                                    if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                        Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                                    } else {
                                        Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                Common_Utils.setEnableDisable(RewardPage_Activity.this, view);
//                                performItemClick(view, categoryModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    LinearLayout layoutSpin = spinView.findViewById(R.id.layoutSpin);
                    TextView lblSpin = spinView.findViewById(R.id.lblTitle);
                    ProgressBar probr = spinView.findViewById(R.id.probr);
                    TextView lblSpinSubTitle = spinView.findViewById(R.id.lblSubTitle);
                    TextView tvLabel1 = spinView.findViewById(R.id.tvLabel);
                    LottieAnimationView ivLottieView = spinView.findViewById(R.id.ivLottie);
                    ivLottieView.setSpeed(0.6f);
                    if (categoryModel.getLabel() != null && categoryModel.getLabel().length() > 0) {
                        tvLabel1.setVisibility(View.VISIBLE);
                        tvLabel1.setText(categoryModel.getLabel());
                    } else {
                        tvLabel1.setVisibility(View.GONE);
                    }

                    ImageView ivIcon = spinView.findViewById(R.id.ivIcon);
                    if (categoryModel.getIcon() != null) {
                        if (categoryModel.getIcon().contains(".json")) {
                            ivIcon.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            Common_Utils.setLottieAnimation(ivLottieView, categoryModel.getIcon());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            probr.setVisibility(View.GONE);
                        } else {
                            ivIcon.setVisibility(View.VISIBLE);
                            ivLottieView.setVisibility(View.GONE);
                            Glide.with(RewardPage_Activity.this)
                                    .load(categoryModel.getIcon())
                                    .override(getResources().getDimensionPixelSize(R.dimen.dim_56))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            probr.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivIcon);
                        }
                 /*   CardView cardFullImage = spinView.findViewById(R.id.cardFullImage);
                    cardFullImage.setVisibility(View.VISIBLE);
                    cardFullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                int position=0;
                                Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/
                    }

               /* if (!Common_Utils.isStringNullOrEmpty(categoryModel.getimages())) {
                    CardView cardContent = spinView.findViewById(R.id.cardContent);
                    cardContent.setVisibility(View.GONE);
                    ImageView ivIconFullImage = spinView.findViewById(R.id.ivIconFullImage);
                    LottieAnimationView ivLottieFullImage = spinView.findViewById(R.id.ivLottieFullImage);
                    ProgressBar progressBarFullImage = spinView.findViewById(R.id.progressBarFullImage);
                    if (categoryModel.getFullImage().contains(".json")) {
                        ivIconFullImage.setVisibility(View.GONE);
                        ivLottieFullImage.setVisibility(View.VISIBLE);
                        CommonMethods.setLottieAnimation(ivLottieFullImage, categoryModel.getFullImage());
                        ivLottieFullImage.setRepeatCount(LottieDrawable.INFINITE);
                        progressBarFullImage.setVisibility(View.GONE);
                    } else {
                        ivIconFullImage.setVisibility(View.VISIBLE);
                        ivLottieFullImage.setVisibility(View.GONE);
                        Glide.with(EarnWithFunActivity.this).load(categoryModel.getFullImage()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                progressBarFullImage.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(ivIconFullImage);
                    }
                    CardView cardFullImage = spinView.findViewById(R.id.cardFullImage);
                    cardFullImage.setVisibility(View.VISIBLE);
                    cardFullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                performItemClick(view, categoryModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }*/

                    if (categoryModel.getType().equals(POC_Constants.RewardDataType.DAILY_REWARDS_CHALLENGE)) {
                        todayDate = categoryModel.getDailyRewardTodayDate();
                        endDate = categoryModel.getDailyRewardEndDate();
                        if (todayDate != null && endDate != null) {
                            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) lblSpinSubTitle.getLayoutParams();
                            param.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_10));
                            lblSpinSubTitle.setLayoutParams(param);
                            layoutTimer = spinView.findViewById(R.id.layoutTimer);
                            layoutTimer.setVisibility(View.VISIBLE);
                            tvTimer = spinView.findViewById(R.id.tvTimer);
                            setTimer();
                        }
                    }
//                ivBanner.setImageResource(categoryModel.getimages);
                    lblSpin.setText(categoryModel.getTitle());
                    lblSpinSubTitle.setText(categoryModel.getSubTitle());
                    layoutSpin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                    Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                                } else {
                                    Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                layoutInflate.addView(spinView);
                break;

            case POC_Constants.RewardDataType.QUICK_TASK:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    quickTaskView = getLayoutInflater().inflate(R.layout.inflate_quick_tasks, layoutInflate, false);
                    RecyclerView rvSliderlist1 = quickTaskView.findViewById(R.id.rvData);
                    TextView textHeader = quickTaskView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        textHeader.setVisibility(View.VISIBLE);
                        textHeader.setText(categoryModel.getTitle());
                    } else {
                        textHeader.setVisibility(View.GONE);
                    }

                    CardView cardView = quickTaskView.findViewById(R.id.cardContent);
                    CardView cardContent1 = quickTaskView.findViewById(R.id.cardContent);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    param.setMargins(getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_10));
                    cardContent1.setLayoutParams(param);

                    cardView.setCardBackgroundColor(Color.parseColor(categoryModel.getBgColor()));
                    quickTasksAdapter = new QuickTasksAdapter(categoryModel.getData(), RewardPage_Activity.this, new QuickTasksAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                selectedQuickTaskPos = position;
                                Activity_Manager.isShowAppOpenAd = false;
                                Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                                startQuickTaskTimer(categoryModel.getData(), position);
                            } else {
                                Common_Utils.NotifyLogin(RewardPage_Activity.this);
                            }
                        }

                    });
                    rvSliderlist1.setLayoutManager(new LinearLayoutManager(RewardPage_Activity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist1.setAdapter(quickTasksAdapter);
                    layoutInflate.addView(quickTaskView);
                }
                break;

            case POC_Constants.RewardDataType.DAILY_TARGET:
                if (categoryModel.getDailyTargetList() != null && categoryModel.getDailyTargetList().size() > 0) {
                    View viewDailyTarget = getLayoutInflater().inflate(R.layout.inflate_home_daily_target, layoutInflate, false);
                    viewDailyTarget.setTag(POC_Constants.RewardDataType.DAILY_TARGET);
                    TextView lblMilestones = viewDailyTarget.findViewById(R.id.lblTitle);
                    TextView lblMilestonesSubTitle = viewDailyTarget.findViewById(R.id.lblSubTitle);
                    CardView carddailytarget = viewDailyTarget.findViewById(R.id.cardContent);
                    FrameLayout.LayoutParams fram = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    fram.setMargins(getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_10));
                    carddailytarget.setLayoutParams(fram);
                    lblMilestones.setText(categoryModel.getTitle());
                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getSubTitle())) {
                        lblMilestonesSubTitle.setText(categoryModel.getSubTitle());
                    } else {
                        lblMilestonesSubTitle.setVisibility(View.GONE);
                    }

                    LinearLayout layoutConten = viewDailyTarget.findViewById(R.id.layoutContent);
                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.bg_sidemenu_social_white);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
                    layoutConten.setBackground(drawable);

                    TextView tvLabelMilestone = viewDailyTarget.findViewById(R.id.tvLabel);
                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getLabel())) {
                        tvLabelMilestone.setText(categoryModel.getLabel());
                        tvLabelMilestone.setVisibility(View.VISIBLE);
                        tvLabelMilestone.startAnimation(blinkAnimation);
                    } else {
                        tvLabelMilestone.setVisibility(View.GONE);
                    }

                    todayDateDailyTarget = categoryModel.getDailyRewardTodayDate();
                    endDateDailyTarget = categoryModel.getDailyRewardEndDate();

                    if (todayDateDailyTarget != null && endDateDailyTarget != null) {
                        tvTimerDailyTarget = viewDailyTarget.findViewById(R.id.tvTime);
                        setTimerDailyTarget();
                    }

                    RecyclerView rvList = viewDailyTarget.findViewById(R.id.rvList);
                    dailyTargetAdapter = new DailyTargetAdapter(categoryModel.getDailyTargetList(), RewardPage_Activity.this, new DailyTargetAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.setEnableDisable(RewardPage_Activity.this, v);
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                Common_Utils.Notify(RewardPage_Activity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                            } else if (!Common_Utils.isStringNullOrEmpty(categoryModel.getDailyTargetList().get(position).getIsClaimed()) && categoryModel.getDailyTargetList().get(position).getIsClaimed().equals("0")) {
                                boolean isClaim = false;
                                if (categoryModel.getDailyTargetList().get(position).getType().equals("0"))// number target
                                {
                                    if (Integer.parseInt(categoryModel.getDailyTargetList().get(position).getNoOfCompleted()) >= Integer.parseInt(categoryModel.getDailyTargetList().get(position).getTargetNumber())) {
                                        isClaim = true;
                                    }
                                } else {// points target
                                    if (Integer.parseInt(categoryModel.getDailyTargetList().get(position).getEarnedPoints()) >= Integer.parseInt(categoryModel.getDailyTargetList().get(position).getTargetPoints())) {
                                        isClaim = true;
                                    }
                                }
                                if (isClaim) {
                                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                        new SaveDailyTargetAsync(RewardPage_Activity.this, categoryModel.getDailyTargetList().get(position).getPoints(), categoryModel.getDailyTargetList().get(position).getId());
                                    } else {
                                        Common_Utils.NotifyLogin(RewardPage_Activity.this);
                                    }
                                } else {
                                    Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getDailyTargetList().get(position).getScreenNo(), categoryModel.getDailyTargetList().get(position).getTitle(), null, categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getIcon());
                                }
                            } else {
                                Common_Utils.Redirect(RewardPage_Activity.this, categoryModel.getDailyTargetList().get(position).getScreenNo(), categoryModel.getDailyTargetList().get(position).getTitle(), null, categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getIcon());
                            }
                        }

                        @Override
                        public void onClaimClick(int position, View v) {
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                if (!Common_Utils.isStringNullOrEmpty(categoryModel.getDailyTargetList().get(position).getIsClaimed()) && categoryModel.getDailyTargetList().get(position).getIsClaimed().equals("0")) {
                                    selectedDailyTargetPos = position;
                                    new SaveDailyTargetAsync(RewardPage_Activity.this, categoryModel.getDailyTargetList().get(position).getPoints(), categoryModel.getDailyTargetList().get(position).getId());
                                }
                            } else {
                                Common_Utils.NotifyLogin(RewardPage_Activity.this);
                            }
                        }
                    });
                    rvList.setAdapter(dailyTargetAdapter);
                    layoutInflate.addView(viewDailyTarget);
                }
                break;
        }
    }

    private void startQuickTaskTimer(List<Home_Data_Model> data, int position) {
        if (timerQuickTask != null) {
            timerQuickTask.cancel();
        }
        timerQuickTask = new CountDownTimer(Integer.parseInt(data.get(position).getDelay()) * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isTimerOver = true;
                timerQuickTask.cancel();
                timerQuickTask = null;
            }
        };
        isTimerSet = true;
        timerQuickTask.start();
    }

    public void setupCPX(LinearLayout llSurvey, TextView txtNoSurvey) {
        App_Controller app = (App_Controller) getApplication();
        app.getCpxResearch().registerListener(new CPXResearchListener() {
            @Override
            public void onSurveysUpdated() {
                List<SurveyItem> surveys = app.getCpxResearch().getSurveys();
                if (surveys.size() == 0) {
                    txtNoSurvey.setVisibility(View.VISIBLE);
                    llSurvey.setVisibility(View.GONE);
                    txtNoSurvey.setText("Currently no surveys available");
                } else {
                    txtNoSurvey.setVisibility(View.GONE);
                    llSurvey.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTransactionsUpdated(@NonNull List<TransactionItem> list) {

            }

            @Override
            public void onSurveysDidOpen() {

            }

            @Override
            public void onSurveysDidClose() {

            }

            @Override
            public void onSurveyDidOpen() {

            }

            @Override
            public void onSurveyDidClose() {

            }
        });
        app.getCpxResearch().setSurveyVisibleIfAvailable(false, RewardPage_Activity.this);

        CPXCardConfiguration cardConfig = new CPXCardConfiguration.Builder()
                .accentColor(Color.parseColor("#6E16E6"))
                .backgroundColor(Color.parseColor("#F9F3FF"))
                .starColor(Color.parseColor("#FF8270"))
                .inactiveStarColor(Color.parseColor("#CAD7DF"))
                .textColor(Color.parseColor("#8E8E93"))
                //.dividerColor(Color.parseColor("#5A7DFE"))
                .cornerRadius(10f)
                .cpxCardStyle(CPXCardStyle.DEFAULT)
                .paddingLeft(15)
                .paddingRight(15)
                .paddingTop(15)
                .paddingBottom(10)
                .fixedCPXCardWidth(120)
                .currencyPrefixImage(R.drawable.ic_coin_img) // set your currency image here!!!
                .hideCurrencyName(true)
                .build();

        app.getCpxResearch().insertCPXResearchCardsIntoContainer(RewardPage_Activity.this, llSurvey, cardConfig);
        app.getCpxResearch().requestSurveyUpdate(false);
    }

    public void setTimer() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            time = Common_Utils.timeDiff(endDate, todayDate);
            timer = new CountDownTimer(time * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText("00:00:00");
                    layoutTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTimerDailyTarget() {
        try {
            if (timerDailyTarget != null) {
                timerDailyTarget.cancel();
            }
            timeDailyTarget = Common_Utils.timeDiff(endDateDailyTarget, todayDateDailyTarget);
            timerDailyTarget = new CountDownTimer(timeDailyTarget * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimerDailyTarget.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimerDailyTarget.setText("00:00:00");
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callRewardScreenDataApi(Activity context) {
//        if (!isResumeCalled) {
        new Get_Reward_Screen_Async(context);
//        }
//        isResumeCalled = true;
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayout, TextView lblLoadingAds) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), RewardPage_Activity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {

                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayout.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayout.setLayoutParams(params);
                    frameLayout.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayout.addView(nativeAdView);
                    frameLayout.setVisibility(View.VISIBLE);
                    lblLoadingAds.setVisibility(View.GONE);
//                    AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    frameLayout.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
//                    AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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


    public void removeAds() {
//        //AppLogger.getInstance().e("removeAds", "removeAds");
        try {
            if (nativeAd != null && nativeAdLoader != null) {
                nativeAdLoader.destroy(nativeAd);
                nativeAd = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurveyDidClose() {

    }

    @Override
    public void onSurveyDidOpen() {

    }

    @Override
    public void onSurveysDidClose() {

    }

    @Override
    public void onSurveysDidOpen() {

    }

    @Override
    public void onSurveysUpdated() {
        App_Controller app = (App_Controller) RewardPage_Activity.this.getApplication();
        List<SurveyItem> surveys = app.getCpxResearch().getSurveys();
        if (surveys.size() == 0) {
            txtSurvey.setVisibility(View.VISIBLE);
            txtSurvey.setText("Currently No Survey Available");
        } else {
            txtSurvey.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTransactionsUpdated(@NonNull List<TransactionItem> list) {

    }

    public void updateQuickTask(boolean isSuccess, String id) {
        try {
            if (isSuccess) {
                for (int i = 0; i < quickTasksAdapter.listTasks.size(); i++) {
                    if (quickTasksAdapter.listTasks.get(i).getId().equals(id)) {
                        quickTasksAdapter.listTasks.remove(i);
                        quickTasksAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                if (quickTasksAdapter.listTasks.size() == 0) {
                    layoutInflate.removeView(quickTaskView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedQuickTaskPos = -1;
    }

    public void onUpdateWalletBalance() {
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                removeAds();
                if (timer != null) {
                    timer.cancel();
                }
                if (timerQuickTask != null) {
                    timerQuickTask.cancel();
                }
                if (timerDailyTarget != null) {
                    timerDailyTarget.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeDailyTargetValues(boolean isSuccess) {
        try {
            if (isSuccess) {
                dailyTargetAdapter.listMilestones.get(selectedDailyTargetPos).setIsClaimed("1");
                dailyTargetAdapter.notifyItemChanged(selectedDailyTargetPos);
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedDailyTargetPos = -1;
    }
}
