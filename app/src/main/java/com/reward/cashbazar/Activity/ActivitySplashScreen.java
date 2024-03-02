package com.reward.cashbazar.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.OfferWallConfig;
import com.pubscale.sdkone.offerwall.models.OfferWallInitListener;
import com.pubscale.sdkone.offerwall.models.errors.InitError;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.AES_Cipher;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.AppLogger;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplashScreen extends AppCompatActivity {
    private Handler handler;

    private ImageView btm, btm1;
    private InstallReferrerClient referrerClient;
    private String referrer = "";
    private BroadcastReceiver appOpenAddLoadedBroadcast;
    private IntentFilter intentFilterActivities;


    static {
        System.loadLibrary("cashbazar");
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onCreate(savedInstanceState);
        Common_Utils.setDayNightTheme(ActivitySplashScreen.this);
        setContentView(R.layout.activity_splash);
        btm = findViewById(R.id.btm);
        btm1 = findViewById(R.id.btm1);
        if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) > 1) {
            btm.setVisibility(View.VISIBLE);
            btm1.setVisibility(View.VISIBLE);
        } else {
            btm.setVisibility(View.GONE);
            btm1.setVisibility(View.GONE);
        }


        if (POC_SharePrefs.getInstance().getString(POC_SharePrefs.appOpenDate).length() == 0 || !POC_SharePrefs.getInstance().getString(POC_SharePrefs.appOpenDate).equals(Common_Utils.getCurrentDate())) {
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.appOpenDate, Common_Utils.getCurrentDate());
            POC_SharePrefs.getInstance().putInt(POC_SharePrefs.todayOpen, 1);
            POC_SharePrefs.getInstance().putInt(POC_SharePrefs.totalOpen, (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) + 1));
        } else {
            POC_SharePrefs.getInstance().putInt(POC_SharePrefs.todayOpen, (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen) + 1));
        }
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null && (extras.containsKey("bundle") && extras.getString("bundle").trim().length() > 0)) {
                POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.isFromNotification, true);
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.notificationData, getIntent().getExtras().getString("bundle"));
            } else {
                POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.isFromNotification, false);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.isFromNotification, false);
        }

        if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.isReferralChecked)) {

            POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.isReferralChecked, true);
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            // on below line we are starting its connection.
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    // this method is called when install referrer setup is finished.
                    switch (responseCode) {
                        // we are using switch case to check the response.
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // this case is called when the status is OK and
                            ReferrerDetails response = null;
                            try {
                                // on below line we are getting referrer details
                                // by calling get install referrer.
                                response = referrerClient.getInstallReferrer();
                                if (response != null) {
                                    // on below line we are getting our apps install referrer.
                                     referrer = response.getInstallReferrer();
                                    if (referrer != null) {
//                                      AppLogger.getInstance().e("REFERRAL_URL", "REFERRAL_URL: " + referrer);
                                        String[] pairs = referrer.split("&");
                                        Map<String, String> map = new LinkedHashMap<String, String>();
                                        for (String pair : pairs) {
                                            int idx = pair.indexOf("=");
                                            map.put(pair.substring(0, idx), pair.substring(idx + 1));
                                        }
//                                        AppLogger.getInstance().e("REFERRAL_MAP", "REFERRAL_SOURCE: " + map.get("utm_source"));
//                                        AppLogger.getInstance().e("REFERRAL_MAP", "REFERRAL_CONTENT: " + map.get("utm_content"));
                                        if (map.get("utm_source").equals("app_referral")) {
                                            POC_SharePrefs.getInstance().putString(POC_SharePrefs.ReferData, map.get("utm_content"));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // handling error case.
                                e.printStackTrace();
                            }
                            referrerClient.endConnection();
                            if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) == 1) {
                                new GetHomeDataAsync(ActivitySplashScreen.this);
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
//                            Toast.makeText(SplashScreenActivity.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                            // API not available on the current Play Store app.
//                            Toast.makeText(SplashScreenActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                            if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) == 1) {
                                new GetHomeDataAsync(ActivitySplashScreen.this);
                            }
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });
        } else {
            if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) == 1) {
                new GetHomeDataAsync(ActivitySplashScreen.this);
            }
        }

  /*      FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            try {
                                if (deepLink != null) {
                                    String str = new Gson().toJson(Common_Utils.splitQuery(new URL(deepLink.toString())));
                                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.ReferData, str);
                                } else {
//                                    SharePrefs.getInstance().putString(SharePrefs.ReferData, "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //AppLogger.getInstance().e("DEEP LINK:", "====" + deepLink);
                        if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) == 1) {
                            new GetHomeDataAsync(ActivitySplashScreen.this);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        SharePrefs.getInstance().putString(SharePrefs.ReferData, "");
                        if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) == 1) {
                            new GetHomeDataAsync(ActivitySplashScreen.this);
                        }
                    }
                });*/

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.AdID, advertId);
            }
        };
        task.execute();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        POC_SharePrefs.getInstance().putString(POC_SharePrefs.FCMregId, token);
                    }
                });
        if (POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen) != 1) {
            new GetHomeDataAsync(ActivitySplashScreen.this);
        }
    }

    public class GetHomeDataAsync {
        private Activity activity;
        private JSONObject jObject;
        private AES_Cipher pocAesCipher;
        private String userToken;

        public GetHomeDataAsync(final Activity activity) {
            this.activity = activity;
            pocAesCipher = new AES_Cipher();
            try {
                jObject = new JSONObject();
                jObject.put("GHYTJKF", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
                jObject.put("870QTW", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
                jObject.put("OLWTFT", Build.MODEL);
                jObject.put("MNFGDEQ", Build.VERSION.RELEASE);
                jObject.put("MQWTMK", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
                jObject.put("YO1HP0", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
                jObject.put("WYQDWW", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
                jObject.put("HVPRIH", Common_Utils.verifyInstallerId(activity));
                jObject.put("GHFWT5", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                jObject.put("4Q4EDN", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
             /*   AppLogger.getInstance().d("HomeDataObject ORIGINAL--)", "" + jObject.toString());
                AppLogger.getInstance().d("HomeDataObject ENCRYPTED--)", "" +  pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));*/
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                    jObject.put("XEZHUU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
                    jObject.put("LONIV9", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
                    userToken = POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken);
                } else {
                    userToken = (POC_Constants.getUSERTOKEN());
                    jObject.put("KLOPIOH", userToken);
                }

                int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
                jObject.put("RANDOM", n);
              /*  AppLogger.getInstance().e("#home", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
                AppLogger.getInstance().e("#home", jObject.toString());*/

                POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
//                Call<Api_Response> call = apiService.getHomeData(userToken, String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
                Call<Api_Response> call = apiService.getHomeData(userToken, String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
                call.enqueue(new Callback<Api_Response>() {
                    @Override
                    public void onResponse(Call<Api_Response> call, Response<Api_Response> response) {
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<Api_Response> call, Throwable t) {
                        if (!call.isCanceled()) {
                            Common_Utils.Notify(activity, getString(R.string.app_name), POC_Constants.msg_Service_Error, true);
                        }
                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        private void onPostExecute(Api_Response response1) {
            try {
                Response_Model response = new Gson().fromJson(new String(pocAesCipher.decrypt(response1.getEncrypt())), Response_Model.class);
                Ads_Utils.adFailUrl = response.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(response.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, response.getUserToken());
                }
                if (response.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                    Common_Utils.doLogout(activity);
                } else if (response.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (!Common_Utils.isStringNullOrEmpty(response.getEarningPoint())) {
                        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, response.getEarningPoint());
                    }

                    if (!Common_Utils.isStringNullOrEmpty(response.getIsShowSurvey()) && response.getIsShowSurvey().matches("1")) {
                        App_Controller app = (App_Controller) getApplication();
                        app.initCPX();
                    }
                    if (!Common_Utils.isStringNullOrEmpty(response.getIsShowAdjump()) && response.getIsShowAdjump().matches("1")) {
                        App_Controller app = (App_Controller) getApplication();
                        app.initAdjump();
                    }


                    if (!Common_Utils.isStringNullOrEmpty(response.getIsShowPubScale()) && response.getIsShowPubScale().matches("1")) {
                        String userID = !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? "0" : POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId);

                        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.small_splash);
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_logo);
                        OfferWallConfig offerWallConfig =
                                new OfferWallConfig.Builder(App_Controller.getContext(), "67045775")
                                        .setUniqueId(userID) //optional, used to represent the user of your application
                                        .setLoaderBackgroundBitmap(bg)//optional
                                        .setLoaderForegroundBitmap(icon)//optional
                                        .setFullscreenEnabled(false)//optional
                                        .build();
                        OfferWall.init(offerWallConfig, new OfferWallInitListener() {
                            @Override
                            public void onInitSuccess() {
                            }

                            @Override
                            public void onInitFailed(InitError initError) {
                            }
                        });
                    }



                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.isShowWhatsAppAuth, response.getIsShowWhatsAppAuth());
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.fakeEarningPoint, response.getFakeEarningPoint());
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.HomeData, new Gson().toJson(response));
                    handler = new Handler();
                    if (Common_Utils.isShowAppLovinAds() && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.isFromNotification) && POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
                        Common_Utils.InitializeApplovinSDK();
                        handler.postDelayed(ActivitySplashScreen.this::gotoMainActivity, 8000);
                    } else {
                        handler.postDelayed(ActivitySplashScreen.this::moveToMainScreen, 2000);
                    }
                } else if (response.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.Notify(activity, getString(R.string.app_name), response.getMessage(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void gotoMainActivity() {
        try {
            removeHandler();
            Ads_Utils.showAppOpenAdd(ActivitySplashScreen.this, new Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (appOpenAddLoadedBroadcast != null) {
                        moveToMainScreen();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToMainScreen() {
        try {
            unRegisterReceivers();
            if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_USER_CONSENT_ACCEPTED)) {
                startActivity(new Intent(ActivitySplashScreen.this, Tearms_and_ConditionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_SKIPPED_LOGIN)) {
                startActivity(new Intent(ActivitySplashScreen.this, UserLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                startActivity(new Intent(ActivitySplashScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
//        //AppLogger.getInstance().e("SplashActivity", "onStop");
            unRegisterReceivers();
            if (referrerClient != null) {
                referrerClient.endConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerBroadcast() {
        appOpenAddLoadedBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //AppLogger.getInstance().e("SplashScreenActivity", "Broadcast Received==" + intent.getAction());
                if (intent.getAction().equals(POC_Constants.APP_OPEN_ADD_DISMISSED)) {
                    removeHandler();
                    moveToMainScreen();
                } else {
                    gotoMainActivity();
                }
            }
        };
        intentFilterActivities = new IntentFilter();
        intentFilterActivities.addAction(POC_Constants.APP_OPEN_ADD_LOADED);
        intentFilterActivities.addAction(POC_Constants.APP_OPEN_ADD_DISMISSED);
        registerReceiver(appOpenAddLoadedBroadcast, intentFilterActivities);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(appOpenAddLoadedBroadcast, intentFilterActivities, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(appOpenAddLoadedBroadcast, intentFilterActivities);
        }
    }

    private void unRegisterReceivers() {
        if (appOpenAddLoadedBroadcast != null) {
//            //AppLogger.getInstance().e("SplashScreenActivity", "Unregister Broadcast");
            unregisterReceiver(appOpenAddLoadedBroadcast);
            appOpenAddLoadedBroadcast = null;
        }
    }

    @Override
    public void onBackPressed() {
//        //AppLogger.getInstance().e("SplashScreenActivity", "onBackPress");
        removeHandler();
        unRegisterReceivers();
        super.onBackPressed();
        System.exit(0);
    }

    private void removeHandler() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}