package com.reward.cashbazar.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Withdraw_Category_List_Adapter;
import com.reward.cashbazar.Async.Get_Withdraw_Category_List_Async;
import com.reward.cashbazar.Async.Home_Data_Async;
import com.reward.cashbazar.Async.Models.Redeem_Points;
import com.reward.cashbazar.Async.Models.Remove_App_Dialog;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Withdraw_CategoryList_Response_Model;
import com.reward.cashbazar.Async.Models.Withdraw_List;
import com.reward.cashbazar.Async.Redeem_Points_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ActivityWithdrawTypesList extends AppCompatActivity {
    private RecyclerView rvList;
    private List<Withdraw_List> listWithdrawTypes = new ArrayList<>();
    private TextView lblLoadingAds, tvPoints;
    private LottieAnimationView ivLottieNoData;
    private LinearLayout layoutPoints;
    private Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private ImageView ivHistory,ivBack;
    private boolean doubleBackToExitPressedOnce = false;
    private Dialog dialogExit;
    private Withdraw_CategoryList_Response_Model objData;
    private int selectedPos = -1;
    private boolean isSetData = false;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(ActivityWithdrawTypesList.this);
        setContentView(R.layout.activity_withdraw_types_list);

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ActivityWithdrawTypesList.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ActivityWithdrawTypesList.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
     //   Common_Utils.startRoundAnimation(WithdrawTypesListActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    Intent intent = new Intent(ActivityWithdrawTypesList.this, PointDetailsActivity.class);
                    intent.putExtra("type", POC_Constants.HistoryType.WITHDRAW_HISTORY);
                    intent.putExtra("title", "Withdrawal History");
                    startActivity(intent);
                } else {
                    Common_Utils.NotifyLogin(ActivityWithdrawTypesList.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        rvList = findViewById(R.id.rvList);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        ImageView imBack = findViewById(R.id.ivBack);

        new Get_Withdraw_Category_List_Async(ActivityWithdrawTypesList.this, getIntent().getStringExtra("type"));
    }
    public void setData(Withdraw_CategoryList_Response_Model responseModel) {
        objData = responseModel;

        if (responseModel.getWithdrawList() != null && responseModel.getWithdrawList().size() > 0) {
            Ads_Utils.showAppLovinInterstitialAd(ActivityWithdrawTypesList.this, null);
            listWithdrawTypes.clear();
            listWithdrawTypes.addAll(responseModel.getWithdrawList());
          if (Common_Utils.isShowAppLovinNativeAds()) {
                if (listWithdrawTypes.size() <= 4) {
                    listWithdrawTypes.add(listWithdrawTypes.size(), new Withdraw_List());
                } else {
                    for (int i2 = 0; i2 < this.listWithdrawTypes.size(); i2++) {
                        if ((i2 + 1) % 5 == 0) {
                            listWithdrawTypes.add(i2, new Withdraw_List());
                        }
                    }
                }
            }

            if (!isSetData) {
                Withdraw_Category_List_Adapter adapter = new Withdraw_Category_List_Adapter(listWithdrawTypes, ActivityWithdrawTypesList.this, new Withdraw_Category_List_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        showRedeemDialog(position);
                    }
                });
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(ActivityWithdrawTypesList.this, 1);
                mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvList.setLayoutManager(mGridLayoutManager);
                rvList.setAdapter(adapter);
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
                        Common_Utils.loadTopBannerAd(
                                ActivityWithdrawTypesList.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (responseModel.getExitDialog() != null) {
                    loadExitDialog(ActivityWithdrawTypesList.this, responseModel.getExitDialog());
                }
            } else {
                rvList.getAdapter().notifyDataSetChanged();
            }
            lblLoadingAds = findViewById(R.id.lblLoadingAds);
            layoutAds = findViewById(R.id.layoutAds);
            layoutAds.setVisibility(View.VISIBLE);
            frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
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


    }
    private void showRedeemDialog(int position) {
        selectedPos = -1;
        if (Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString()) >= Integer.parseInt(listWithdrawTypes.get(position).getMinPoint())) {
            Dialog dialogRedeem = new Dialog(ActivityWithdrawTypesList.this, android.R.style.Theme_Light);
            dialogRedeem.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogRedeem.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogRedeem.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogRedeem.setCancelable(true);
            dialogRedeem.setCanceledOnTouchOutside(true);
            dialogRedeem.setContentView(R.layout.popup_withdraw);

            EditText etMobile = dialogRedeem.findViewById(R.id.etMobile);
            if (!Common_Utils.isStringNullOrEmpty(listWithdrawTypes.get(position).getInputType()) && listWithdrawTypes.get(position).getInputType().equals("1")) {
                etMobile.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers. 1 = mobile
            } else {
                etMobile.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); //for email. 2 = email
            }
            TextView tvLabel = dialogRedeem.findViewById(R.id.tvLabel);
            View viewSeparator = dialogRedeem.findViewById(R.id.viewSeparator);
            if (!Common_Utils.isStringNullOrEmpty(listWithdrawTypes.get(position).getLabel())) {
                tvLabel.setVisibility(View.VISIBLE);
                viewSeparator.setVisibility(View.VISIBLE);
                tvLabel.setText(listWithdrawTypes.get(position).getLabel());
            } else {
                tvLabel.setVisibility(View.GONE);
                tvLabel.setVisibility(View.GONE);
            }

            TextView tvHint = dialogRedeem.findViewById(R.id.tvHint);
            tvHint.setText(listWithdrawTypes.get(position).getTitle());

            AppCompatButton btnCancel = dialogRedeem.findViewById(R.id.btnCancel);
            AppCompatButton btnRedeem = dialogRedeem.findViewById(R.id.btnRedeem);

            ImageView ivIconDialog = dialogRedeem.findViewById(R.id.ivIconDailog);
            LottieAnimationView ivLottieViewDialog = dialogRedeem.findViewById(R.id.ivLottieViewDailog);
            ProgressBar probrBanner = dialogRedeem.findViewById(R.id.probrBanner);

            etMobile.setHint(listWithdrawTypes.get(position).getHintName());

            if (listWithdrawTypes.get(position).getIcon() != null) {
                if (listWithdrawTypes.get(position).getIcon().contains(".json")) {
                    ivIconDialog.setVisibility(View.GONE);
                    ivLottieViewDialog.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(ivLottieViewDialog, listWithdrawTypes.get(position).getIcon());
                    ivLottieViewDialog.setRepeatCount(LottieDrawable.INFINITE);
                    ivLottieViewDialog.playAnimation();
                    probrBanner.setVisibility(View.GONE);
                } else {
                    ivIconDialog.setVisibility(View.VISIBLE);
                    ivLottieViewDialog.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(listWithdrawTypes.get(position).getIcon()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    probrBanner.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .override(getResources().getDimensionPixelSize(R.dimen.dim_80)).into(ivIconDialog);
                }
            } else {
                probrBanner.setVisibility(View.GONE);
                ivIconDialog.setVisibility(View.GONE);
                ivLottieViewDialog.setVisibility(View.GONE);
            }
            btnRedeem.setOnClickListener(v -> {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (getIntent().getStringExtra("type").equals("1") && etMobile.getText().toString().trim().length() == 10 && Common_Utils.isValidMobile(etMobile.getText().toString().trim())) {
                        selectedPos = position;
                        dialogRedeem.dismiss();
                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
                    } else if (!getIntent().getStringExtra("type").equals("1") && checkValidation(listWithdrawTypes.get(position).getRegxPatten(), etMobile.getText().toString().trim())) {
                        selectedPos = position;
                        dialogRedeem.dismiss();
                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
                    } else {
                        Common_Utils.Notify(ActivityWithdrawTypesList.this, getString(R.string.app_name), listWithdrawTypes.get(position).getHintName() + " is Invalid!", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnCancel.setOnClickListener(v -> {
                dialogRedeem.dismiss();
            });

            if (!isFinishing() && !dialogRedeem.isShowing()) {
                dialogRedeem.show();
            }
        } else {
            NotifyCoin(ActivityWithdrawTypesList.this);
        }
    }

/*    private void showRedeemDialog(int position) {
        selectedPos = -1;
        if (Integer.parseInt(POC_SharePrefs.getInstance().getEarningPointString()) >= Integer.parseInt(listWithdrawTypes.get(position).getMinPoint())) {
            Dialog dialogRedeem = new Dialog(ActivityWithdrawTypesList.this, android.R.style.Theme_Light);
            dialogRedeem.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogRedeem.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogRedeem.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogRedeem.setCancelable(true);
            dialogRedeem.setCanceledOnTouchOutside(true);
            dialogRedeem.setContentView(R.layout.dialog_redeem_popup);

            EditText etMobile = dialogRedeem.findViewById(R.id.etMobile);
            if (!Common_Utils.isStringNullOrEmpty(listWithdrawTypes.get(position).getInputType()) && listWithdrawTypes.get(position).getInputType().equals("1")) {
                etMobile.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers. 1 = mobile
            } else {
                etMobile.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); //for email. 2 = email
            }

            View viewSeparator = dialogRedeem.findViewById(R.id.viewSeparator);
            ImageView ivCountry = dialogRedeem.findViewById(R.id.ivCountry);
            TextView tvCountryCode = dialogRedeem.findViewById(R.id.tvCountryCode);
            if (!Common_Utils.isStringNullOrEmpty(objData.getCountry()) && objData.getCountry().equalsIgnoreCase("India")) {
                try {
                    CountryPicker countryPicker = new CountryPicker.Builder().with(ActivityWithdrawTypesList.this).listener(null).build();
                    Country selectedCountry = countryPicker.getCountryByName("India");
                    tvCountryCode.setText(selectedCountry.getDialCode());
                    ivCountry.setImageResource(selectedCountry.getFlag());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                viewSeparator.setVisibility(View.GONE);
                ivCountry.setVisibility(View.GONE);
                tvCountryCode.setVisibility(View.GONE);
            }

            TextView tvHint = dialogRedeem.findViewById(R.id.tvHint);
            tvHint.setText(listWithdrawTypes.get(position).getTitle());

            AppCompatButton btnCancel = dialogRedeem.findViewById(R.id.btnCancel);
            AppCompatButton btnRedeem = dialogRedeem.findViewById(R.id.btnRedeem);

            ImageView ivIconDialog = dialogRedeem.findViewById(R.id.ivIconDailog);
            LottieAnimationView ivLottieViewDialog = dialogRedeem.findViewById(R.id.ivLottieViewDailog);
            ProgressBar probrBanner = dialogRedeem.findViewById(R.id.probrBanner);

            etMobile.setHint(listWithdrawTypes.get(position).getHintName());

            if (listWithdrawTypes.get(position).getIcon() != null) {
                if (listWithdrawTypes.get(position).getIcon().contains(".json")) {
                    ivIconDialog.setVisibility(View.GONE);
                    ivLottieViewDialog.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(ivLottieViewDialog, listWithdrawTypes.get(position).getIcon());
                    ivLottieViewDialog.setRepeatCount(LottieDrawable.INFINITE);
                    ivLottieViewDialog.playAnimation();
                    probrBanner.setVisibility(View.GONE);
                } else {
                    ivIconDialog.setVisibility(View.VISIBLE);
                    ivLottieViewDialog.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(listWithdrawTypes.get(position).getIcon()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    probrBanner.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .override(getResources().getDimensionPixelSize(R.dimen.dim_80)).into(ivIconDialog);
                }
            } else {
                probrBanner.setVisibility(View.GONE);
                ivIconDialog.setVisibility(View.GONE);
                ivLottieViewDialog.setVisibility(View.GONE);
            }

//            btnRedeem.setOnClickListener(v -> {
//                try {
//                    if (getIntent().getStringExtra("type").equals("1") && etMobile.getText().toString().trim().length() == 10 && POC_Common_Utils.isValidMobile(etMobile.getText().toString().trim())) {
//                        selectedPos = position;
//                        dialogRedeem.dismiss();
//                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
//                    } else if (!getIntent().getStringExtra("type").equals("1") && checkValidation(listWithdrawTypes.get(position).getRegxPatten(), etMobile.getText().toString().trim())) {
//                        selectedPos = position;
//                        dialogRedeem.dismiss();
//                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
//                    } else {
//                        POC_Common_Utils.Notify(ActivityWithdrawTypesList.this, getString(R.string.app_name), listWithdrawTypes.get(position).getHintName() + " is Invalid!", false);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });


            btnRedeem.setOnClickListener(v -> {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (getIntent().getStringExtra("type").equals("1") && etMobile.getText().toString().trim().length() == 10 && Common_Utils.isValidMobile(etMobile.getText().toString().trim())) {
                        selectedPos = position;
                        dialogRedeem.dismiss();
                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
                    } else if (!getIntent().getStringExtra("type").equals("1") && checkValidation(listWithdrawTypes.get(position).getRegxPatten(), etMobile.getText().toString().trim())) {
                        selectedPos = position;
                        dialogRedeem.dismiss();
                        new Redeem_Points_Async(ActivityWithdrawTypesList.this, listWithdrawTypes.get(position).getId(), listWithdrawTypes.get(position).getTitle(), listWithdrawTypes.get(position).getType(), etMobile.getText().toString().trim());
                    } else {
                        Common_Utils.Notify(ActivityWithdrawTypesList.this, getString(R.string.app_name), listWithdrawTypes.get(position).getHintName() + " is Invalid!", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnCancel.setOnClickListener(v -> {
                dialogRedeem.dismiss();
            });

            if (!isFinishing() && !dialogRedeem.isShowing()) {
                dialogRedeem.show();
            }
        } else {
            NotifyCoin(ActivityWithdrawTypesList.this);
        }
    }*/

    public boolean checkValidation(String valPattern, String val) {
        if (!Pattern.matches(valPattern, val)) {
            return false;
        } else {
            return true;
        }
    }

    public void checkWithdraw(Redeem_Points responseModel) {
        if (responseModel != null) {
            if (responseModel.getStatus().matches(POC_Constants.STATUS_SUCCESS)) {
                Common_Utils.logFirebaseEvent(ActivityWithdrawTypesList.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Withdraw", "Withdraw Success -> " + listWithdrawTypes.get(selectedPos).getTitle());
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());
                if (!Common_Utils.isStringNullOrEmpty(objData.getIsRateus()) && objData.getIsRateus().equals("1") && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_REVIEW_GIVEN)) {
                    Ads_Utils.showAppLovinRewardedAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            SuccessDialog(ActivityWithdrawTypesList.this, getString(R.string.app_name), responseModel.getMessage(), responseModel.getTxnStatus(),true);
                        }
                    });
                }else{
                    AppLogger.getInstance().d("TAG", "checkWithdraw: =====)"  + responseModel.getTxnStatus());
                    SuccessDialog(ActivityWithdrawTypesList.this, getString(R.string.app_name), responseModel.getMessage(), responseModel.getTxnStatus(),false);
                }

            } else {
                Common_Utils.logFirebaseEvent(ActivityWithdrawTypesList.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Withdraw", "Withdraw Fail -> " + listWithdrawTypes.get(selectedPos).getTitle());
                showFailDialog(getString(R.string.app_name), responseModel.getMessage());
            }
        } else {
            Common_Utils.logFirebaseEvent(ActivityWithdrawTypesList.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Withdraw", "Withdraw Error -> " + listWithdrawTypes.get(selectedPos).getTitle());
            Common_Utils.Notify(ActivityWithdrawTypesList.this, getString(R.string.app_name), "Something went wrong, please try again later.", false);
        }
    }

    private void showFailDialog(String title, String message) {
        try {
            final Dialog dialog1 = new Dialog(ActivityWithdrawTypesList.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.dialog_notification);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                Ads_Utils.showAppLovinRewardedAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
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

    public void loadExitDialog(Activity activity, Remove_App_Dialog popData) {
        if (activity != null && popData != null) {
            dialogExit = new Dialog(activity, android.R.style.Theme_Light);
            dialogExit.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogExit.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogExit.setContentView(R.layout.dialog_home_popup);
            dialogExit.setCancelable(true);

            Button btnOk = dialogExit.findViewById(R.id.btnSubmit);

            TextView txtTitle = dialogExit.findViewById(R.id.txtTitle);
            txtTitle.setText(popData.getTitle());

            TextView btnCancel = dialogExit.findViewById(R.id.btnCancel);

            ProgressBar probrBanner = dialogExit.findViewById(R.id.probrBanner);
            ImageView imgBanner = dialogExit.findViewById(R.id.imgBanner);
            RelativeLayout relPopup = dialogExit.findViewById(R.id.relPopup);
            LottieAnimationView ivLottieView = dialogExit.findViewById(R.id.ivLottieView);

            TextView txtMessage = dialogExit.findViewById(R.id.txtMessage);
            txtMessage.setText(popData.getDescription());

            btnCancel.setVisibility(View.GONE);

            if (!Common_Utils.isStringNullOrEmpty(popData.getBtnName())) {
                btnOk.setText(popData.getBtnName());
            }
            if (!Common_Utils.isStringNullOrEmpty(popData.getBtnColor())) {
                Drawable mDrawable = ContextCompat.getDrawable(ActivityWithdrawTypesList.this, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(popData.getBtnColor()), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            }
            if (!Common_Utils.isStringNullOrEmpty(popData.getImage())) {
                if (popData.getImage().contains("json")) {
                    probrBanner.setVisibility(View.GONE);
                    imgBanner.setVisibility(View.GONE);
                    ivLottieView.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(ivLottieView, popData.getImage());
                    ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    imgBanner.setVisibility(View.VISIBLE);
                    ivLottieView.setVisibility(View.GONE);
                    Glide.with(ActivityWithdrawTypesList.this).load(popData.getImage()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            probrBanner.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imgBanner);
                }
            } else {
                imgBanner.setVisibility(View.GONE);
                probrBanner.setVisibility(View.GONE);
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogExit.dismiss();
                }
            });
            relPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExit.dismiss();
                    if (popData.getIsShowAds() != null && popData.getIsShowAds().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                        Ads_Utils.showAppLovinInterstitialAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                            }
                        });
                    } else if (popData.getIsShowAds() != null && popData.getIsShowAds().equals(POC_Constants.APPLOVIN_REWARD)) {
                        Ads_Utils.showAppLovinRewardedAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                            }
                        });
                    } else {
                        Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                    }
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExit.dismiss();
                    if (popData.getIsShowAds() != null && popData.getIsShowAds().equals(POC_Constants.APPLOVIN_INTERSTITIAL)) {
                        Ads_Utils.showAppLovinInterstitialAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                            }
                        });
                    } else if (popData.getIsShowAds() != null && popData.getIsShowAds().equals(POC_Constants.APPLOVIN_REWARD)) {
                        Ads_Utils.showAppLovinRewardedAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                            }
                        });
                    } else {
                        Common_Utils.Redirect(activity, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage());
                    }
                }
            });
        }
    }

    public void SuccessDialog(final Activity activity, String title, String message, String txtStatus, boolean isShowRate) {
        ////Log.e("title1--)", "" + title + "--)" + message);
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.dialog_notification_success);
            dialog1.setCancelable(false);
            Button btnOk = dialog1.findViewById(R.id.btnOk);
            LottieAnimationView lottiePlay = dialog1.findViewById(R.id.animation_view);

            //AppLogger.getInstance().e("txtStatus--)", "" + txtStatus);
            if (txtStatus.matches("1")) {
                lottiePlay.setVisibility(View.VISIBLE);
                lottiePlay.setAnimation(R.raw.success);
                lottiePlay.setRepeatCount(LottieDrawable.INFINITE);
                lottiePlay.playAnimation();
                Drawable mDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            } else if (txtStatus.matches("0")) {
                lottiePlay.setVisibility(View.VISIBLE);
                lottiePlay.setAnimation(R.raw.pending);
                lottiePlay.setRepeatCount(LottieDrawable.INFINITE);
                lottiePlay.playAnimation();
                Drawable mDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.orange_yellow), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            } else if (txtStatus.matches("2")) {
                lottiePlay.setVisibility(View.VISIBLE);
                lottiePlay.setAnimation(R.raw.refund);
                lottiePlay.setRepeatCount(LottieDrawable.INFINITE);
                lottiePlay.playAnimation();
                Drawable mDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.red), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            } else if (txtStatus.matches("3")) {
                lottiePlay.setVisibility(View.VISIBLE);
                lottiePlay.setAnimation(R.raw.refund);
                lottiePlay.setRepeatCount(LottieDrawable.INFINITE);
                lottiePlay.playAnimation();
                Drawable mDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.red), PorterDuff.Mode.SRC_IN));
                btnOk.setBackground(mDrawable);
            }

            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                if (isShowRate) {
                    if (!activity.isFinishing()) {
                        dialog1.dismiss();
                    }
                    Common_Utils.showRatePopup(ActivityWithdrawTypesList.this);
                } else {
                    Ads_Utils.showAppLovinRewardedAd(ActivityWithdrawTypesList.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {

                            if (!activity.isFinishing()) {
                                dialog1.dismiss();
                            }
                        }
                    });
                }
            });dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    new Get_Withdraw_Category_List_Async(ActivityWithdrawTypesList.this, getIntent().getStringExtra("type"));
                }
            });
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    public void NotifyCoin(Activity activity) {
        if (activity != null) {
            Dialog dialog1 = new Dialog(ActivityWithdrawTypesList.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setCancelable(true);
            dialog1.setCanceledOnTouchOutside(true);
            dialog1.setContentView(R.layout.dialog_earn_more_point);

            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setTextColor(getColor(R.color.red));
            tvTitle.setText("Not Enough Bucks!");

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText("You don't have enough bucks to withdraw. Earn more bucks and then try again.");

            View viewShine = dialog1.findViewById(R.id.viewShine);
            Animation animUpDown = AnimationUtils.loadAnimation(ActivityWithdrawTypesList.this, R.anim.left_right);
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

            Button btnEarnMore = dialog1.findViewById(R.id.btnEarnMore);
            btnEarnMore.setOnClickListener(v -> {
                if (!activity.isFinishing()) {
                    dialog1.dismiss();
                }
                Intent intent = new Intent(ActivityWithdrawTypesList.this, RewardPage_Activity.class);
                intent.putExtra("taskTypeId", POC_Constants.TASK_TYPE_ALL);
                intent.putExtra("title", "Tasks");
                startActivity(intent);
            });
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new Home_Data_Async(ActivityWithdrawTypesList.this);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        if (dialogExit != null) {
            if (!dialogExit.isShowing()) {
                dialogExit.show();
            }
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), ActivityWithdrawTypesList.this);
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