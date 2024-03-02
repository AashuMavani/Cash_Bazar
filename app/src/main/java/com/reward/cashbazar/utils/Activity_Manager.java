package com.reward.cashbazar.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.otpless.main.OtplessLoginActivity;

import io.adjoe.sdk.AdjoeActivity;
import com.reward.cashbazar.Activity.ActivitySplashScreen;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.value.POC_Constants;

public class Activity_Manager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private int numberActivitiesStart = 0;
    private String TAG = this.getClass().getSimpleName();
    public static int timeToWatchInSeconds;
    public static boolean appInForeground, isShowAppOpenAd = true;
    private long lastTime, newTime;

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        try {
            //AppLogger.getInstance().e(TAG, "onMoveToForeground===  app went to foreground");
            if (!(Ads_Utils.getCurrentActivity() instanceof ActivitySplashScreen)
                    && !(Ads_Utils.getCurrentActivity() instanceof OtplessLoginActivity) && !(Ads_Utils.getCurrentActivity() instanceof AdjoeActivity) && isShowAppOpenAd)
                Ads_Utils.showAppOpenAdd(Ads_Utils.getCurrentActivity(), null);
            isShowAppOpenAd = true;
            if (newTime - lastTime >= timeToWatchInSeconds) {
                App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.WATCH_WEBSITE_RESULT)
                        .setPackage(App_Controller.getContext().getPackageName()).putExtra("status", POC_Constants.STATUS_SUCCESS));
//                            CommonUtils.NotifySuccess(AdsUtils.getCurrentActivity(), "Watch Website", "Congratulations! 20 Seconds completed!", false);
            } else {
                App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.WATCH_WEBSITE_RESULT)
                        .setPackage(App_Controller.getContext().getPackageName()).putExtra("status", POC_Constants.STATUS_ERROR));
//                            CommonUtils.Notify(AdsUtils.getCurrentActivity(), "Watch Website", "Oops - 20 Seconds NOT completed!", false);
            }
            lastTime = 0;
            newTime = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        try {
            Ads_Utils.setCurrentActivity(activity);
            if (numberActivitiesStart == 0) {
                // The application come from background to foreground
                //AppLogger.getInstance().e(TAG, "App_Controller status > onActivityStarted:  app went to foreground");
                if (!appInForeground) {
                    appInForeground = true;
                }
            }
            numberActivitiesStart++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Ads_Utils.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        try {
            numberActivitiesStart--;
            if (numberActivitiesStart == 0) {
                // The application go from foreground to background
                appInForeground = false;
                //AppLogger.getInstance().e(TAG, "App_Controller status > onActivityStopped: app went to background");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
