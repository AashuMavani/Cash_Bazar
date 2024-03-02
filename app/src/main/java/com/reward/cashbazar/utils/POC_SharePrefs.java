package com.reward.cashbazar.utils;

import static com.reward.cashbazar.utils.Common_Utils.DATE_FORMAT_STANDARDIZED_UTC;

import android.app.Activity;
import android.content.Context;

import com.github.rtoshiro.secure.SecureSharedPreferences;

import com.reward.cashbazar.App_Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class POC_SharePrefs {

    public static String RATE_POPUP_SHOWN_COUNT = "RatePopupShownCount";
    private SecureSharedPreferences sharedPreferences;
    private static POC_SharePrefs instance = null;
    public static String RATE_POPUP_MOVE_TO_PLAY_STORE_COUNT = "ratePopupMoveToPlayStoreCount";
    public static String RATE_POPUP_LAST_SHOW_DATE = "ratePopupLastShowDate";
    public static String IS_USER_CONSENT_ACCEPTED = "isUserConsentAccepted";

    public static String IS_LOGIN = "isLogin";
    public static String IS_SKIPPED_LOGIN = "isSkippedLogin";
    public static String IS_FIRST_LOGIN = "isFirstLogin";
    public static final String ReferData = "ReferData";

    public static final String User_Details = "User_Details";
    public static final String userId = "userId";
    public static final String userToken = "userToken";
    public static final String AdID = "AdID";
    public static final String EarnedPoints = "EarnedPoints";
    public static final String isShowWhatsAppAuth = "isShowWhatsAppAuth";
    public static final String fakeEarningPoint = "fakeEarningPoint";
    public static final String LastSpinIndex = "LastSpinIndex";
    public static final String FCMregId = "FCMregId";
    public static final String AppVersion = "AppVersion";
    public static final String HomeData = "HomeData";
    public static final String totalOpen = "totalOpen";
    public static final String todayOpen = "todayOpen";
    public static final String appOpenDate = "appOpenDate";
    public static final String homeDialogShownDate = "homeDialogShownDate";
    public static final String pointHistoryMiniAdShownDate = "pointHistoryMiniAdShownDate";
    public static final String isFromNotification = "isFromNotification";
    public static final String notificationData = "notificationData";
    public static String IS_WELCOME_POPUP_SHOWN = "IS_WELCOME_POPUP_SHOWN";
    public static String IS_REVIEW_GIVEN = "isReviewGiven";

    public static final String IS_TASK_EVENT_TRIGGERED ="isTaskEventTriggered" ;
    public static final String isReferralChecked = "isReferralChecked";

    /**
     * Saving data in shared preferences which will store life time of Application
     */
    public POC_SharePrefs(Context context) {
        this.sharedPreferences = new SecureSharedPreferences(App_Controller.mContext);
    }

    public static POC_SharePrefs getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new POC_SharePrefs(App_Controller.mContext);
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT_STANDARDIZED_UTC).format(new Date());
    }

    public static boolean verifyInstallerId(Activity context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }

    public static int getRandomNumberBetweenRange(int min, int max) {
        if (max == 0) {
            return 0;
        }
        Random r = new Random();
        int i1 = r.nextInt(max - min) + min;// min inclusive & max exclusive
        return i1;
    }


    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getEarningPointString() {
        return sharedPreferences.getString(POC_SharePrefs.EarnedPoints, sharedPreferences.getString(POC_SharePrefs.fakeEarningPoint, "0"));
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void clearSharePrefs() {
        sharedPreferences.edit().clear().apply();
    }

    public void putFloat(String key, float val) {
        sharedPreferences.edit().putFloat(key, val).apply();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0.0f);
    }


}
