package com.reward.cashbazar.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.SimpleTextAdapter;
import com.reward.cashbazar.Async.DownloadShareImageAsync;
import com.reward.cashbazar.Async.Get_Task_History_Async;
import com.reward.cashbazar.Async.Models.ReferResponseModel;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Task_History_Response_Model;
import com.reward.cashbazar.Async.SaveShareTaskAsync;
import com.reward.cashbazar.Async.Task_Image_Upload_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Activity_Manager;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.io.File;
import java.util.List;

public class TaskDetailsActivity extends AppCompatActivity {
    private TextView tvPoints, tvScratchCard, lblLoadingAds;
    private ImageView ivBack, ivHistory, ivSmallIcon, ivBanner, ivVideoTutorial, ivGifFinger1, ivGifFinger2, ivGifFinger3, ivGifFinger4, loadSelectImage;
    private TextView tvTitle, txtPoints, txtTitle, txtSubtitle, txtNote, txtTitleUpload, txtFileName, tvReferTaskPoints;
    private LottieAnimationView ltSmallIcon, ivLottieView;
    private RelativeLayout layoutButton, layoutNote;
    private CardView cardPoint, cardHowToClaim, cardReferTask;
    private Button lInstallBtn, btnUpload;
    private LinearLayout lTaskMain, lWatch,layoutShareOther,layoutCopyLink;
    private WebView webTaskStep, webDisclaimer;
    private LinearLayout relVideoTutorial, lPickImage, lIsImageUpload, layoutScratchCard, layoutShareWA,lnr_unlimite_point;
    private String imagePath;
    private RelativeLayout layoutYoutubeImage, layoutTaskBanner;
    private int PICK_IMAGE = 12;
    private String taskId;
    private LinearLayout layoutPoints, layoutAds, layoutPointss;
    private Task_History_Response_Model objTask;
    private View viewShine, viewShineNote;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private RecyclerView rvFootSteps;
    public Response_Model responseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(TaskDetailsActivity.this);
        setContentView(R.layout.activity_task_details_info);

        taskId = getIntent().getStringExtra("taskId");
        initView();


        new Get_Task_History_Async(TaskDetailsActivity.this, taskId);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    private void initView() {
        layoutTaskBanner = findViewById(R.id.layoutTaskBanner);
        layoutScratchCard = findViewById(R.id.layoutScratchCard);
        tvScratchCard = findViewById(R.id.tvScratchCard);
//        layoutPointss= findViewById(R.id.layoutPointss);
        cardHowToClaim = findViewById(R.id.cardHowToClaim);
        rvFootSteps = findViewById(R.id.rvFootSteps);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvReferTaskPoints = findViewById(R.id.tvReferTaskPoints);
        cardReferTask = findViewById(R.id.cardReferTask);
        layoutShareWA = findViewById(R.id.layoutShareWA);
        layoutShareOther = findViewById(R.id.layoutShareOther);
        layoutShareOther = findViewById(R.id.layoutShareOther);
        layoutCopyLink = findViewById(R.id.layoutCopyLink);
        lnr_unlimite_point = findViewById(R.id.lnr_unlimite_point);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(TaskDetailsActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(TaskDetailsActivity.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
//        ivHistory = findViewById(R.id.ivHistory);
        //   Common_Utils.startRoundAnimation(TaskDetailsInfoActivity.this, ivHistory);
    //        ivHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
//                    startActivity(new Intent(TaskDetailsActivity.this, PointDetailsActivity.class)
//                            .putExtra("type", POC_Constants.HistoryType.TASK)
//                            .putExtra("title", "Task History"));
//                } else {
//                    Common_Utils.NotifyLogin(TaskDetailsActivity.this);
//                }
//            }
//        });
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutYoutubeImage = findViewById(R.id.layoutYoutubeImage);
        lInstallBtn = findViewById(R.id.lInstallBtn);
        viewShine = findViewById(R.id.viewShine);
        layoutButton = findViewById(R.id.layoutButton);
        lTaskMain = findViewById(R.id.lTaskMain);
        tvTitle = findViewById(R.id.tvTitle);
        cardPoint = findViewById(R.id.cardPoint);
        txtFileName = findViewById(R.id.txtFileName);
        txtTitleUpload = findViewById(R.id.txtTitleUpload);
        loadSelectImage = findViewById(R.id.loadSelectImage);
        ivSmallIcon = findViewById(R.id.ivSmallIcon);
        btnUpload = findViewById(R.id.btnUpload);
        lIsImageUpload = findViewById(R.id.lIsImageUpload);
        relVideoTutorial = findViewById(R.id.relVideoTutorial);
        txtPoints = findViewById(R.id.txtPoints);
        lPickImage = findViewById(R.id.lPickImage);
        ivGifFinger1 = findViewById(R.id.ivGifFinger1);
        ivGifFinger2 = findViewById(R.id.ivGifFinger2);
        ivGifFinger3 = findViewById(R.id.ivGifFinger3);
        ivGifFinger4 = findViewById(R.id.ivGifFinger4);
        lWatch = findViewById(R.id.lWatch);
        txtTitle = findViewById(R.id.txtTitle);
        ivVideoTutorial = findViewById(R.id.ivVideoTutorial);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        layoutNote = findViewById(R.id.layoutNote);
        viewShineNote = findViewById(R.id.viewShineNote);
        txtNote = findViewById(R.id.txtNote);
        ivBanner = findViewById(R.id.ivBanner);
        webTaskStep = findViewById(R.id.webTaskStep);
        webDisclaimer = findViewById(R.id.webDisclamier);
        ltSmallIcon = findViewById(R.id.ltSmallIcon);
        ivLottieView = findViewById(R.id.ivLottieView);
        lIsImageUpload.setVisibility(View.GONE);
        lPickImage.setVisibility(View.GONE);
        lTaskMain.setVisibility(View.INVISIBLE);
        layoutButton.setVisibility(View.GONE);

        lPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common_Utils.setEnableDisable(TaskDetailsActivity.this, v);
                if (ContextCompat.checkSelfPermission(TaskDetailsActivity.this.getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(TaskDetailsActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 74);
                } else {
                    Activity_Manager.isShowAppOpenAd = false;
                    txtFileName.setText("Click here to select image");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common_Utils.setEnableDisable(TaskDetailsActivity.this, v);
                if (imagePath != null && !imagePath.isEmpty()) {
                    UploadImage();
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setData(Task_History_Response_Model responseModel) {
        if (responseModel.getTaskDetails() != null) {
            objTask = responseModel;
            if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                Ads_Utils.showAppLovinInterstitialAd(TaskDetailsActivity.this, null);
            } else if (responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(POC_Constants.APPLOVIN_REWARD)) {
                Ads_Utils.showAppLovinRewardedAd(TaskDetailsActivity.this, null);
            }

            LinearLayout layoutBannerAdTop = findViewById(R.id.layoutBannerAdTop);
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (responseModel.getTaskDetails().getIsShowNativeAd() != null && responseModel.getTaskDetails().getIsShowNativeAd().equals("1")) {
                layoutTaskBanner.setVisibility(View.GONE);
                layoutBannerAdTop.setVisibility(View.GONE);
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                layoutAds = findViewById(R.id.layoutAds);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                loadAppLovinNativeAds();
            } else {
                // Load top banner ad
                try {
                    if (Common_Utils.isShowAppLovinBannerAds()) {
                        layoutBannerAdTop.setVisibility(View.VISIBLE);
                        TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceTop);
                        Common_Utils.loadBannerAds(TaskDetailsActivity.this, layoutBannerAdTop, lblAdSpaceBottom);
                    } else {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                    layoutTaskBanner.setVisibility(View.VISIBLE);
                    if (responseModel.getTaskDetails().getImages() != null) {
                        if (responseModel.getTaskDetails().getImages().contains(".json")) {
                            ivBanner.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            Common_Utils.setLottieAnimation(ivLottieView, responseModel.getTaskDetails().getImages());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                        } else {
                            ivBanner.setVisibility(View.VISIBLE);
                            ivLottieView.setVisibility(View.GONE);
                            Glide.with(getApplicationContext())
                                    .load(responseModel.getTaskDetails().getImages())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            ivBanner.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.rectangle_white));
                                            return false;
                                        }
                                    })
                                    .into(ivBanner);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    Common_Utils.loadTopBannerAd(TaskDetailsActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (responseModel.getTaskDetails().getIcon() != null) {
                if (responseModel.getTaskDetails().getIcon().contains(".json")) {
                    ivSmallIcon.setVisibility(View.GONE);
                    ltSmallIcon.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(ltSmallIcon, responseModel.getTaskDetails().getIcon());
                    ltSmallIcon.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    ivSmallIcon.setVisibility(View.VISIBLE);
                    ltSmallIcon.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(responseModel.getTaskDetails().getIcon())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    ivSmallIcon.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.rectangle_white));
                                    return false;
                                }
                            })
                            .into(ivSmallIcon);
                }
            }

            lInstallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        Common_Utils.logFirebaseEvent(TaskDetailsActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Task_Details", "Action Button Clicked");
                        if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getScreenNo()) && responseModel.getTaskDetails().getScreenNo().equals("2")) { // open url in external browser
                            Activity_Manager.isShowAppOpenAd = false;
                        }
                        Common_Utils.Redirect(TaskDetailsActivity.this, responseModel.getTaskDetails().getScreenNo(), responseModel.getTaskDetails().getTitle(), responseModel.getTaskDetails().getUrl(), responseModel.getTaskDetails().getId(), responseModel.getTaskDetails().getId(), responseModel.getTaskDetails().getImages());
                    } else {
                        Common_Utils.NotifyLogin(TaskDetailsActivity.this);
                    }
                }
            });

            if (responseModel.getTaskDetails().getTitle() != null) {
                txtTitle.setText(responseModel.getTaskDetails().getTitle());
                tvTitle.setText(responseModel.getTaskDetails().getTitle());
            }

            if (responseModel.getTaskDetails().getDescription() != null) {
                txtSubtitle.setText(responseModel.getTaskDetails().getDescription());
            }

            if (responseModel.getTaskDetails().getIsImageUpload() != null) {
                if (responseModel.getTaskDetails().getIsImageUpload().matches("0")) {
                    lPickImage.setVisibility(View.GONE);
                    lIsImageUpload.setVisibility(View.GONE);
                } else {
                    lPickImage.setVisibility(View.VISIBLE);
                    lIsImageUpload.setVisibility(View.VISIBLE);
                }
            } else {
                lPickImage.setVisibility(View.GONE);
                lIsImageUpload.setVisibility(View.GONE);
            }

            if (responseModel.getTaskDetails().getPoints() != null) {
                if (responseModel.getTaskDetails().getPoints().matches("0")) {
                    cardPoint.setVisibility(View.GONE);
                } else {
                    cardPoint.setVisibility(View.VISIBLE);
                }
            }

            if (responseModel.getTaskDetails().getPoints() != null) {
                try {
                    txtPoints.setText(responseModel.getTaskDetails().getPoints());
//                    TextView tvTaskPoints = findViewById(R.id.tvTaskPoints);
                    TextView tvTaskRupees = findViewById(R.id.tvTaskRupees);
//                    tvTaskPoints.setText(responseModel.getTaskDetails().getPoints());
                    tvTaskRupees.setText(Common_Utils.convertPointsInINR(responseModel.getTaskDetails().getPoints(), new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getPointValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (responseModel.getTaskDetails().getImageUploadTitle() != null) {
                txtTitleUpload.setText(responseModel.getTaskDetails().getImageUploadTitle());
            }

            if (responseModel.getTaskDetails().getYoutubeLink() != null && !responseModel.getTaskDetails().getYoutubeLink().isEmpty()) {
                lWatch.setVisibility(View.VISIBLE);
                if (responseModel.getTaskDetails().getYoutubeImage() != null && !responseModel.getTaskDetails().getYoutubeImage().isEmpty()) {
                    relVideoTutorial.setVisibility(View.GONE);
                    layoutYoutubeImage.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(responseModel.getTaskDetails().getYoutubeImage())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(ivVideoTutorial);
                } else {
                    layoutYoutubeImage.setVisibility(View.GONE);
                    relVideoTutorial.setVisibility(View.VISIBLE);
                }
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.left_finger))
                        .into(ivGifFinger3);
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.left_finger))
                        .into(ivGifFinger4);
            } else {
                lWatch.setVisibility(View.GONE);
            }

            lWatch.setOnClickListener(v -> {
                if (responseModel.getTaskDetails().getYoutubeLink() != null) {
                    Common_Utils.openUrl(TaskDetailsActivity.this, responseModel.getTaskDetails().getYoutubeLink());
                }
            });
            lTaskMain.setVisibility(View.VISIBLE);
            layoutButton.setVisibility(View.VISIBLE);

            Animation animUpDown = AnimationUtils.loadAnimation(TaskDetailsActivity.this, R.anim.left_right);
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

            if (responseModel.getTaskDetails().getIsShareTask() != null && responseModel.getTaskDetails().getIsShareTask().equals("1")) {
                cardReferTask.setVisibility(View.VISIBLE);
                lnr_unlimite_point.setVisibility(View.VISIBLE);
                tvReferTaskPoints.setText(responseModel.getTaskDetails().getShareTaskPoint());
//                if(!CommonMethodsUtils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareBtnText())){
//                    btnShareLink.setText(responseModel.getTaskDetails().getShareBtnText());
//                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareBtnNote())) {
                    TextView tvShareBtnNote = findViewById(R.id.tvShareBtnNote);
                    tvShareBtnNote.setText(responseModel.getTaskDetails().getShareBtnNote());
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareNote())) {
                    TextView tvShareNote = findViewById(R.id.tvShareNote);
                    tvShareNote.setText(responseModel.getTaskDetails().getShareNote());
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareTitle())) {
                    TextView tvShareTitle = findViewById(R.id.tvShareTitle);
                    tvShareTitle.setText(responseModel.getTaskDetails().getShareTitle());
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getShareMessage())) {
                    TextView tvTopNote = findViewById(R.id.tvTopNote);
                    tvTopNote.setText(responseModel.getTaskDetails().getShareMessage());
                }
                layoutShareWA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            if (Common_Utils.isNetworkAvailable(TaskDetailsActivity.this)) {
                                if (responseModelShare == null) {
                                    new SaveShareTaskAsync(TaskDetailsActivity.this, taskId, "1");
                                } else {
                                    responseModelShare.setType("1");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                Common_Utils.setToast(TaskDetailsActivity.this, "No internet connection");
                            }
                        }
                        else {
                            NotifyLogin(TaskDetailsActivity.this);
                        }
                    }
                });
                layoutShareOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            if (Common_Utils.isNetworkAvailable(TaskDetailsActivity.this)) {
                                if (responseModelShare == null) {
                                    new SaveShareTaskAsync(TaskDetailsActivity.this, taskId, "2");
                                } else {
                                    responseModelShare.setType("2");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                Common_Utils.setToast(TaskDetailsActivity.this, "No internet connection");
                            }
                        }
                      else {
                            NotifyLogin(TaskDetailsActivity.this);
                        }
                    }
                });
                layoutCopyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                            if (Common_Utils.isNetworkAvailable(TaskDetailsActivity.this)) {
                                if (responseModelShare == null) {
                                    new SaveShareTaskAsync(TaskDetailsActivity.this, taskId, "3");
                                } else {
                                    responseModelShare.setType("3");
                                    saveShareTaskOffer(responseModelShare);
                                }
                            } else {
                                Common_Utils.setToast(TaskDetailsActivity.this, "No internet connection");
                            }
                        }
                        else {
                            NotifyLogin(TaskDetailsActivity.this);
                        }
                    }
                });
            } else {
                cardReferTask.setVisibility(View.GONE);
                lnr_unlimite_point.setVisibility(View.GONE);
            }

            if (responseModel.getTaskDetails().getIsScratchCard() != null && responseModel.getTaskDetails().getIsScratchCard().equals("1")) {
                layoutScratchCard.setVisibility(View.VISIBLE);
                tvScratchCard.setText(tvScratchCard.getText() + "\nEarn upto 10-" + responseModel.getTaskDetails().getPoints() + " Bucks.");
                layoutScratchCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(TaskDetailsActivity.this, Activity_Scratch_CouponsGame.class));
                    }
                });
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.left_finger))
                        .into(ivGifFinger1);
                Glide.with(getApplicationContext())
                        .load(getResources().getDrawable(R.drawable.left_finger))
                        .into(ivGifFinger2);
            } else {
                layoutScratchCard.setVisibility(View.GONE);
            }

            if (responseModel.getTaskDetails().getNote() != null && !responseModel.getTaskDetails().getNote().isEmpty()) {
                layoutNote.setVisibility(View.VISIBLE);
                txtNote.setText("Note: " + responseModel.getTaskDetails().getNote());
                Animation animUpDown1 = AnimationUtils.loadAnimation(TaskDetailsActivity.this, R.anim.left_right_slow);
                animUpDown1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewShineNote.startAnimation(animUpDown1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.dim_60), txtNote.getMeasuredHeight());
                viewShineNote.setLayoutParams(rel_btn);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // start the animation
                        viewShineNote.startAnimation(animUpDown1);
                    }
                }, 500);
            } else {
                View sepNote = findViewById(R.id.sepNote);
                sepNote.setVisibility(View.VISIBLE);
                layoutNote.setVisibility(View.GONE);
            }

            if (responseModel.getTaskDetails().getStapes() != null) {
                String start = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Medium.ttf\")}body {font-family: MyFont;font-size: small;text-align: justify;}</style></head><body>";
                String end = "</body></html>";
                String myHtmlString = start + responseModel.getTaskDetails().getStapes() + end;
                webTaskStep.loadData(responseModel.getTaskDetails().getStapes(), "text/html", "UTF-8");
            }

            if (responseModel.getTaskDetails().getTnc() != null) {
                String start = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Medium.ttf\")}body {font-family: MyFont;font-size: small;text-align: justify;}</style></head><body>";
                String end = "</body></html>";
                String myHtmlString = start + responseModel.getTaskDetails().getTnc() + end;
                webDisclaimer.loadData(responseModel.getTaskDetails().getTnc(), "text/html", "UTF-8");
            }

            if (responseModel.getTaskDetails().getBtnColor() != null && responseModel.getTaskDetails().getBtnColor().length() > 0) {
                Drawable mDrawable = ContextCompat.getDrawable(TaskDetailsActivity.this, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getTaskDetails().getBtnColor()), PorterDuff.Mode.SRC_IN));
                lInstallBtn.setBackground(mDrawable);

//                Drawable mDrawable1 = ContextCompat.getDrawable(TaskDetailsInfoActivity.this, R.drawable.bg_points_conversion);
//                mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getTaskDetails().getBtnColor()), PorterDuff.Mode.SRC_IN));
//                layoutPointss.setBackground(mDrawable1);
            }
            if (responseModel.getTaskDetails().getBtnName() != null) {
                lInstallBtn.setText(responseModel.getTaskDetails().getBtnName());
            }
            if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getNote())) {
                Common_Utils.NotifyMessage(TaskDetailsActivity.this, "Important Note!", responseModel.getTaskDetails().getNote(), false);
            }



           /* AppLogger.getInstance().d("TAG", "efffffgawyr5a" + responseModel.getTaskDetails().getFootstep());
            if (responseModel.getTaskDetails().getFootstep() != null && responseModel.getTaskDetails().getFootstep().size() > 0) {
                rvFootSteps.setLayoutManager(new LinearLayoutManager(TaskDetailsActivity.this));
                rvFootSteps.setAdapter(new SimpleTextAdapter(responseModel.getTaskDetails().getFootstep(), TaskDetailsActivity.this));
                rvFootSteps.setVisibility(View.VISIBLE);
                webTaskStep.setVisibility(View.GONE);
            } else if (!Common_Utils.isStringNullOrEmpty(responseModel.getTaskDetails().getStapes())) {
                webTaskStep.loadData(responseModel.getTaskDetails().getStapes(), "text/html", "UTF-8");
            } else {
                cardHowToClaim.setVisibility(View.GONE);
            }*/

            if (responseModel.getTaskDetails().getFootstep()!=null && responseModel.getTaskDetails().getFootstep().size()>0)
            {
                webTaskStep.setVisibility(View.GONE);
                rvFootSteps.setVisibility(View.VISIBLE);

                SimpleTextAdapter  mStepAdapter=new SimpleTextAdapter(responseModel.getTaskDetails().getFootstep(),TaskDetailsActivity.this);
                rvFootSteps.setLayoutManager(new LinearLayoutManager(TaskDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
                rvFootSteps.setAdapter(mStepAdapter);
            }
            else {
                if (responseModel.getTaskDetails().getStapes() != null) {
                    webTaskStep.setVisibility(View.VISIBLE);
                    rvFootSteps.setVisibility(View.GONE);
                    webTaskStep.loadData(responseModel.getTaskDetails().getStapes(), "text/html", "UTF-8");
                }

            }

        }

    }

/*    @Override
    public void onBackPressed() {
        try {
            if (objTask != null && objTask.getAppLuck() != null) {
                Common_Utils.dialogShowAppLuck(TaskDetailsActivity.this, objTask.getAppLuck());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 74:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Activity_Manager.isShowAppOpenAd = false;
                    txtFileName.setText("Click here to select image");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    Common_Utils.setToast(TaskDetailsActivity.this, "Allow permission for storage access!");
                }
                break;

            case 774:
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        shareImageData1();
                    } else {
                        Common_Utils.setToast(TaskDetailsActivity.this, "Allow permission for storage access!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void uploadTaskImageSuccess() {
        imagePath = null;
        txtFileName.setText("Click here to select image");
        loadSelectImage.setImageResource(R.drawable.ic_gallery_img);
        btnUpload.setVisibility(View.GONE);
    }

    public void UploadImage() {
        new Task_Image_Upload_Async(TaskDetailsActivity.this, taskId, objTask.getTaskDetails().getTitle(), imagePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data != null) {
                    try {
                        final Uri selectedUri = data.getData();
                        if (selectedUri != null) {
                            imagePath = Common_Utils.getPathFromURI(TaskDetailsActivity.this, selectedUri);
                            Glide.with(getApplicationContext())
                                    .load(imagePath)
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .into(loadSelectImage);
                            txtFileName.setText(new File(imagePath).getName().toString());
                            btnUpload.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(TaskDetailsActivity.this, "Cannot retrieve selected image", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getLovinNativeID()), TaskDetailsActivity.this);
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

    private ReferResponseModel responseModelShare;

    public void saveShareTaskOffer(ReferResponseModel responseModelShare1) {

        try {
            responseModelShare = responseModelShare1;
            if (responseModelShare != null) {
                if (responseModelShare1.getType().equals("3")) {
                    try {
                        ClipboardManager clipboard = (ClipboardManager) TaskDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", responseModelShare.getShareUrl());
                        clipboard.setPrimaryClip(clip);
                        Common_Utils.setToast(TaskDetailsActivity.this, "Copied!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (responseModelShare.getShareImage() != null && !responseModelShare.getShareImage().isEmpty()) {
                        if (Build.VERSION.SDK_INT <= 32) {
                            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 774);
                            } else {
                                shareImageData1();
                            }
                        } else {
                            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES);
                            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 774);
                            } else {
                                shareImageData1();
                            }
                        }
                    } else {
                        shareImageData1();
                    }
                }
            }
        } catch (Exception e) {


        }
    }

    public void shareImageData1() {
        try {
            Intent share;
            if (responseModelShare.getShareImage().trim().length() > 0 && responseModelShare.getType().equals("1")) {
                File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_PICTURES) + File.separator)));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String[] str = responseModelShare.getShareImage().trim().split("/");
                String extension = "";
                if (str[str.length - 1].contains(".")) {
                    extension = str[str.length - 1].substring(str[str.length - 1].lastIndexOf("."));
                    str[str.length - 1] = str[str.length - 1].substring(0, str[str.length - 1].lastIndexOf(".")) + "_" + taskId;
                }
                if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".gif")) {
                    // extension = "";
                } else {
                    extension = ".png";
                }

                File file = new File(dir, str[str.length - 1] + extension);
                if (file.exists()) {
                    try {
                        share = new Intent(Intent.ACTION_SEND);
                        Uri uri = null;
                        if (Build.VERSION.SDK_INT >= 24) {
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        share.setType("image/*");
                        if (responseModelShare.getShareImage().contains(".gif")) {
                            share.setType("image/gif");
                        } else {
                            share.setType("image/*");
                        }
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        share.setPackage(POC_Constants.whatsappPackageName);
                        share.putExtra(Intent.EXTRA_TEXT, responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString());
                        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(share, PackageManager.MATCH_DEFAULT_ONLY);

                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }

                        startActivity(Intent.createChooser(share, "Share Task"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Common_Utils.isNetworkAvailable(TaskDetailsActivity.this)) {
                    new DownloadShareImageAsync(TaskDetailsActivity.this, file, responseModelShare.getShareImage(), responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString(), "2").execute();
                }
            } else {
                try {
                    share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    share.putExtra(Intent.EXTRA_TEXT, responseModelShare.getType().equals("1") ? responseModelShare.getShareMessageWhatsApp() : Html.fromHtml(responseModelShare.getShareMessage()).toString());
                    if (responseModelShare.getType().equals("1")) {
                        share.setPackage(POC_Constants.whatsappPackageName);
                    }
                    share.setType("text/plain");
                    startActivity(Intent.createChooser(share, "Share Task"));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NotifyLogin(final Activity activity) {
        try {
            if (activity != null) {
                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog1.setContentView(R.layout.dialog_login_required);
                dialog1.setCancelable(false);

                AppCompatButton btnLogin = dialog1.findViewById(R.id.btnLogin);
                btnLogin.setOnClickListener(v -> {
                    dialog1.dismiss();
                    activity.startActivity(new Intent(activity, UserLoginActivity.class));
                });

                AppCompatButton btnNotNow = dialog1.findViewById(R.id.btnNotNow);
                btnNotNow.setOnClickListener(v -> {
                    dialog1.dismiss();
                });

                if (!activity.isFinishing()) {
                    dialog1.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}