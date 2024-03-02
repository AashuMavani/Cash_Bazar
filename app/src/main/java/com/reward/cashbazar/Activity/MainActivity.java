package com.reward.cashbazar.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXCardStyle;
import com.makeopinion.cpxresearchlib.models.SurveyItem;
import com.makeopinion.cpxresearchlib.models.TransactionItem;
import com.onesignal.OneSignal;
import com.reward.cashbazar.Adapter.ActiveMilestonesAdapter;
import com.reward.cashbazar.Adapter.DailyTargetAdapter;
import com.reward.cashbazar.Adapter.DrawerMenuAdapter;
import com.reward.cashbazar.Adapter.EasyEarningAdapter;
import com.reward.cashbazar.Adapter.HomeGiveawayCodesAdapter;
import com.reward.cashbazar.Adapter.Home_Category_Offer_List_Adapter;
import com.reward.cashbazar.Adapter.Home_Horizontal_Category_Adapter;
import com.reward.cashbazar.Adapter.Home_Single_Slider_Adapter;
import com.reward.cashbazar.Adapter.Home_Single_Task_Adapter;
import com.reward.cashbazar.Adapter.Home_Story_Adapter;
import com.reward.cashbazar.Adapter.LiveMilestonesAdapter;
import com.reward.cashbazar.Adapter.QuickTasksAdapter;
import com.reward.cashbazar.Adapter.SideDrawerItemChildView;
import com.reward.cashbazar.Adapter.SideDrawerItemParentView;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.GetWalletBalanceAsync;
import com.reward.cashbazar.Async.Home_Data_Async;
import com.reward.cashbazar.Async.Models.Home_Data_List_Menu;
import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.Async.Models.Menu_Listenu;
import com.reward.cashbazar.Async.Models.Model_MinesweeperData;
import com.reward.cashbazar.Async.Models.Push_Notification_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Sub_ItemList_Item;
import com.reward.cashbazar.Async.Models.User_History;
import com.reward.cashbazar.Async.SaveDailyTargetAsync;
import com.reward.cashbazar.Async.SaveQuickTaskAsync;
import com.reward.cashbazar.Async.Share_Package_Data_Async;
import com.reward.cashbazar.Async.Store_Package_Install_Data;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.recyclerviewpager.Recycler_ViewPager;
import com.reward.cashbazar.customviews.recyclerviewpager.ViewPager_Adapter;
import com.reward.cashbazar.utils.Activity_Manager;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;
import com.skydoves.progressview.ProgressView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements CPXResearchListener {
    private DrawerLayout drawer;
    private NavigationView nav_view_left;
    private boolean isCheckedForUpdate = false;
    private Handler handlerExit;
    private Response_Model responseMain;
    private Dialog dialog, dialogExitDialogAfterInterstitial;
    private boolean doubleBackToExitPressedOnce = false, isExitNativeNotLoaded = false, isHomeSelected = false;
    private MaxAd nativeAdExit;
    private MaxNativeAdLoader nativeAdLoaderExit;
    private FrameLayout frameLayoutExit;
    private LinearLayout layoutHome, layoutReward, layoutTasks, layoutInvite, layoutMe, linearProgress;
    private ImageView ivHome, ivReward, ivTasks, ivMe, ivInvite;
    private Animation blinkAnimation;
    private LottieAnimationView lottieViewTask, lottie_withdraw;
    private int selectedPos = -1, selectedDailyTargetPos = -1;
    private AppCompatButton btnWithdraw;
    private TextView lblHome, lblReward, lblTasks, lblInvite, lblMe, tvName, tvEmail, lblGiveawayCode;
    private ActiveMilestonesAdapter milestoneAdapter;
    private CountDownTimer timer, timerQuickTask, timerDailyTarget;
    private LottieAnimationView progressBarWithdraw;
    private boolean isTimerSet = false, isTimerOver = false;
    private DailyTargetAdapter dailyTargetAdapter;
    private TextView tvNextPayout, tvWithdrawProgress;
    private LiveMilestonesAdapter adapter;
    private CircleImageView ivProfilePic;
    public ImageView ivNotification, ivMenu, ivAdjoe;
    private LottieAnimationView lottieAdjoe, imgStory;
    private ImageView ivAdjoeLeaderboard;
    private RelativeLayout layoutSlider, layoutTasks1;
    private Recycler_ViewPager rvSlider;
    private LinearLayout layoutInflate, layoutPoints, layoutHotOffers, layoutGiveawayCode;
    private TextView txtSurvey;
    private LinearLayout relSlider;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    private TextView tvPoints, tvTimerDailyTarget;
    private View viewMilestones;
    public static boolean isClickOffer = false;
    private BroadcastReceiver watchWebsiteBroadcast;
    private int time, timeDailyTarget;
    private int selectedQuickTaskPos = -1;
    private String todayDate, endDate, todayDateDailyTarget, endDateDailyTarget;
    private QuickTasksAdapter quickTasksAdapter;
    private View quickTaskView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(MainActivity.this);
        setContentView(R.layout.activity_main);

        blinkAnimation = new AlphaAnimation(0.3f, 1.0f);
        blinkAnimation.setDuration(500); //You can manage the blinking time with this parameter
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        if (responseMain == null || (getIntent().getExtras() != null && getIntent().getExtras().containsKey("isFromLogin"))) {
            new Home_Data_Async(MainActivity.this);
        } else {
            setData();
            showHomeDialog();
        }
        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.isFromNotification) || !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
            Common_Utils.InitializeApplovinSDK();
        }

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addAction(Intent.ACTION_INSTALL_PACKAGE);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            intentFilter.addDataScheme("package");
            if (App_Controller.packageInstallBroadcast == null) {
                App_Controller.packageInstallBroadcast = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (!intent.getExtras().containsKey(Intent.EXTRA_REPLACING)) {
                            try {
                                new Share_Package_Data_Async(MainActivity.this, intent.getData().toString().replace("package:", ""));
                                new Store_Package_Install_Data(intent.getData().toString().replace("package:", ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                registerReceiver(App_Controller.packageInstallBroadcast, intentFilter);
                //AppLogger.getInstance().e("PACKAGE onCreate=======", "REGISTER");
            }
//            if (target android 14 -> change functioning of registering receiver like below

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(App_Controller.packageInstallBroadcast, intentFilter, RECEIVER_EXPORTED);
            } else {
                registerReceiver(App_Controller.packageInstallBroadcast, intentFilter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerBroadcast();


    }


    protected void onDestroy() {
        super.onDestroy();
        try {
            if (App_Controller.packageInstallBroadcast != null) {
                unregisterReceiver(App_Controller.packageInstallBroadcast);
                App_Controller.packageInstallBroadcast = null;
                //AppLogger.getInstance().e("PACKAGE onDestroy=======", "UNREGISTER");
            }
        } catch (Exception e) {
            App_Controller.packageInstallBroadcast = null;
            e.printStackTrace();
        }
    }

    private void setData() {
        initView();
        initSlideMenuUI();
        showExitDialog(false);

        if (!isCheckedForUpdate) {
            isCheckedForUpdate = true;
            if (responseMain.getAppVersion() != null) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    if (!responseMain.getAppVersion().equals(version)) {
                        Common_Utils.UpdateApp(MainActivity.this, responseMain.getIsForceUpdate(), responseMain.getAppUrl(), responseMain.getUpdateMessage());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getHotOffersScreenNo()) && !Common_Utils.isStringNullOrEmpty(responseMain.getIsShowHotOffers()) && responseMain.getIsShowHotOffers().equalsIgnoreCase("1")) {
                layoutHotOffers.setVisibility(View.VISIBLE);
                layoutHotOffers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common_Utils.Redirect(MainActivity.this, responseMain.getHotOffersScreenNo(), "", "", "", "", "");
                    }
                });

            } else {
                layoutHotOffers.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowFooterTaskIcon()) && responseMain.getIsShowFooterTaskIcon().equals("1")) {
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getFooterTaskIcon())) {
                    if (responseMain.getFooterTaskIcon().endsWith(".json")) {
                        lottieViewTask.setVisibility(View.VISIBLE);
                        ivTasks.setVisibility(View.GONE);
                        Common_Utils.setLottieAnimation(lottieViewTask, responseMain.getFooterTaskIcon());
                    } else {
                        Glide.with(MainActivity.this)
                                .load(responseMain.getFooterTaskIcon())
                                .into(ivTasks);
                        ivTasks.setVisibility(View.VISIBLE);
                        lottieViewTask.setVisibility(View.GONE);
                    }
                } else {
                    lottieViewTask.setVisibility(View.VISIBLE);
                    ivTasks.setVisibility(View.GONE);
                    lottieViewTask.setAnimation(R.raw.ic_tack_anim);
                }
            } else {
                layoutTasks1.setVisibility(View.GONE);
                layoutTasks.setVisibility(View.GONE);
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
                    rvGiveawayCodes.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvGiveawayCodes.setAdapter(new HomeGiveawayCodesAdapter(list, MainActivity.this, new HomeGiveawayCodesAdapter.ClickListener() {
                        @Override
                        public void onClick(int position, View v, LinearLayout linearLayout) {
                            startActivity(new Intent(MainActivity.this, EverydayGiveawayRewardActivity.class));
                        }
                    }));

                } else {
                    lblGiveawayCode.setText("Have a Giveaway Code?");
                    rvGiveawayCodes.setVisibility(View.GONE);
                }
                rvGiveawayCodes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, EverydayGiveawayRewardActivity.class));
                        Toast.makeText(MainActivity.this, "code", Toast.LENGTH_SHORT).show();
                    }
                });

                layoutGiveawayCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, EverydayGiveawayRewardActivity.class));
                    }
                });
            } else {
                layoutGiveawayCode.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("type")) {
                if (getIntent().getExtras().getString("type").equals("invite")) {
                    performInviteClick();
                } else if (getIntent().getExtras().getString("type").equals("me")) {
                    performMeClick();
                } else if (getIntent().getExtras().getString("type").equals("task")) {
                    performTaskClick();
                } else if (getIntent().getExtras().getString("type").equals("reward")) {
                    performRewardClick();
                } else {
                    performHomeClick();
                }
            } else {
                performHomeClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.isFromNotification)) {
                POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.isFromNotification, false);
                Push_Notification_Model notificationModel = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.notificationData), Push_Notification_Model.class);
                Common_Utils.Redirect(MainActivity.this, notificationModel.getScreenNo(), notificationModel.getTitle(), notificationModel.getUrl(), notificationModel.getId(), notificationModel.getTaskId(), notificationModel.getImage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (MainActivity.this).openDrawer();
            }
        });
        try {
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowPlaytimeIcone()) && responseMain.getIsShowPlaytimeIcone().equalsIgnoreCase("1")) {

                if (responseMain.getImageAdjoeIcon().endsWith(".json")) {
                    lottieAdjoe.setVisibility(View.VISIBLE);
                    lottieAdjoe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common_Utils.openAdjoeOfferWall(MainActivity.this);
                        }
                    });
                    Common_Utils.setLottieAnimation(lottieAdjoe, responseMain.getImageAdjoeIcon());
                } else {
                    ivAdjoe.setVisibility(View.VISIBLE);
                    ivAdjoe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common_Utils.openAdjoeOfferWall(MainActivity.this);
                        }
                    });
                    Glide.with(MainActivity.this)
                            .load(responseMain.getImageAdjoeIcon())
                            .override(getResources().getDimensionPixelSize(R.dimen.dim_32))
                            .into(ivAdjoe);
                }
            } else {
                ivAdjoe.setVisibility(View.GONE);
                lottieAdjoe.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAdjoeLeaderboardIcon()) && responseMain.getIsShowAdjoeLeaderboardIcon().equals("1")) {
            ivAdjoeLeaderboard.setVisibility(View.VISIBLE);
        } else {
            ivAdjoeLeaderboard.setVisibility(View.GONE);
        }


        try {
            if (responseMain.getHomeSlider() != null && responseMain.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Home_Slider_Menu>) responseMain.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new ViewPager_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Common_Utils.Redirect(MainActivity.this, responseMain.getHomeSlider().get(position).getScreenNo(), responseMain.getHomeSlider().get(position).getTitle()
                                , responseMain.getHomeSlider().get(position).getUrl(), responseMain.getHomeSlider().get(position).getId(), null, responseMain.getHomeSlider().get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeNote())) {
                WebView webNote = findViewById(R.id.webNote);
                webNote.getSettings().setJavaScriptEnabled(true);
                webNote.setVisibility(View.VISIBLE);
                webNote.loadDataWithBaseURL(null, responseMain.getHomeNote(), "text/html", "UTF-8", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (layoutInflate != null) {
            layoutInflate.removeAllViews();
        }
        layoutInflate.setVisibility(View.VISIBLE);
        try {
            if (responseMain.getHomeDataList() != null && responseMain.getHomeDataList().size() > 0) {
                for (int i = 0; i < responseMain.getHomeDataList().size(); i++) {
                    try {
                        inflateHomeScreenData(responseMain.getHomeDataList().get(i).getType(), responseMain.getHomeDataList().get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!Common_Utils.isStringNullOrEmpty(responseMain.getWithdrawLottie())) {
                if (responseMain.getWithdrawLottie().endsWith(".json")) {
                    lottie_withdraw.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(lottie_withdraw, responseMain.getWithdrawLottie());
                }
            } else {
                lottie_withdraw.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowWelcomeBonusPopup()) && responseMain.getIsShowWelcomeBonusPopup().equals("1") && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_WELCOME_POPUP_SHOWN)) {
            Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Sign_up", "Sign up");
            showWelcomeBonusPopup(responseMain.getWelcomeBonus());
        }

        updateNextWithdrawAmount();
    }

    private void showWelcomeBonusPopup(String points) {
        Dialog dialogWin = new Dialog(MainActivity.this, android.R.style.Theme_Light);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.setContentView(R.layout.popup_welcome_bonus);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView tvPoint = dialogWin.findViewById(R.id.tvPoints);
//        tvPoint.setText(points);

        LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);
        animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                super.onAnimationStart(animation, isReverse);
                Common_Utils.startTextCountAnimation(tvPoint, points);
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
        Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        try {
            int pt = Integer.parseInt(points);
            lblPoints.setText((pt <= 1 ? "Buck" : "Bucks"));
        } catch (Exception e) {
            e.printStackTrace();
            lblPoints.setText("Bucks");
        }
        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        btnOk.setText("Ok");
        btnOk.setOnClickListener(v -> {
            Ads_Utils.showAppLovinInterstitialAd(MainActivity.this, new Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dialogWin != null) {
                        dialogWin.dismiss();
                    }
                }
            });
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
            POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.IS_WELCOME_POPUP_SHOWN, true);
        }
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
        app.getCpxResearch().setSurveyVisibleIfAvailable(false, MainActivity.this);

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

        app.getCpxResearch().insertCPXResearchCardsIntoContainer(MainActivity.this, llSurvey, cardConfig);
        app.getCpxResearch().requestSurveyUpdate(false);
    }

    private void inflateHomeScreenData(String type, Home_Data_List_Menu categoryModel) {
        switch (type) {


            case POC_Constants.HomeDataType.HORIZONTAL_TASK:
                View viewHorizontaltask = getLayoutInflater().inflate(R.layout.inflate_home_layout1, null);
                RecyclerView rvGridlist1 = viewHorizontaltask.findViewById(R.id.rvIconlist);
                TextView txtGridHeader1 = viewHorizontaltask.findViewById(R.id.txtTitleHeader);

                if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                    txtGridHeader1.setVisibility(View.VISIBLE);
                    txtGridHeader1.setText(categoryModel.getTitle());
                } else {
                    txtGridHeader1.setVisibility(View.GONE);
                }

                rvGridlist1.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                Home_Horizontal_Category_Adapter homehorizontaltaskAdapter = new Home_Horizontal_Category_Adapter(MainActivity.this, categoryModel.getData(), new Home_Horizontal_Category_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                    }
                });
                rvGridlist1.setAdapter(homehorizontaltaskAdapter);
                layoutInflate.addView(viewHorizontaltask);
                break;


            case POC_Constants.HomeDataType.TASK_LIST:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewTaskList = getLayoutInflater().inflate(R.layout.inflate_home_layout, null);
                    RecyclerView taskList = viewTaskList.findViewById(R.id.rvIconlist);
                    TextView taskListHeader = viewTaskList.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        taskListHeader.setVisibility(View.VISIBLE);
                        taskListHeader.setText(categoryModel.getTitle());
                    } else {
                        taskListHeader.setVisibility(View.GONE);
                    }
                    Home_Category_Offer_List_Adapter taskListAdapter = new Home_Category_Offer_List_Adapter(categoryModel.getData(), MainActivity.this, categoryModel.getIsBorder(), categoryModel.getBgColor(), new Home_Category_Offer_List_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    taskList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    taskList.setAdapter(taskListAdapter);
                    layoutInflate.addView(viewTaskList);
                }
                break;
            case POC_Constants.HomeDataType.ICON_LIST:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View iconView = getLayoutInflater().inflate(R.layout.inflate_home_appiconlist, null);
                    RecyclerView rvIconlist = iconView.findViewById(R.id.rvIconlist);
                    TextView txtTitleHeader = iconView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtTitleHeader.setVisibility(View.VISIBLE);
                        txtTitleHeader.setText(categoryModel.getTitle());
                    } else {
                        txtTitleHeader.setVisibility(View.GONE);
                    }
                    Home_Story_Adapter homeStoryAdapter = new Home_Story_Adapter(MainActivity.this, categoryModel.getData(), new Home_Story_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvIconlist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvIconlist.setAdapter(homeStoryAdapter);
                    layoutInflate.addView(iconView);
                }
                break;
            case POC_Constants.HomeDataType.SINGLE_SLIDER:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View iconSingleSlider = getLayoutInflater().inflate(R.layout.inflate_home_layout, null);
                    RecyclerView rvSliderlist = iconSingleSlider.findViewById(R.id.rvIconlist);
                    TextView txtHeader = iconSingleSlider.findViewById(R.id.txtTitleHeader);
                    LinearLayout linearcard = iconSingleSlider.findViewById(R.id.relSlider);
                    FrameLayout.LayoutParams fram = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    fram.setMargins(getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_10));
                    linearcard.setLayoutParams(fram);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtHeader.setVisibility(View.VISIBLE);
                        txtHeader.setText(categoryModel.getTitle());
                    } else {
                        txtHeader.setVisibility(View.GONE);
                    }
                    ////Loge("Size--)", "" + categoryModel.getData().size());
                    Home_Single_Slider_Adapter homeSingleSilderAdapter = new Home_Single_Slider_Adapter(MainActivity.this, categoryModel.getData(), false, new Home_Single_Slider_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvSliderlist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist.setAdapter(homeSingleSilderAdapter);
                    layoutInflate.addView(iconSingleSlider);
                }
                break;
            case POC_Constants.HomeDataType.TWO_GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View twoGrid = getLayoutInflater().inflate(R.layout.inflate_home_grid_laayout, null);
                    RecyclerView rvGridlist = twoGrid.findViewById(R.id.rvIconlist);
                    TextView txtGridHeader = twoGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtGridHeader.setVisibility(View.VISIBLE);
                        txtGridHeader.setText(categoryModel.getTitle());
                    } else {
                        txtGridHeader.setVisibility(View.GONE);
                    }

                    Home_Single_Slider_Adapter homeGridAdpater = new Home_Single_Slider_Adapter(MainActivity.this, categoryModel.getData(), true, new Home_Single_Slider_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvGridlist.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    rvGridlist.setAdapter(homeGridAdpater);
                    layoutInflate.addView(twoGrid);
                }
                break;

            case POC_Constants.HomeDataType.SINGLE_BIG_TASK:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewSingleBigTaskRow = getLayoutInflater().inflate(R.layout.inflate_home_layout, null);
                    RecyclerView rvSingleBiglist = viewSingleBigTaskRow.findViewById(R.id.rvIconlist);
                    TextView txtSingleBigHeader = viewSingleBigTaskRow.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtSingleBigHeader.setVisibility(View.VISIBLE);
                        txtSingleBigHeader.setText(categoryModel.getTitle());
                    } else {
                        txtSingleBigHeader.setVisibility(View.GONE);
                    }
                    Home_Single_Task_Adapter homeSingleBiogtaskAdapter = new Home_Single_Task_Adapter(MainActivity.this, categoryModel.getData(), categoryModel.getPointBackgroundColor(), categoryModel.getPointTextColor(), new Home_Single_Task_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvSingleBiglist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSingleBiglist.setAdapter(homeSingleBiogtaskAdapter);
                    layoutInflate.addView(viewSingleBigTaskRow);
                }
                break;
            case POC_Constants.HomeDataType.NATIVE_AD:
                View viewNativeAd = getLayoutInflater().inflate(R.layout.inflate_native_ad_layout, null);
                FrameLayout fl_adplaceholder = viewNativeAd.findViewById(R.id.fl_adplaceholder);
                TextView lblLoadingAds = viewNativeAd.findViewById(R.id.lblLoadingAds);
                if (Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
                    layoutInflate.addView(viewNativeAd);
                }
                break;
            case POC_Constants.HomeDataType.EARN_GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewEarnGrid = getLayoutInflater().inflate(R.layout.inflate_home_grid_laayout, null);
                    RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                    TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        earnGridHeader.setVisibility(View.VISIBLE);
                        earnGridHeader.setText(categoryModel.getTitle());
                    } else {
                        earnGridHeader.setVisibility(View.GONE);
                    }
                    EasyEarningAdapter earnGridAdapter = new EasyEarningAdapter(MainActivity.this, categoryModel.getData(), new EasyEarningAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    }) {
                        @Override
                        public void onItemClick(int position, View v) {

                        }
                    };
                    gridList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    gridList.setAdapter(earnGridAdapter);
                    layoutInflate.addView(viewEarnGrid);

                }
                break;
            case POC_Constants.HomeDataType.GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewEarnGrid = getLayoutInflater().inflate(R.layout.inflate_home_grid_laayout, null);
                    RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                    TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        earnGridHeader.setVisibility(View.VISIBLE);
                        earnGridHeader.setText(categoryModel.getTitle());
                    } else {
                        earnGridHeader.setVisibility(View.GONE);
                    }
                    EasyEarningAdapter earnGridAdapter = new EasyEarningAdapter(MainActivity.this, categoryModel.getData(), new EasyEarningAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    }) {
                        @Override
                        public void onItemClick(int position, View v) {

                        }
                    };
                    gridList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    gridList.setAdapter(earnGridAdapter);
                    layoutInflate.addView(viewEarnGrid);
                }
                break;

           /* case POC_Constants.HomeDataType.CPXSurvey:
                if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowSurvey()) && responseMain.getIsShowSurvey().equals("1")) {
                    View viewCPXSurvey = getLayoutInflater().inflate(R.layout.inflate_survey_item_menu, null);
                    TextView tvTopSurveys = viewCPXSurvey.findViewById(R.id.tvTopSurveys);
                    LinearLayout layoutSurveys = viewCPXSurvey.findViewById(R.id.layoutSurveys);
                    LinearLayout layoutSurveyList = viewCPXSurvey.findViewById(R.id.layoutSurveyList);
                  *//*  LinearLayout cardContent = viewCPXSurvey.findViewById(R.id.cardContent);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );*//*
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
                                startActivity(new Intent(MainActivity.this, PointDetailsActivity.class)
                                        .putExtra("type", POC_Constants.HistoryType.CPXSurvey)
                                        .putExtra("title", "Survey History"));
                            } else {
                                Common_Utils.NotifyLogin(MainActivity.this);
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
                        TopOffersListAdapter dailyLoginAdapter = new TopOffersListAdapter(MainActivity.this, responseMain.getTop_offers(), new TopOffersListAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                if (responseMain.getTop_offers().get(position).getIsShowDetails() != null && responseMain.getTop_offers().get(position).getIsShowDetails().equals("1")) {
                                    Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                                    intent.putExtra("taskId", responseMain.getTop_offers().get(position).getId());
                                    startActivity(intent);
                                } else {
                                    Common_Utils.Redirect(MainActivity.this, responseMain.getTop_offers().get(position).getScreenNo(), responseMain.getTop_offers().get(position).getTitle()
                                            , responseMain.getTop_offers().get(position).getUrl(), null, responseMain.getTop_offers().get(position).getId(), responseMain.getTop_offers().get(position).getIcon());
                                }
                            }
                        });
                        rvTopOffers.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        rvTopOffers.setAdapter(dailyLoginAdapter);
                    } else {
                        tvTopOffers.setVisibility(View.GONE);
                        rvTopOffers.setVisibility(View.GONE);
                    }
                    setupCPX(layoutSurveyList, txtNoSurvey);
                    layoutInflate.addView(viewCPXSurvey);
                }
                break;*/

            case POC_Constants.HomeDataType.LIVE_CONTEST:
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
                        Drawable mDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_white);
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
                        Glide.with(MainActivity.this).load(categoryModel.getIcon()).override(getResources().getDimensionPixelSize(R.dimen.dim_50)).listener(new RequestListener<Drawable>() {
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

                LinearLayout layoutContentClick = viewLiveContest.findViewById(R.id.layoutContentClick);
                layoutContentClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common_Utils.Redirect(MainActivity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                    }
                });
                layoutInflate.addView(viewLiveContest);
                break;


            case POC_Constants.HomeDataType.LIVE_MILESTONES:
                if (categoryModel.getMilestoneData() != null && categoryModel.getMilestoneData().size() > 0) {
                    viewMilestones = getLayoutInflater().inflate(R.layout.inflate_home_live_milestones, null);
                    viewMilestones.setTag(POC_Constants.HomeDataType.LIVE_MILESTONES);
                    TextView lblMilestones = viewMilestones.findViewById(R.id.lblTitle);
                    TextView tvshowmilestonelist = viewMilestones.findViewById(R.id.tvshowmilestonelist);
                    TextView lblMilestonesSubTitle = viewMilestones.findViewById(R.id.lblSubTitle);
                    AppLogger.getInstance().e("TAG", "==<<>>??" + categoryModel.getTitle());
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
                            startActivity(new Intent(MainActivity.this, Activity_Milestone.class)
                                    .putExtra("type", POC_Constants.HistoryType.LIVE_MILESTONES)
                                    .putExtra("title", "Milestone History"));
                           /* } else {
                                Common_Utils.NotifyLogin(MainActivity.this);
                            }*/
                        }
                    });

                    RecyclerView rvList = viewMilestones.findViewById(R.id.rvList);

                    adapter = new LiveMilestonesAdapter(categoryModel.getMilestoneData(), MainActivity.this, categoryModel.getIconBGColor(), new LiveMilestonesAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.setEnableDisable(MainActivity.this, v);
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                Common_Utils.Notify(MainActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
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
//                                    AppLogger.getInstance().d("TAG==", "<<>>>>>>===" + categoryModel.getMilestoneData().get(position).getIsShowDetails());
                                    if (!Common_Utils.isStringNullOrEmpty(categoryModel.getMilestoneData().get(position).getIsShowDetails()) && categoryModel.getMilestoneData().get(position).getIsShowDetails().equals("1")) {
                                        selectedPos = position;
                                        Intent intent = new Intent(MainActivity.this, MilestoneHistoryActivity.class).putExtra("milestoneId", categoryModel.getMilestoneData().get(position).getId());
                                        someActivityResultLauncher.launch(intent);
                                    }
                                } else {
                                    Common_Utils.Redirect(MainActivity.this, categoryModel.getMilestoneData().get(position).getScreenNo(), categoryModel.getMilestoneData().get(position).getTitle(), null, categoryModel.getMilestoneData().get(position).getId(), categoryModel.getMilestoneData().get(position).getId(), categoryModel.getMilestoneData().get(position).getIcon());
                                }
                            }
                        }
                    });
                    rvList.setAdapter(adapter);
                    layoutInflate.addView(viewMilestones);
                }
                break;


            case POC_Constants.HomeDataType.QUICK_TASK:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    quickTaskView = getLayoutInflater().inflate(R.layout.inflate_quick_tasks, layoutInflate, false);
                    RecyclerView rvSliderlist = quickTaskView.findViewById(R.id.rvData);
                    TextView txtHeader = quickTaskView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtHeader.setVisibility(View.VISIBLE);
                        txtHeader.setText(categoryModel.getTitle());
                    } else {
                        txtHeader.setVisibility(View.GONE);
                    }

                    CardView cardConten1 = quickTaskView.findViewById(R.id.cardContent);
                    cardConten1.setCardBackgroundColor(Color.parseColor(categoryModel.getBgColor()));
                    quickTasksAdapter = new QuickTasksAdapter(categoryModel.getData(), MainActivity.this, new QuickTasksAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                selectedQuickTaskPos = position;
                                Activity_Manager.isShowAppOpenAd = false;
                                Common_Utils.Redirect(MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                                startQuickTaskTimer(categoryModel.getData(), position);
                            } else {
                                Common_Utils.NotifyLogin(MainActivity.this);
                            }
                        }
                    });
                    rvSliderlist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist.setAdapter(quickTasksAdapter);
                    layoutInflate.addView(quickTaskView);
                }
                break;

            case POC_Constants.HomeDataType.DAILY_TARGET:
                if (categoryModel.getDailyTargetList() != null && categoryModel.getDailyTargetList().size() > 0) {
                    View viewDailyTarget = getLayoutInflater().inflate(R.layout.inflate_home_daily_target, layoutInflate, false);
                    viewDailyTarget.setTag(POC_Constants.HomeDataType.DAILY_TARGET);
                    TextView lblMilestones = viewDailyTarget.findViewById(R.id.lblTitle);
                    TextView lblMilestonesSubTitle = viewDailyTarget.findViewById(R.id.lblSubTitle);
                    CardView carddailytarget = viewDailyTarget.findViewById(R.id.cardContent);
                    FrameLayout.LayoutParams fram = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    fram.setMargins(getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_15), getResources().getDimensionPixelSize(R.dimen.dim_10));
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
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
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
                    dailyTargetAdapter = new DailyTargetAdapter(categoryModel.getDailyTargetList(), MainActivity.this, new DailyTargetAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Common_Utils.setEnableDisable(MainActivity.this, v);
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                Common_Utils.Notify(MainActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
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
                                        new SaveDailyTargetAsync(MainActivity.this, categoryModel.getDailyTargetList().get(position).getPoints(), categoryModel.getDailyTargetList().get(position).getId());
                                    } else {
                                        Common_Utils.NotifyLogin(MainActivity.this);
                                    }
                                } else {
                                    Common_Utils.Redirect(MainActivity.this, categoryModel.getDailyTargetList().get(position).getScreenNo(), categoryModel.getDailyTargetList().get(position).getTitle(), null, categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getIcon());
                                }
                            } else {
                                Common_Utils.Redirect(MainActivity.this, categoryModel.getDailyTargetList().get(position).getScreenNo(), categoryModel.getDailyTargetList().get(position).getTitle(), null, categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getId(), categoryModel.getDailyTargetList().get(position).getIcon());
                            }
                        }

                        @Override
                        public void onClaimClick(int position, View v) {
                            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                                if (!Common_Utils.isStringNullOrEmpty(categoryModel.getDailyTargetList().get(position).getIsClaimed()) && categoryModel.getDailyTargetList().get(position).getIsClaimed().equals("0")) {
                                    selectedDailyTargetPos = position;
                                    new SaveDailyTargetAsync(MainActivity.this, categoryModel.getDailyTargetList().get(position).getPoints(), categoryModel.getDailyTargetList().get(position).getId());
                                }
                            } else {
                                Common_Utils.NotifyLogin(MainActivity.this);
                            }
                        }
                    });
                    rvList.setAdapter(dailyTargetAdapter);
                    layoutInflate.addView(viewDailyTarget);
                }
                break;

        }
    }

   /* private void setTimer() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            time = Common_Utils.timeDiff(endDate, todayDate);
            timer = new CountDownTimer(time * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    tvTime.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
//                public void onFinish() {
//                    tvTime.setText("00:00:00");
//                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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

    private void loadAppLovinNativeAds(FrameLayout frameLayout, TextView lblLoadingAds) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), MainActivity.this);
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
                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    frameLayout.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
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

    public void setHomeData() {
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        setData();
    }

    private void initView() {
        layoutHome = findViewById(R.id.layoutHome);
        ivHome = findViewById(R.id.ivHome);
        ivMe = findViewById(R.id.ivMe);
        ivReward = findViewById(R.id.ivReward);
        ivTasks = findViewById(R.id.ivTasks);
        lblTasks = findViewById(R.id.lblTasks);
        ivInvite = findViewById(R.id.ivInvite);
        lblHome = findViewById(R.id.lblHome);
        lblReward = findViewById(R.id.lblReward);
        lottieViewTask = findViewById(R.id.lottieViewTask);
        tvNextPayout = findViewById(R.id.tvNextPayout);
        lblInvite = findViewById(R.id.lblInvite);
        lblMe = findViewById(R.id.lblMe);
        tvWithdrawProgress = findViewById(R.id.tvWithdrawProgress);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        progressBarWithdraw = findViewById(R.id.progressBarWithdraw);
        lottie_withdraw = findViewById(R.id.lottie_withdraw);
        layoutGiveawayCode = findViewById(R.id.layoutGiveawayCode);
        lblGiveawayCode = findViewById(R.id.lblGiveawayCode);
        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);
        ivMenu = findViewById(R.id.ivMenu);
        lottieAdjoe = findViewById(R.id.lottieAdjoe);
        ivAdjoe = findViewById(R.id.ivAdjoe);
        layoutInflate = findViewById(R.id.layoutInflate);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        linearProgress = findViewById(R.id.linearProgress);

        layoutHotOffers = findViewById(R.id.layoutHotOffers);

        if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
            progressBarWithdraw.setVisibility(View.GONE);
        }

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(MainActivity.this, ActivityWithdrawTypes.class));
                } else {
                    Common_Utils.NotifyLogin(MainActivity.this);
                }
            }
        });

        layoutTasks1 = findViewById(R.id.layoutTasks1);
        layoutTasks1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTaskClick();
            }
        });

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(MainActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(MainActivity.this);
                }
            }
        });

        ivAdjoeLeaderboard = findViewById(R.id.ivAdjoeLeaderboard);
        ivAdjoeLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdjoeLeaderboardActivity.class));
            }
        });

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performHomeClick();
            }
        });
        layoutReward = findViewById(R.id.layoutReward);
        layoutReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRewardClick();
            }
        });
        layoutTasks = findViewById(R.id.layoutTasks);
        layoutTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTaskClick();
            }
        });

        layoutInvite = findViewById(R.id.layoutInvite);
        layoutInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performInviteClick();
            }
        });
        layoutMe = findViewById(R.id.layoutMe);
        layoutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performMeClick();
            }
        });


    }

    private void showHomeDialog() {
        try {
            if (responseMain.getHomeDialog() != null) {
                if (POC_SharePrefs.getInstance().getString(POC_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId()).length() == 0
                        || (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getIsShowEverytime()) && responseMain.getHomeDialog().getIsShowEverytime().equals("1"))
                        || !POC_SharePrefs.getInstance().getString(POC_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId()).equals(Common_Utils.getCurrentDate())) {
                    if (Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getPackagename())
                            || (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getPackagename())
                            && !Common_Utils.appInstalledOrNot(MainActivity.this, responseMain.getHomeDialog().getPackagename()))) {
                        POC_SharePrefs.getInstance().putString(POC_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId(), Common_Utils.getCurrentDate());

                        Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Light);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        dialog.setContentView(R.layout.dialog_home_popup);

                        Button btnOk = dialog.findViewById(R.id.btnSubmit);
                        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
                        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
                        ProgressBar probrBanner = dialog.findViewById(R.id.probrBanner);
                        ImageView imgBanner = dialog.findViewById(R.id.imgBanner);
                        txtTitle.setText(responseMain.getHomeDialog().getTitle());
                        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
                        RelativeLayout relPopup = dialog.findViewById(R.id.relPopup);
                        LottieAnimationView ivLottieView = dialog.findViewById(R.id.ivLottieView);
                        txtMessage.setText(responseMain.getHomeDialog().getDescription());
                        if (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getIsForce()) && responseMain.getHomeDialog().getIsForce().equals("1")) {
                            btnCancel.setVisibility(View.GONE);
                        } else {
                            btnCancel.setVisibility(View.VISIBLE);
                        }

                        if (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getBtnName())) {
                            btnOk.setText(responseMain.getHomeDialog().getBtnName());
                        }

                        if (!Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getImage())) {
                            if (responseMain.getHomeDialog().getImage().contains("json")) {
                                probrBanner.setVisibility(View.GONE);
                                imgBanner.setVisibility(View.GONE);
                                ivLottieView.setVisibility(View.VISIBLE);
                                Common_Utils.setLottieAnimation(ivLottieView, responseMain.getHomeDialog().getImage());
                                ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            } else {
                                imgBanner.setVisibility(View.VISIBLE);
                                ivLottieView.setVisibility(View.GONE);
                                Glide.with(MainActivity.this)
                                        .load(responseMain.getHomeDialog().getImage())
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                        .addListener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                probrBanner.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(imgBanner);
                            }
                        } else {
                            imgBanner.setVisibility(View.GONE);
                            probrBanner.setVisibility(View.GONE);
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        relPopup.setOnClickListener(v -> Common_Utils.Redirect(MainActivity.this, responseMain.getHomeDialog().getScreenNo(), responseMain.getHomeDialog().getTitle(), responseMain.getHomeDialog().getUrl(), responseMain.getHomeDialog().getId(), null, responseMain.getHomeDialog().getImage()));
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Common_Utils.Redirect(MainActivity.this, responseMain.getHomeDialog().getScreenNo(), responseMain.getHomeDialog().getTitle(), responseMain.getHomeDialog().getUrl(), responseMain.getHomeDialog().getId(), null, responseMain.getHomeDialog().getImage());
                            }
                        });

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showPushNotificationSettingsDialog();
                                    }
                                }, 500);
                            }
                        });
                        dialog.show();
                    } else {
                        showPushNotificationSettingsDialog();
                    }
                } else {
                    showPushNotificationSettingsDialog();
                }
            } else {
                showPushNotificationSettingsDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPushNotificationSettingsDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OneSignal.promptForPushNotifications();
            }
        }, 2000);
    }

    public void performInviteClick() {
        try {
            isHomeSelected = false;
            ivInvite.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            ivInvite.setImageResource(R.drawable.mail_outline_boader);
            lblInvite.setTextColor(getColor(R.color.colorPrimary));

            ivHome.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivHome.setImageResource(R.drawable.icon_home_outline_boarder);
            lblHome.setTextColor(getColor(R.color.dark_black));

            ivTasks.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivTasks.setImageResource(R.drawable.task);
            lblTasks.setTextColor(getColor(R.color.dark_black));

            ivReward.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivReward.setImageResource(R.drawable.gift_outline_boarder);
            lblReward.setTextColor(getColor(R.color.dark_black));

            ivMe.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivMe.setImageResource(R.drawable.me_outline);
            lblMe.setTextColor(getColor(R.color.dark_black));

            startActivity(new Intent(MainActivity.this, ReferUserActivity.class));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void performHomeClick() {
        try {
            isHomeSelected = true;
            ivInvite.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivInvite.setImageResource(R.drawable.mail_outline_boader);
            lblInvite.setTextColor(getColor(R.color.dark_black));

            ivHome.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            ivHome.setImageResource(R.drawable.ic_home_img);
            lblHome.setTextColor(getColor(R.color.colorPrimary));

            ivTasks.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivTasks.setImageResource(R.drawable.task);
            lblTasks.setTextColor(getColor(R.color.dark_black));

            ivReward.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivReward.setImageResource(R.drawable.gift_outline_boarder);
            lblReward.setTextColor(getColor(R.color.dark_black));

            ivMe.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivMe.setImageResource(R.drawable.me_outline);
            lblMe.setTextColor(getColor(R.color.dark_black));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performRewardClick() {
        try {
            isHomeSelected = false;
            ivInvite.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivInvite.setImageResource(R.drawable.mail_outline_boader);
            lblInvite.setTextColor(getColor(R.color.dark_black));

            ivHome.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivHome.setImageResource(R.drawable.icon_home_outline_boarder);
            lblHome.setTextColor(getColor(R.color.dark_black));

            ivTasks.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivTasks.setImageResource(R.drawable.task);
            lblTasks.setTextColor(getColor(R.color.dark_black));

            ivReward.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            ivReward.setImageResource(R.drawable.gift);
            lblReward.setTextColor(getColor(R.color.colorPrimary));

            ivMe.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivMe.setImageResource(R.drawable.me_outline);
            lblMe.setTextColor(getColor(R.color.dark_black));

            startActivity(new Intent(MainActivity.this, RewardPage_Activity.class));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performMeClick() {
        try {
            isHomeSelected = false;
            ivInvite.setImageResource(R.drawable.mail_outline_boader);
            ivInvite.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            lblInvite.setTextColor(getColor(R.color.dark_black));

            ivHome.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivHome.setImageResource(R.drawable.icon_home_outline_boarder);
            lblHome.setTextColor(getColor(R.color.dark_black));

            ivTasks.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivTasks.setImageResource(R.drawable.task);
            lblTasks.setTextColor(getColor(R.color.dark_black));

            ivReward.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivReward.setImageResource(R.drawable.gift_outline_boarder);
            lblReward.setTextColor(getColor(R.color.dark_black));

            ivMe.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            ivMe.setImageResource(R.drawable.me);
            lblMe.setTextColor(getColor(R.color.colorPrimary));

            startActivity(new Intent(MainActivity.this, MePage_Activity.class));

//            hideAllFragments();
         /*   POC_Fragment_Utils.show(fragmentManager, meFragment);
            meFragment.callGetProfileDataApi(MainActivity.this);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performTaskClick() {
        try {
            isHomeSelected = false;
            ivInvite.setImageResource(R.drawable.mail_outline_boader);
            ivInvite.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            lblInvite.setTextColor(getColor(R.color.dark_black));

            ivHome.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivHome.setImageResource(R.drawable.icon_home_outline_boarder);
            lblHome.setTextColor(getColor(R.color.dark_black));

            ivTasks.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            ivTasks.setImageResource(R.drawable.task_fill);
            lblTasks.setTextColor(getColor(R.color.dark_black));

            ivReward.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivReward.setImageResource(R.drawable.gift_outline_boarder);
            lblReward.setTextColor(getColor(R.color.dark_black));

            ivMe.setImageTintList(ColorStateList.valueOf(getColor(R.color.dark_gray)));
            ivMe.setImageResource(R.drawable.me_outline);
            lblMe.setTextColor(getColor(R.color.dark_black));

            startActivity(new Intent(MainActivity.this, TaskPage_Activity.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Common_Utils.initializeAdJoe(MainActivity.this, false);
        setProfileData();
        performHomeClick();
        updateUserPoints();
        if (isTimerSet && isTimerOver && selectedQuickTaskPos >= 0) {
            new SaveQuickTaskAsync(MainActivity.this, quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getPoints(), quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getId());
        }
        isTimerSet = false;
        isTimerOver = false;

        new GetWalletBalanceAsync(MainActivity.this);

    }


    private void updateUserPoints() {
        try {
            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            unRegisterReceivers();
            if (handlerExit != null) {
                handlerExit.removeCallbacksAndMessages(null);
            }
            if (nativeAdExit != null && nativeAdLoaderExit != null) {
                nativeAdLoaderExit.destroy(nativeAdExit);
                nativeAdExit = null;
                frameLayoutExit = null;
            }
        }
    }

    private void initSlideMenuUI() {
        drawer = findViewById(R.id.drawer_layout);
        nav_view_left = findViewById(R.id.nav_view_left);

        ivProfilePic = nav_view_left.findViewById(R.id.ivProfilePic);
        LinearLayout layoutUserProfile = nav_view_left.findViewById(R.id.layoutUserProfile);
        layoutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    performMeClick();
                } else {
                    startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
                }
            }
        });
        tvName = nav_view_left.findViewById(R.id.tvName);
        tvEmail = nav_view_left.findViewById(R.id.tvEmail);
        setProfileData();

        TextView tvVersionName = nav_view_left.findViewById(R.id.tvVersionName);
        tvVersionName.setText(Common_Utils.getVersionName(MainActivity.this));

        ImageView menuAdBanner = nav_view_left.findViewById(R.id.menuAdBanner);
        if (responseMain.getMenuBanner() != null && !Common_Utils.isStringNullOrEmpty(responseMain.getMenuBanner().getImage())) {
            menuAdBanner.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this)
                    .load(responseMain.getMenuBanner().getImage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(menuAdBanner);
            menuAdBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getMenuBanner().getUrl())) {
                        Common_Utils.openUrl(MainActivity.this, responseMain.getMenuBanner().getUrl());
                    }
                }
            });
        }

        LinearLayout layoutTelegram = nav_view_left.findViewById(R.id.layoutTelegram);
        layoutTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(MainActivity.this, responseMain.getTelegramUrl());
            }
        });
        LinearLayout layoutYoutube = nav_view_left.findViewById(R.id.layoutYoutube);
        layoutYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(MainActivity.this, responseMain.getYoutubeUrl());
            }
        });
        LinearLayout layoutInstagram = nav_view_left.findViewById(R.id.layoutInstagram);
        layoutInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common_Utils.openUrl(MainActivity.this, responseMain.getInstagramUrl());
            }
        });

        RecyclerView rvMenuList = nav_view_left.findViewById(R.id.rvMenuList);
        if (responseMain.getSideMenuList() != null && responseMain.getSideMenuList().size() > 0) {
            List<SideDrawerItemParentView> listSideMenu = new ArrayList<>();
            for (Menu_Listenu objMenu : responseMain.getSideMenuList()) {
                List<SideDrawerItemChildView> subMenuList = new ArrayList<>();
                if (objMenu.getSubMenuList() != null && objMenu.getSubMenuList().size() > 0) {
                    for (Sub_ItemList_Item objSubMenu : objMenu.getSubMenuList()) {
                        subMenuList.add(new SideDrawerItemChildView(objSubMenu));
                    }
                }
                SideDrawerItemParentView obj = new SideDrawerItemParentView(objMenu, subMenuList);
                listSideMenu.add(obj);
            }
            DrawerMenuAdapter mAdapter = new DrawerMenuAdapter(this, listSideMenu);
            rvMenuList.setAdapter(mAdapter);
            rvMenuList.setLayoutManager(new LinearLayoutManager(this));
            rvMenuList.setVisibility(View.VISIBLE);
        } else {
            rvMenuList.setVisibility(View.GONE);
        }
    }

    private void setProfileData() {
        try {
            if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                try {
                    User_History userDetails = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.User_Details), User_History.class);
                    tvEmail.setText(userDetails.getEmailId());
                    tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
                    if (userDetails.getProfileImage() != null) {
                        Glide.with(MainActivity.this).load(userDetails.getProfileImage()).override(getResources().getDimensionPixelSize(R.dimen.dim_90),
                                getResources().getDimensionPixelSize(R.dimen.dim_90)).into(ivProfilePic);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tvEmail.setVisibility(View.GONE);
                tvName.setText("Login / Signup");
            }
            updateNextWithdrawAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openDrawer() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDrawer() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroyView() {
        super.onDestroy();
        removeAds();
    }

    public void removeAds() {
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
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (!isHomeSelected) {
                performHomeClick();
            } else if (doubleBackToExitPressedOnce) {
                if (Common_Utils.isShowAppLovinAds() && responseMain.getIsBackAdsInterstitial() != null) {
                    if (responseMain.getIsBackAdsInterstitial().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                        Ads_Utils.showAppLovinInterstitialAd(MainActivity.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                showFinalExitPopup();
                            }
                        });
                    } else if (responseMain.getIsBackAdsInterstitial().equals(POC_Constants.APPLOVIN_REWARD)) {
                        Ads_Utils.showAppLovinRewardedAd(MainActivity.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                showFinalExitPopup();
                            }
                        });
                    } else {
                        Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home", "Interstitial Ad Not Loaded -> Exit");
                        exitApp();
                    }
                } else {
                    Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home", "Not Show Ad -> Exit");
                    exitApp();
                }
            } else {
                showExitDialog(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFinalExitPopup() {
        try {
            dialogExitDialogAfterInterstitial = new Dialog(MainActivity.this, android.R.style.Theme_Light);
            dialogExitDialogAfterInterstitial.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogExitDialogAfterInterstitial.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogExitDialogAfterInterstitial.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogExitDialogAfterInterstitial.setContentView(R.layout.dialog_app_exit_after_interstitial);
            dialogExitDialogAfterInterstitial.setCancelable(true);
            TextView tvTitle = dialogExitDialogAfterInterstitial.findViewById(R.id.tvTitle);
            tvTitle.setText("Thank You For Using\n" + getString(R.string.app_name) + "!");

            ImageView ivClose = dialogExitDialogAfterInterstitial.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExitDialogAfterInterstitial.dismiss();
                }
            });
            dialogExitDialogAfterInterstitial.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (handlerExit != null)
                        handlerExit.removeCallbacksAndMessages(null);
                }
            });
            dialogExitDialogAfterInterstitial.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        dialogExitDialogAfterInterstitial.dismiss();
                    }
                    return true;
                }
            });

            Button btnOk = dialogExitDialogAfterInterstitial.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home", "Show Interstitial Ad -> Exit");
                    exitApp();
                }
            });

            dialogExitDialogAfterInterstitial.show();
            ProgressView progressView = dialogExitDialogAfterInterstitial.findViewById(R.id.progressBar);
            progressView.progressAnimate();
            progressView.setProgress(100);
            handlerExit = new Handler(Looper.getMainLooper());
            handlerExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home", "Show Interstitial Ad -> Exit");
                    exitApp();
                }
            }, 2500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitApp() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (dialogExitDialogAfterInterstitial != null && dialogExitDialogAfterInterstitial.isShowing()) {
                dialogExitDialogAfterInterstitial.dismiss();
            }
            finishAffinity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExitDialog(boolean isShow) {
        try {
            if (isShow && Common_Utils.isShowAppLovinAds() && responseMain.getIsBackAdsInterstitial() != null && !responseMain.getIsBackAdsInterstitial().equals("0")) {
                try {
                    doubleBackToExitPressedOnce = true;
                    Common_Utils.setToast(this, getString(R.string.tap_to_exit));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (isShow && ((responseMain.getExitDialog() == null && responseMain.getIsShowNativeAdsOnAppExit() != null && responseMain.getIsShowNativeAdsOnAppExit().equals("0")) || isExitNativeNotLoaded)) {
                try {
                    doubleBackToExitPressedOnce = true;
                    Common_Utils.setToast(this, getString(R.string.tap_to_exit));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (dialog == null) {
                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Light);
                dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.setContentView(R.layout.dialog_app_quit);
                dialog.setCancelable(false);

                LinearLayout layoutParent = dialog.findViewById(R.id.layoutParent);
                frameLayoutExit = dialog.findViewById(R.id.fl_adplaceholder);
                TextView tvTapAgainToExit = dialog.findViewById(R.id.tvTapAgainToExit);
                if (responseMain.getExitDialog() != null) {
                    View viewCustomAd = getLayoutInflater().inflate(R.layout.quit_dialog_custom_ad, null);
                    ImageView ivExitDialogImage = viewCustomAd.findViewById(R.id.ad_media);
                    LottieAnimationView ivLottieView = dialog.findViewById(R.id.ivLottieView);
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getImage())) {
                        if (responseMain.getExitDialog().getImage().contains("json")) {
                            ivExitDialogImage.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            Common_Utils.setLottieAnimation(ivLottieView, responseMain.getExitDialog().getImage());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            ivLottieView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.Redirect(MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                                }
                            });
                        } else {
                            Glide.with(MainActivity.this)
                                    .load(responseMain.getExitDialog().getImage())
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .into(ivExitDialogImage);
                            ivExitDialogImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common_Utils.Redirect(MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                                }
                            });
                        }
                    } else {
                        ivExitDialogImage.setVisibility(View.GONE);
                    }
                    TextView tvTitle = viewCustomAd.findViewById(R.id.ad_headline);
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getTitle())) {
                        tvTitle.setText(responseMain.getExitDialog().getTitle());
                    } else {
                        tvTitle.setVisibility(View.GONE);
                    }
                    TextView tvDescription = viewCustomAd.findViewById(R.id.ad_body);
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getDescription())) {
                        tvDescription.setText(responseMain.getExitDialog().getDescription());
                    } else {
                        tvDescription.setVisibility(View.GONE);
                    }
                    Button btnAction = viewCustomAd.findViewById(R.id.ad_call_to_action);
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getBtnName())) {
                        btnAction.setText(responseMain.getExitDialog().getBtnName());
                    }
                    if (!Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getBtnColor())) {
                        btnAction.getBackground().setTint(Color.parseColor(responseMain.getExitDialog().getBtnColor()));
                    }

                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common_Utils.Redirect(MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                        }
                    });
                    // Ensure that the parent view doesn't already contain an ad view.
                    frameLayoutExit.removeAllViews();
                    // Place the AdView into the parent.
                    frameLayoutExit.addView(viewCustomAd);
                    frameLayoutExit.setVisibility(View.VISIBLE);
                } else {
                    if (responseMain.getIsShowNativeAdsOnAppExit() != null && responseMain.getIsShowNativeAdsOnAppExit().equals(POC_Constants.APPlOVIN_AD)) {
                        loadAppLovinNativeAdsExit();
                    } else {
                        isExitNativeNotLoaded = true;
                    }
                }
                tvTapAgainToExit.setOnClickListener(view -> {
                    exitApp();
                });
                layoutParent.setOnClickListener(view -> {
                    dialog.dismiss();
                });
            }
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    doubleBackToExitPressedOnce = false;
                }
            });
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (doubleBackToExitPressedOnce) {
                            Common_Utils.logFirebaseEvent(MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home", isExitNativeNotLoaded ? "Exit Dialog With Custom Ad -> Exit" : "Exit Dialog With Native Ad -> Exit");
                            exitApp();
                        }
                    }
                    return true;
                }
            });
            if (isShow && !dialog.isShowing()) {
                doubleBackToExitPressedOnce = true;
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAdsExit() {
        try {
            nativeAdLoaderExit = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), MainActivity.this);
            nativeAdLoaderExit.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdExit != null) {
                        nativeAdLoaderExit.destroy(nativeAdExit);
                    }
                    nativeAdExit = ad;
                    frameLayoutExit.removeAllViews();

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutExit.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutExit.setLayoutParams(params);
                    frameLayoutExit.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutExit.setBackgroundResource(R.drawable.rectangle_white);
                    frameLayoutExit.addView(nativeAdView);
                    frameLayoutExit.setVisibility(View.VISIBLE);
                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                    isExitNativeNotLoaded = true;
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderExit.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logoutAndCloseApp() {
        Common_Utils.doLogout(MainActivity.this);
        finishAffinity();
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
        App_Controller app = (App_Controller) MainActivity.this.getApplication();
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


    public void setWalletBalance(Model_MinesweeperData responseModel) {

        if (responseModel != null) {
            isClickOffer = false;
            AppLogger.getInstance().e("Point", responseModel.getEarningPoint());
            if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
            }
        }
    }


    public void onUpdateWalletBalance() {
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
        updateNextWithdrawAmount();
    }

    private void updateNextWithdrawAmount() {
        try {
            responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

            tvNextPayout.setText("₹ " + responseMain.getNextWithdrawAmount());
            tvWithdrawProgress.setText(POC_SharePrefs.getInstance().getEarningPointString() + "/" + (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue())));

            int required = Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue());
            int per = Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString()) * 100 / required;
            btnWithdraw.setEnabled(per >= 100);


            int height = linearProgress.getHeight();
            int width = linearProgress.getWidth();
            if (width != 0) {
                ViewGroup.LayoutParams params = progressBarWithdraw.getLayoutParams();
                params.height = height;
                if (Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString()) >= (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue()))) {
                    params.width = params.MATCH_PARENT;
                } else {
                    params.width = (width * Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString())) / (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue()));

                }
                progressBarWithdraw.setLayoutParams(params);
                progressBarWithdraw.playAnimation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* private void updateNextWithdrawAmount() {
        try {
            responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
            tvNextPayout.setText("Next payout for ₹ " + responseMain.getNextWithdrawAmount());
            tvWithdrawProgress.setText(POC_SharePrefs.getInstance().getEarningPointString() + "/" + Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue()));

            progressBarWithdraw.setMax(100);
            int required = Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue());
            int per = Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString()) * 100 / required;
            progressBarWithdraw.setProgress(per);
            btnWithdraw.setEnabled(per >= 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private BroadcastReceiver dataChangedBroadcast;
    private IntentFilter intentFilter;

    public void registerBroadcast() {
        if (dataChangedBroadcast == null) {
            dataChangedBroadcast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //AppLogger.getInstance().e("WATCH WEBSITE", "Broadcast Received==" + intent.getAction());
                    String id = intent.getExtras().getString("id");
                    if (intent.getAction().equals(POC_Constants.DAILY_TARGET_RESULT)) {
                        if (intent.getExtras().getString("status").equals(POC_Constants.STATUS_SUCCESS)) {
                            for (int i = 0; i < dailyTargetAdapter.listMilestones.size(); i++) {
                                if (dailyTargetAdapter.listMilestones.get(i).getId().equals(id)) {
                                    dailyTargetAdapter.listMilestones.get(i).setIsClaimed("1");
                                    dailyTargetAdapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                            updateNextWithdrawAmount();
                        }
                    } else if (intent.getAction().equals(POC_Constants.QUICK_TASK_RESULT)) {
                        if (intent.getExtras().getString("status").equals(POC_Constants.STATUS_SUCCESS)) {
                            for (int i = 0; i < quickTasksAdapter.listTasks.size(); i++) {
                                if (quickTasksAdapter.listTasks.get(i).getId().equals(id)) {
                                    quickTasksAdapter.listTasks.remove(i);
                                    quickTasksAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                            updateNextWithdrawAmount();
                            if (quickTasksAdapter.listTasks.size() == 0) {
                                layoutInflate.removeView(quickTaskView);
                            }
                        }
                        selectedQuickTaskPos = -1;
                    } else if (intent.getAction().equals(POC_Constants.LIVE_MILESTONE_RESULT)) {
                        if (intent.getExtras().getString("status").equals(POC_Constants.STATUS_SUCCESS)) {
//                            AppLogger.getInstance().d("TAG====dgz", "Succesess" + POC_Constants.STATUS_SUCCESS);
                            try {
                                for (int i = 0; i < milestoneAdapter.listMilestones.size(); i++) {
                                    if (milestoneAdapter.listMilestones.get(i).getId().equals(id)) {
                                        milestoneAdapter.listMilestones.remove(i);
                                        milestoneAdapter.notifyItemRemoved(i);
                                        milestoneAdapter.notifyItemRangeChanged(i, milestoneAdapter.listMilestones.size());
                                        break;
                                    }
                                }
                                if (milestoneAdapter.listMilestones.size() == 0) {
                                    layoutInflate.removeView(viewMilestones);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            intentFilter = new IntentFilter();
            intentFilter.addAction(POC_Constants.DAILY_TARGET_RESULT);
            intentFilter.addAction(POC_Constants.LIVE_MILESTONE_RESULT);
            intentFilter.addAction(POC_Constants.QUICK_TASK_RESULT);
            registerReceiver(dataChangedBroadcast, intentFilter);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(watchWebsiteBroadcast, intentFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(watchWebsiteBroadcast, intentFilter);
        }

    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(dataChangedBroadcast, intentFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(dataChangedBroadcast, intentFilter);
        }*/
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

    private void unRegisterReceivers() {
        if (dataChangedBroadcast != null) {
//            AppLogger.getInstance().e("SplashActivity", "Unregister Broadcast");
            unregisterReceiver(dataChangedBroadcast);
            dataChangedBroadcast = null;
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