package com.reward.cashbazar.Activity;

import static android.view.View.GONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.User_History;
import com.reward.cashbazar.Async.Feedback_Submit_Async;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Activity_Manager;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.io.File;

public class SubmitFeedbackActivity extends AppCompatActivity {

    private ImageView ivFAQ, ivImage;
    private AppCompatButton btnSubmit;
    private Response_Model responseMain;
    private LinearLayout layoutImage,layoutAds;
    private FrameLayout frameLayoutNativeAd;
    private User_History userDetails;
    private MaxNativeAdLoader nativeAdLoader;
    private EditText etFeedback;
    private String selectedImage;

    private TextView lblLoadingAds;
    private MaxAd nativeAd;
    public final int GALLERY_REQUEST_CODE = 105, Request_Storage_resize = 200;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(SubmitFeedbackActivity.this);
        setContentView(R.layout.activity_submit_feedback);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        userDetails = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.User_Details), User_History.class);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getIntent().getStringExtra("title"));
        etFeedback = findViewById(R.id.etFeedback);
        ivImage = findViewById(R.id.ivImage);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);

      /*  ivFAQ = findViewById(R.id.ivFAQ);
        ivFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubmitFeedbackActivity.this, FAQActivity.class));
            }
        });*/

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData()) {
                    Ads_Utils.showAppLovinInterstitialAd(SubmitFeedbackActivity.this, new Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            if (userDetails == null) {
                                new Feedback_Submit_Async(SubmitFeedbackActivity.this,
                                        "", etFeedback.getText().toString().trim(), "", selectedImage);
                            } else {
                                new Feedback_Submit_Async(SubmitFeedbackActivity.this,
                                        userDetails.getEmailId(), etFeedback.getText().toString().trim(), userDetails.getMobileNumber(), selectedImage);
                            }
                        }
                    });
                }
            }
        });

        layoutAds = findViewById(R.id.layoutAds);
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutImage = findViewById(R.id.layoutImage);
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SubmitFeedbackActivity.this.getApplicationContext(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SubmitFeedbackActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request_Storage_resize);
                } else {
                    selectImage(GALLERY_REQUEST_CODE);
                }
            }
        });

        if (Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Storage_resize) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(GALLERY_REQUEST_CODE);
            } else {
                Common_Utils.setToast(SubmitFeedbackActivity.this, "Allow permission for storage access!");
            }
            return;
        }
    }

    private void selectImage(int requestCode) {
        Activity_Manager.isShowAppOpenAd = false;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (data != null) {
                    Uri uriforloadImage = data.getData();
                    if (uriforloadImage != null) {
                        selectedImage = Common_Utils.getPathFromURI(SubmitFeedbackActivity.this, uriforloadImage);
                        Glide.with(SubmitFeedbackActivity.this)
                                .load(new File(selectedImage))
                                .override(getResources().getDimensionPixelSize(R.dimen.dim_90), getResources().getDimensionPixelSize(R.dimen.dim_90))
                                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_5))))
                                .into(ivImage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Request_Storage_resize) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    selectImage(GALLERY_REQUEST_CODE);
                } else {
                    Common_Utils.setToast(SubmitFeedbackActivity.this, "Allow permission for storage access!");
                }
            }
        }
    }


    private boolean isValidData() {
        if (etFeedback.getText().toString().trim().length() == 0) {
            Common_Utils.setToast(SubmitFeedbackActivity.this, "Please enter feedback");
            return false;
        }
        return true;
    }
    private void loadAppLovinNativeAds() {
       /* try {*/
            nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), SubmitFeedbackActivity.this);
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
       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}