/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Horizontal_Category_List_Adapter;
import com.reward.cashbazar.Adapter.Task_Category_Adapter;
import com.reward.cashbazar.Adapter.Task_Offer_List_Adapter;
import com.reward.cashbazar.Async.Get_Task_Offer_List_Async;
import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.TaskOffer;
import com.reward.cashbazar.Async.Models.Task_Category;
import com.reward.cashbazar.Async.Models.Task_OfferList_Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.recyclerviewpager.Recycler_Pager_Small;
import com.reward.cashbazar.customviews.recyclerviewpager.ViewPager_Adapter_Small;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class TaskPage_Activity extends AppCompatActivity {
    private int pageNo = 1;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private TextView tvPoints, lblLoadingAds, tvAllTasks, tvHighestPayingTask, tvAllTasksCount, tvHighestPayingCount;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvTaskList, rvCategoryList;
    private WebView webTaskStep;
    private final List<TaskOffer> listTasks = new ArrayList<>();
    private boolean isResumeCalled = false;
    private LinearLayout layoutPoints;
    private ImageView iv_left, ivHistory;
    private RelativeLayout layoutSlider;
    private Recycler_Pager_Small rvSlider;
    private ArrayList<Task_Category> categoryList = new ArrayList<>();
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds, lnr_all_task, higest_task;
    private Response_Model responseMain;
    private String selectedTaskType = POC_Constants.TASK_TYPE_ALL;
    private boolean isAdLoaded;
    private Task_Offer_List_Adapter taskAdapter;
    private View viewAll, viewHighestPaying, viewHighestPayingTaskDot, viewAllDot;
    private RecyclerView rvFootSteps;

    public TaskPage_Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(TaskPage_Activity.this);
        setContentView(R.layout.activity_task_page);
        initView();
//        new Get_Task_Offer_List_Async(TaskPage_Activity.this, selectedTaskType, String.valueOf(pageNo));
    }


    private void initView() {
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        rvSlider = findViewById(R.id.rvSlider);
        lnr_all_task = findViewById(R.id.lnr_all_task);
        layoutSlider = findViewById(R.id.layoutSlider);
        iv_left = findViewById(R.id.iv_left);
        higest_task = findViewById(R.id.higest_task);
        tvPoints = findViewById(R.id.tvPoints);
        tvAllTasksCount = findViewById(R.id.tvAllTasksCount);
        tvHighestPayingCount = findViewById(R.id.tvHighestPayingCount);
        rvFootSteps = findViewById(R.id.rvFootSteps);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        webTaskStep = findViewById(R.id.webTaskStep);
        layoutPoints = findViewById(R.id.layoutPoints);
//        viewHighestPayingTaskDot = findViewById(R.id.viewAll);
//        viewHighestPayingTaskDot = findViewById(R.id.viewHighestPayingTaskDot);
//        viewHighestPaying = findViewById(R.id.viewHighestPaying);
//        viewAllDot = findViewById(R.id.viewAllDot);
        layoutAds = findViewById(R.id.layoutAds);


        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(TaskPage_Activity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(TaskPage_Activity.this);
                }
            }
        });


        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //    Common_Utils.startRoundAnimation(TaskPage_Activity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(TaskPage_Activity.this, PointDetailsActivity.class).putExtra("type", POC_Constants.HistoryType.TASK).putExtra("title", "Task History"));
                } else {
                    Common_Utils.NotifyLogin(TaskPage_Activity.this);
                }
            }
        });
        rvTaskList = findViewById(R.id.rvTaskList);
        rvTaskList.setLayoutManager(new LinearLayoutManager(TaskPage_Activity.this));
        taskAdapter = new Task_Offer_List_Adapter(listTasks, TaskPage_Activity.this, new Task_Offer_List_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (listTasks.get(position).getIsShowDetails() != null && listTasks.get(position).getIsShowDetails().equals("1")) {
                    Intent intent = new Intent(TaskPage_Activity.this, TaskDetailsActivity.class);
                    intent.putExtra("taskId", listTasks.get(position).getId());
                    startActivity(intent);
                } else {
                    Common_Utils.Redirect(TaskPage_Activity.this, listTasks.get(position).getScreenNo(), listTasks.get(position).getTitle(), listTasks.get(position).getUrl(), null, listTasks.get(position).getId(), listTasks.get(position).getIcon());
                }
            }
        });
        rvTaskList.setAdapter(taskAdapter);

        rvCategoryList = findViewById(R.id.rvCategoryList);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(TaskPage_Activity.this, LinearLayoutManager.HORIZONTAL, false));
        rvCategoryList.setAdapter(new Task_Category_Adapter(TaskPage_Activity.this, categoryList, new Task_Category_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(TaskPage_Activity.this, TasksCategoryTypeActivity.class);
                intent.putExtra("taskTypeId", categoryList.get(position).getId());
                intent.putExtra("title", categoryList.get(position).getName());
                startActivity(intent);
            }
        }));
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Get_Task_Offer_List_Async(TaskPage_Activity.this, POC_Constants.TASK_TYPE_ALL, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
        new Get_Task_Offer_List_Async(TaskPage_Activity.this, selectedTaskType, String.valueOf(pageNo + 1));

    }

    private Task_OfferList_Response_Model responseModel;

    public void setData(Task_OfferList_Response_Model responseModel1) {
        responseModel = responseModel1;
//        AppLogger.getInstance().e("#setdatatask", new Gson().toJson(responseModel));
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            // load horizontal tasks
            RecyclerView rvAdAppList = findViewById(R.id.rvAdAppList);
            LinearLayout layoutTodayStory = findViewById(R.id.layoutTodayStory);
            if (responseModel.getHorizontalTaskList() != null && responseModel.getHorizontalTaskList().size() > 0) {
//                AppLogger.getInstance().e("TAG", "Horizantal task" + responseModel.getHorizontalTaskLabel());
                layoutTodayStory.setVisibility(View.VISIBLE);

              /*  Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(400); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                layoutTodayStory.startAnimation(anim);*/

//                TextView tvTodayStory = findViewById(R.id.tvTodayStory);
//                tvTodayStory.setText(responseModel.getHorizontalTaskLabel());
                rvAdAppList.setHasFixedSize(true);
                rvAdAppList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


                Horizontal_Category_List_Adapter adAppListAdapter = new Horizontal_Category_List_Adapter(TaskPage_Activity.this, (ArrayList<TaskOffer>) responseModel.getHorizontalTaskList(), new Horizontal_Category_List_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        try {
                            if (responseModel.getHorizontalTaskList().get(position).getIsShowDetails() != null && responseModel.getHorizontalTaskList().get(position).getIsShowDetails().equals("1")) {
                                Intent intent = new Intent(TaskPage_Activity.this, TaskDetailsActivity.class);
                                intent.putExtra("taskId", responseModel.getHorizontalTaskList().get(position).getId());
                                startActivity(intent);
                            } else {
                                Common_Utils.Redirect(TaskPage_Activity.this, responseModel.getHorizontalTaskList().get(position).getScreenNo(), responseModel.getHorizontalTaskList().get(position).getTitle(), responseModel.getHorizontalTaskList().get(position).getUrl(), null, responseModel.getHorizontalTaskList().get(position).getId(), responseModel.getHorizontalTaskList().get(position).getIcon());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                rvAdAppList.setAdapter(adAppListAdapter);
            } else {
                layoutTodayStory.setVisibility(View.GONE);
            }

            tvAllTasks = findViewById(R.id.tvAllTasks);
            tvAllTasks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedTaskType = POC_Constants.TASK_TYPE_ALL;
//                viewHighestPaying.setVisibility(View.GONE);
//                viewAll.setVisibility(View.VISIBLE);
//                viewAllDot.setVisibility(View.VISIBLE);
//                viewHighestPayingTaskDot.setVisibility(View.GONE);
                    tvAllTasks.setTextColor(getColor(R.color.white));
                    lnr_all_task.setBackground(getDrawable(R.drawable.bg_survey_selected));
                    higest_task.setBackgroundColor(getColor(R.color.gray));
                    tvHighestPayingTask.setTextColor(getColor(R.color.task_color));
//                tvHighestPayingCount.setVisibility(View.GONE);
                    callApi();
                }
            });
            tvHighestPayingTask = findViewById(R.id.tvHighestPayingTask);
            tvHighestPayingTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedTaskType = POC_Constants.TASK_TYPE_HIGHEST_PAYING;
                    tvHighestPayingTask.setTextColor(getColor(R.color.white));
//                viewHighestPaying.setVisibility(View.VISIBLE);
//                viewAll.setVisibility(View.GONE);
//                viewAllDot.setVisibility(View.GONE);
//                viewHighestPayingTaskDot.setVisibility(View.VISIBLE);
                    higest_task.setBackground(getDrawable(R.drawable.bg_survey_selected));
                    lnr_all_task.setBackgroundColor(getColor(R.color.gray));
                    tvAllTasks.setTextColor(getColor(R.color.task_color));
//                tvAllTasksCount.setVisibility(View.GONE);
                    tvHighestPayingCount.setVisibility(View.VISIBLE);
                    callApi();
                }
            });


            if (categoryList.size() == 0 && responseModel != null && responseModel.getTaskCategoryList() != null && responseModel.getTaskCategoryList().size() > 0) {
                categoryList.addAll(responseModel.getTaskCategoryList());
                rvCategoryList.getAdapter().notifyDataSetChanged();
                rvCategoryList.setVisibility(View.VISIBLE);
            }
            if (responseModel != null && responseModel.getTaskOffers() != null && responseModel.getTaskOffers().size() > 0) {
                int prevItemCount = listTasks.size();
                listTasks.addAll(responseModel.getTaskOffers());
                //AppLogger.getInstance().e("PREV ITEM = ", "===>" + prevItemCount);
                if (prevItemCount == 0) {
                    taskAdapter.notifyDataSetChanged();
                    if (selectedTaskType.equals(POC_Constants.TASK_TYPE_ALL)) {
                        tvAllTasksCount.setVisibility(View.VISIBLE);
                        tvAllTasksCount.setText("" + responseModel.getTotalIteam());

                        tvHighestPayingCount.setVisibility(View.VISIBLE);
                        tvHighestPayingCount.setText("" + responseModel.getHighPoinCount());

                    } else {
                        tvHighestPayingCount.setVisibility(View.VISIBLE);
                        tvHighestPayingCount.setText("" + responseModel.getTotalIteam());
                    }
                } else {
                    taskAdapter.notifyItemRangeInserted(prevItemCount, responseModel.getTaskOffers().size());
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
                            //AppLogger.getInstance().e("TOP ADS", "===" + responseModel.getTopAds().getImage());
                            Common_Utils.loadTopBannerAd(TaskPage_Activity.this, layoutTopAds, responseModel.getTopAds());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isAdLoaded = true;
            }
            if (listTasks.isEmpty()) {
                layoutAds.setVisibility(View.VISIBLE);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                if (Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds();
                } else {
                    layoutAds.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        AppLogger.getInstance().e("vjerg","avrrrrrrrrd"+responseModel.getHomeSlider());
        try {
            if (responseModel.getHomeSlider() != null && responseModel.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Home_Slider_Menu>) responseModel.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new ViewPager_Adapter_Small.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Common_Utils.Redirect(TaskPage_Activity.this, responseModel.getHomeSlider().get(position).getScreenNo(), responseModel.getHomeSlider().get(position).getTitle(), responseModel.getHomeSlider().get(position).getUrl(), responseModel.getHomeSlider().get(position).getId(), null, responseModel.getHomeSlider().get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rvTaskList.setVisibility(listTasks.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listTasks.isEmpty() ? View.VISIBLE : View.GONE);
        if (listTasks.isEmpty()) ivLottieNoData.playAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), TaskPage_Activity.this);
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
    public void onResume() {
        super.onResume();
        updateUserPoints();
    }

    private void callApi() {
        pageNo = 1;
        numOfPage = 0;
        rvTaskList.setVisibility(View.INVISIBLE);
        listTasks.clear();
        taskAdapter.notifyDataSetChanged();
        layoutAds.setVisibility(View.GONE);
        ivLottieNoData.setVisibility(View.GONE);
        nestedScrollView.scrollTo(0, 0);
//        AppLogger.getInstance().e("TAG", "+++------" + selectedTaskType);
        new Get_Task_Offer_List_Async(TaskPage_Activity.this, selectedTaskType, String.valueOf(pageNo));

    }


    private void updateUserPoints() {
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }
}
