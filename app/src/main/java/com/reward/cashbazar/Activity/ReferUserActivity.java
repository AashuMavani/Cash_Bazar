/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.reward.cashbazar.Adapter.Invite_Rules_Adapter;
import com.reward.cashbazar.Async.Download_Image_Share_Async;
import com.reward.cashbazar.Async.Get_Invite_Async;
import com.reward.cashbazar.Async.Models.Invite_Response_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.OnBalloonClickListener;

import java.io.File;

public class ReferUserActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE = 1000;
    private boolean isResumeCalled = false, isParentImageLoaded = false, isTopImageLoaded = false;
    private LinearLayout layoutLogin, layoutInvite, layoutPoints1, more_copy, telegram_copy, sms_copy, copy_wp, layoutPoints;
    private AppCompatButton btnLogin;
    private TextView tvRules, tvInviteNo, tvInviteIncome, tvInviteCode, tvInvite, tvLoginText, lblInviteNo, lblIncome, tvPoints;
    private Invite_Response_Model objInvite;
    private RelativeLayout layoutBtnInvite;
    private ImageView ivCopy, ivHistory, iv_left;
    private LottieAnimationView inviteBtnLottie, parentLottie, dataLottie, topLottie;
    private View viewSeparator;
    RecyclerView rvRules;
    private long lastClickTime = 0;
    private Response_Model responseMain;

    public ReferUserActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(ReferUserActivity.this);
        setContentView(R.layout.activity_invite_page);
        initView();
        new Get_Invite_Async(ReferUserActivity.this);
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);


    }

    private void initView() {
        viewSeparator = findViewById(R.id.viewSeparator);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints1 = findViewById(R.id.layoutPoints1);
        iv_left = findViewById(R.id.iv_left);
        ivHistory = findViewById(R.id.ivHistory);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ReferUserActivity.this, ReferPointHistoryActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ReferUserActivity.this);
                }
            }
        });
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ReferUserActivity.this, ReferPointHistoryActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ReferUserActivity.this);
                }
            }
        });

        layoutPoints1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(ReferUserActivity.this, MyWalletActivity.class));
                } else {
                    Common_Utils.NotifyLogin(ReferUserActivity.this);
                }
            }
        });
        tvLoginText = findViewById(R.id.tvLoginText);

        tvInviteNo = findViewById(R.id.tvInviteNo);
        tvInviteIncome = findViewById(R.id.tvInviteIncome);
        tvInviteCode = findViewById(R.id.tvInviteCode);
        ivCopy = findViewById(R.id.ivCopy);
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) ReferUserActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", tvInviteCode.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                Common_Utils.setToast(ReferUserActivity.this, "Copied!");
            }
        });
        tvInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) ReferUserActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", tvInviteCode.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                Common_Utils.setToast(ReferUserActivity.this, "Copied!");
            }
        });
        tvInvite = findViewById(R.id.tvInvite);
        lblInviteNo = findViewById(R.id.lblInviteNo);
        lblIncome = findViewById(R.id.lblIncome);

        layoutBtnInvite = findViewById(R.id.layoutBtnInvite);
        layoutBtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                callInviteFriends();
            }
        });

        more_copy = findViewById(R.id.more_copy);
        telegram_copy = findViewById(R.id.telegram_copy);
        sms_copy = findViewById(R.id.sms_copy);
        copy_wp = findViewById(R.id.copy_wp);

        copy_wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (objInvite.getReferralLink() != null) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setPackage("com.whatsapp");
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
                        share.setType("text/plain");
                        try {
                            startActivity(share);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(ReferUserActivity.this, "Kindly install whatsapp first", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        sms_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (objInvite.getReferralLink() != null) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setPackage("com.instagram.android");
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
                        share.setType("text/plain");
                        try {
                            startActivity(share);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(ReferUserActivity.this, "Kindly install Instagram first", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });
        telegram_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (objInvite.getReferralLink() != null) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setPackage("org.telegram.messenger");
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
                        share.setType("text/plain");
                        try {
                            startActivity(share);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(ReferUserActivity.this, "Kindly install Telegram first", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });
        more_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objInvite.getReferralLink() != null) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_TEXT, objInvite.getReferralLink());
                    startActivity(Intent.createChooser(share, "Share via"));
                }
            }
        });


        inviteBtnLottie = findViewById(R.id.inviteBtnLottie);
        parentLottie = findViewById(R.id.parentLottie);
        dataLottie = findViewById(R.id.dataLottie);
        // topLottie = view.findViewById(R.id.topLottie);
        parentLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isParentImageLoaded = true;
                if (isParentImageLoaded && isTopImageLoaded) {
                    layoutPoints.setVisibility(View.VISIBLE);
                    tvInviteCode.setVisibility(View.VISIBLE);
                    Common_Utils.dismissProgressLoader();
                }
            }
        });

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutInvite = findViewById(R.id.layoutInvite);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(ReferUserActivity.this, UserLoginActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rvRules = findViewById(R.id.rvRules);

    }

    public void callInviteDataApi(Activity context) {
        try {
            if (!isResumeCalled) {
                new Get_Invite_Async(context);
            } else {
                setData(null);
            }
            isResumeCalled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Balloon balloon;

    private void callInviteFriends() {
        try {
            if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                Common_Utils.NotifyLogin(ReferUserActivity.this);
            } else {
                if (!Common_Utils.isStringNullOrEmpty(objInvite.getShareImage())) {
                    if (ContextCompat.checkSelfPermission(ReferUserActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ReferUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                    } else {
                        shareImageData(ReferUserActivity.this, objInvite.getShareImage(), objInvite.getShareMessage());
                    }
                } else {
                    shareImageData(ReferUserActivity.this, "", objInvite.getShareMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Invite_Response_Model responseModel;

    public void setData(Invite_Response_Model responseModel1) {
        responseModel = responseModel1;
        tvPoints.setText(POC_SharePrefs.getInstance().getEarningPointString());

        layoutLogin.setVisibility(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? View.GONE : View.VISIBLE);
        layoutInvite.setVisibility(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? View.VISIBLE : View.GONE);
        if (responseModel == null) {
            return;
        }
        objInvite = responseModel;

        rvRules.setAdapter(new Invite_Rules_Adapter(ReferUserActivity.this, objInvite.getHowToWork(), new Invite_Rules_Adapter.ClickListener() {
            @Override
            public void onInfoButtonClick(int position, View v) {
                try {
                    balloon = new Balloon.Builder(ReferUserActivity.this)
                            .setArrowSize(10)
                            .setArrowOrientation(ArrowOrientation.TOP)
                            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                            .setArrowPosition(0.5f)
                            .setWidth(BalloonSizeSpec.WRAP)
                            .setHeight(65)
                            .setTextSize(15f)
                            .setCornerRadius(4f)
                            .setAlpha(0.9f)
                            .setText(objInvite.getHowToWork().get(position).getDescription())
                            .setTextColor(ContextCompat.getColor(ReferUserActivity.this, R.color.white))
                            .setTextIsHtml(true)
                            .setBackgroundColor(ContextCompat.getColor(ReferUserActivity.this, R.color.black_transparent))
                            .setOnBalloonClickListener(new OnBalloonClickListener() {
                                @Override
                                public void onBalloonClick(@NonNull View view) {
                                    balloon.dismiss();
                                }
                            })
                            .setBalloonAnimation(BalloonAnimation.FADE)
                            .setLifecycleOwner(ReferUserActivity.this)
                            .build();
                    balloon.showAlignBottom(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        rvRules.setLayoutManager(new GridLayoutManager(ReferUserActivity.this, 3));

        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);



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
                Common_Utils.loadTopBannerAd(ReferUserActivity.this, layoutTopAds, responseModel.getTopAds());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // tvRules.setVisibility(responseModel.getHowToWork() != null && responseModel.getHowToWork().size() > 0 ? View.VISIBLE : View.GONE);

        //tvInvite.setText(responseModel.getBtnName());
        tvInvite.setTextColor(Color.parseColor(responseModel.getBtnTextColor()));

        tvInviteNo.setText(responseModel.getTotalReferrals());
        tvInviteNo.setTextColor(Color.parseColor(responseModel.getInviteNoTextColor()));

        tvInviteIncome.setText(responseModel.getTotalReferralIncome());
        tvInviteIncome.setTextColor(Color.parseColor(responseModel.getInviteNoTextColor()));

        tvInviteCode.setText(responseModel.getReferralCode());

        lblInviteNo.setTextColor(Color.parseColor(responseModel.getInviteLabelTextColor()));
        lblIncome.setTextColor(Color.parseColor(responseModel.getInviteLabelTextColor()));
        viewSeparator.setBackgroundColor(Color.parseColor(responseModel.getSeparatorColor()));
        tvLoginText.setTextColor(Color.parseColor(responseModel.getTextColor()));


    }

    public void shareImageData(Activity activity, String img, String msg) {
        Intent share;
        if (img.trim().length() > 0) {
            String[] str = img.trim().split("/");
            String extension = "";
            if (str[str.length - 1].contains(".")) {
                extension = str[str.length - 1].substring(str[str.length - 1].lastIndexOf("."));
            }
            if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".gif")) {
                extension = "";
            } else {
                extension = ".png";
            }
            File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_PICTURES) + File.separator)));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, str[str.length - 1] + extension);
            if (file.exists()) {
                try {
                    share = new Intent(Intent.ACTION_SEND);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".provider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    share.setType("image/*");
                    if (img.contains(".gif")) {
                        share.setType("image/gif");
                    } else {
                        share.setType("image/*");
                    }
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    share.putExtra(Intent.EXTRA_TEXT, msg);
                    activity.startActivity(Intent.createChooser(share, "Share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Common_Utils.isNetworkAvailable(ReferUserActivity.this)) {
                new Download_Image_Share_Async(activity, file, img, "", msg).execute();
            }
        } else {
            try {
                share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_TEXT, msg);
                share.setType("text/plain");
                activity.startActivity(Intent.createChooser(share, "Share"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_CODE_STORAGE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareImageData(ReferUserActivity.this, objInvite.getShareImage(), objInvite.getShareMessage());
                } else {
                    Common_Utils.setToast(ReferUserActivity.this, "Allow storage permissions!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
