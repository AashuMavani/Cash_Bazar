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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.reward.cashbazar.Adapter.Task_Offer_List_Adapter;
import com.reward.cashbazar.Async.Get_Task_Offer_List_Async;
import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.TaskOffer;
import com.reward.cashbazar.Async.Models.Task_OfferList_Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.recyclerviewpager.ViewPager_Adapter_Small;
import com.reward.cashbazar.customviews.recyclerviewpager.Recycler_Pager_Small;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;

public class TasksCategoryTypeActivity extends AppCompatActivity {
    private int pageNo = 1;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private TextView tvPoints, lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvTaskList;
    private final List<TaskOffer> listTasks = new ArrayList<>();
    private LinearLayout layoutPoints;
    private ImageView ivHistory;
    private RelativeLayout layoutSlider;
    private Recycler_Pager_Small rvSlider;
    private String taskTypeId;
    private TextView tvTitle;
    private Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(TasksCategoryTypeActivity.this);
        setContentView(R.layout.activity_task_page);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("title")) {
                tvTitle = findViewById(R.id.tvTitle);
                tvTitle.setText(getIntent().getStringExtra("title"));
            }
            taskTypeId = getIntent().getStringExtra("taskTypeId");
        }

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(TasksCategoryTypeActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(TasksCategoryTypeActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //Common_Utils.startRoundAnimation(TasksCategoryTypeActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(TasksCategoryTypeActivity.this, PointDetailsActivity.class)
                            .putExtra("type", POC_Constants.HistoryType.TASK)
                            .putExtra("title", "Task History"));
                } else {
                    Common_Utils.NotifyLogin(TasksCategoryTypeActivity.this);
                }
            }
        });
        rvTaskList = findViewById(R.id.rvTaskList);
        rvTaskList.setAdapter(new Task_Offer_List_Adapter(listTasks, TasksCategoryTypeActivity.this, new Task_Offer_List_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (listTasks.get(position).getIsShowDetails() != null && listTasks.get(position).getIsShowDetails().equals("1")) {
                    Intent intent = new Intent(TasksCategoryTypeActivity.this, TaskDetailsActivity.class);
                    intent.putExtra("taskId", listTasks.get(position).getId());
                    startActivity(intent);
                }
            }
        }));
        rvTaskList.setLayoutManager(new LinearLayoutManager(TasksCategoryTypeActivity.this));
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Get_Task_Offer_List_Async(TasksCategoryTypeActivity.this, taskTypeId, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
        new Get_Task_Offer_List_Async(TasksCategoryTypeActivity.this, taskTypeId, String.valueOf(pageNo));
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setData(Task_OfferList_Response_Model responseModel) {
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            if (responseModel != null && responseModel.getTaskCategoryList() != null && responseModel.getTaskCategoryList().size() > 0) {
                for (int i = 0; i < responseModel.getTaskCategoryList().size(); i++) {
                    if (taskTypeId.equals(responseModel.getTaskCategoryList().get(i).getId())) {
                        tvTitle.setText(responseModel.getTaskCategoryList().get(i).getName());
                    }
                }
            }
            if (listTasks.size() == 0) {
                // load horizontal tasks
                RecyclerView rvAdAppList = findViewById(R.id.rvAdAppList);
                LinearLayout layoutTodayStory = findViewById(R.id.layoutTodayStory);
                if (responseModel.getHorizontalTaskList() != null && responseModel.getHorizontalTaskList().size() > 0) {
                    layoutTodayStory.setVisibility(View.VISIBLE);
                    TextView tvTodayStory = findViewById(R.id.tvTodayStory);
                    tvTodayStory.setText(responseModel.getHorizontalTaskLabel());
                    rvAdAppList.setHasFixedSize(true);
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(TasksCategoryTypeActivity.this, 2);
                    mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    rvAdAppList.setLayoutManager(mGridLayoutManager);
                    Horizontal_Category_List_Adapter adAppListAdapter = new Horizontal_Category_List_Adapter(TasksCategoryTypeActivity.this, (ArrayList<TaskOffer>) responseModel.getHorizontalTaskList(), new Horizontal_Category_List_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            try {
                                if (responseModel.getHorizontalTaskList().get(position).getIsShowDetails() != null && responseModel.getHorizontalTaskList().get(position).getIsShowDetails().equals("1")) {
                                    Intent intent = new Intent(TasksCategoryTypeActivity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", responseModel.getHorizontalTaskList().get(position).getId());
                                    startActivity(intent);
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
            }
            if (responseModel != null && responseModel.getTaskOffers() != null && responseModel.getTaskOffers().size() > 0) {
                int prevItemCount = listTasks.size();
                listTasks.addAll(responseModel.getTaskOffers());
                if (prevItemCount == 0) {
                    rvTaskList.getAdapter().notifyDataSetChanged();
                } else {
                    rvTaskList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getTaskOffers().size());
                }

                numOfPage = responseModel.getTotalPage();
                pageNo = Integer.parseInt(responseModel.getCurrentPage());
            }
            if (listTasks.isEmpty()) {
                layoutAds = findViewById(R.id.layoutAds);
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

        try {
            if (responseModel.getHomeSlider() != null && responseModel.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Home_Slider_Menu>) responseModel.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new ViewPager_Adapter_Small.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Common_Utils.Redirect(TasksCategoryTypeActivity.this, responseModel.getHomeSlider().get(position).getScreenNo(), responseModel.getHomeSlider().get(position).getTitle()
                                , responseModel.getHomeSlider().get(position).getUrl(), responseModel.getHomeSlider().get(position).getId(), null, responseModel.getHomeSlider().get(position).getImage());
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
        if (listTasks.isEmpty())
            ivLottieNoData.playAnimation();
    }


    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), TasksCategoryTypeActivity.this);
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
}