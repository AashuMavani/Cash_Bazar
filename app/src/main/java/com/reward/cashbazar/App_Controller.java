package com.reward.cashbazar;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.messaging.FirebaseMessaging;
import com.leadmint.adjump.adjump;
import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;
import com.onesignal.OneSignal;
import com.reward.cashbazar.utils.Activity_Manager;
import com.reward.cashbazar.utils.POC_SharePrefs;

import io.adjoe.sdk.Adjoe;

public class App_Controller extends Application {
    public static Context mContext;
    private static final String ONESIGNAL_APP_ID = "b38b1d39-8432-4d6b-8929-81ccea8c9605";
    public static BroadcastReceiver packageInstallBroadcast;

    private static CPXResearch cpxResearch;
    private static String exUserId;
    private adjump adjump;




    static {
        System.loadLibrary("cashbazar");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Adjoe.isAdjoeProcess()) {
            // the method is executed on the adjoe process
            return;
        }
        mContext = this;



        Activity_Manager activityManager = new Activity_Manager();
        registerActivityLifecycleCallbacks(activityManager);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(activityManager);

        FirebaseMessaging.getInstance().subscribeToTopic("global");
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            FirebaseMessaging.getInstance().subscribeToTopic("globalV" + version);
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.AppVersion, version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }



    public static Context getContext() {
        return mContext;
    }

    @NonNull
    public CPXResearch getCpxResearch() {
        return cpxResearch;
    }

/*    public static void initCPX() {
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.CornerBottomRight, "Earn up to<br> 3 Coins", 18, "#ffffff", "#ffaf20", true);


        if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
            exUserId = POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId);
        } else {
            exUserId = ("0");
        }

        CPXConfiguration config = new CPXConfigurationBuilder("19751", exUserId, "Db5o9JJ5aTHyLeCrNVzvdUul4TW1UeOC", style).build();

        cpxResearch = CPXResearch.Companion.init(config);
    }*/



/*    public static String homeAppLuckInter, homeAppLuckIncentive, homeAppLuckId;
    private void resetAppLuckVariables() {
        try {
            Common_Utils.dismissProgressLoader();
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.appLuckShownDate + homeAppLuckId, Common_Utils.getCurrentDate());
            homeAppLuckInter = null;
            homeAppLuckIncentive = null;
            homeAppLuckId = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
/*    public void setUpAppLuckSDK(Activity activity, String defaultAdUnit, String interstitialAdUnit, String incentiveAdUnit) {
        AppLuckSDK.setListener(new AppLuckSDK.AppLuckSDKListener() {
            @Override
            public void onInitSuccess() {
//                AppLogger.getInstance().e("AppLuckSDK", "Init SUCCESS");
                AppLuckSDK.loadPlacement(defaultAdUnit, "icon", 150, 150);
                AppLuckSDK.loadPlacement(interstitialAdUnit, "icon", 150, 150);
                AppLuckSDK.loadPlacement(incentiveAdUnit, "icon", 150, 150);
            }

            @Override
            public void onInitFailed(Error error) {
//                AppLogger.getInstance().e("AppLuckSDK", "Init FAILED");
            }*/

/*            @Override
            public void onPlacementLoadSuccess(String placementId) {
//                AppLogger.getInstance().e("AppLuckSDK", "LOADED ID: " + placementId);
//                AppLogger.getInstance().e("AppLuckSDK", "homeAppLuckId: " + homeAppLuckId);
//                AppLogger.getInstance().e("AppLuckSDK", "homeAppLuckIncentive: " + homeAppLuckIncentive);
//                AppLogger.getInstance().e("AppLuckSDK", "homeAppLuckInter: " + homeAppLuckInter);
                if (!Common_Utils.isStringNullOrEmpty(homeAppLuckId)) {
                    if (!Common_Utils.isStringNullOrEmpty(homeAppLuckIncentive) && placementId.equals(incentiveAdUnit)) {
                        AppLuckSDK.openInteractiveAds(placementId, 2, 2);
                        resetAppLuckVariables();
                    } else if (!Common_Utils.isStringNullOrEmpty(homeAppLuckInter) && placementId.equals(interstitialAdUnit)) {
                        AppLuckSDK.openInteractiveAds(placementId, 1, 1);
                        resetAppLuckVariables();
                    }
                }
            }*/

       /*     @Override
            public void onInteractiveAdsHidden(String placementId, int i) {
                // 0 = ordinary close, 1 = finish the goal of times
//                if (placementId.equals(incentiveAdUnit)) {
//                    if (i == 1)
//                        Toast.makeText(MyApplication.this, placementId + " WIN", Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(MyApplication.this, placementId + " BETTER LUCK", Toast.LENGTH_SHORT).show();
//                }
            }*/
/*
            @Override
            public void onInteractiveAdsDisplayed(String placementId) {

            }

            @Override
            public void onUserInteraction(String placementId, String interaction) {
            }
        });
        AppLuckSDK.init(defaultAdUnit);
    }*/

    public void initCPX() {
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.CornerBottomRight,
                "Earn up to<br> 3 Points",
                18,
                "#ffffff",
                "#ffaf20",
                true);

        String userID = !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? "0" : POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId);

        CPXConfiguration config = new CPXConfigurationBuilder("19120",
                userID,
                "tLipE1pb11EpphRWzRdHETUZ0mjT85FH",
                style)
                .build();

        cpxResearch = CPXResearch.Companion.init(config);
    }

    public void initAdjump() {
        String userID = !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? "0" : POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId);
        adjump = new adjump(mContext, "1036", "1018", userID);
    }

    public adjump getAdjump() {
        return adjump;
    }
}

