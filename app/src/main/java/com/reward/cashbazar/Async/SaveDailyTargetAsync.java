package com.reward.cashbazar.Async;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.Activity.RewardPage_Activity;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.AES_Cipher;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDailyTargetAsync {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;
    private String milestoneId;

    public SaveDailyTargetAsync(final Activity activity, String point, String milestoneId) {
        this.activity = activity;
        this.milestoneId = milestoneId;
        cipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("W5GHW8A", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("BININXS4A", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("T6GVYSAU", point);
            jObject.put("B6BCKS8GH", milestoneId);
            jObject.put("SDAFSDF", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("JKLJJJK", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("L87BHDEW", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("Q234423", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("L9HTWT2W", Build.MODEL);
            jObject.put("LONHTVWY", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

//            AppLogger.getInstance().e("Save Spin ORIGINAL ==>", jObject.toString());
//            AppLogger.getInstance().e("DAILY TARGET ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveDailyTarget(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Api_Response>() {
                @Override
                public void onResponse(Call<Api_Response> call, Response<Api_Response> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<Api_Response> call, Throwable t) {
                    Common_Utils.dismissProgressLoader();
                    if (!call.isCanceled()) {
                        Common_Utils.Notify(activity, activity.getString(R.string.app_name), POC_Constants.msg_Service_Error, false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Common_Utils.dismissProgressLoader();
        }
    }

    private void onPostExecute(Api_Response response) {
        try {
            Common_Utils.dismissProgressLoader();
            Response_Model responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Response_Model.class);
//            AppLogger.getInstance().e("Save DAILY TARGET RESPONSE ==>", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof RewardPage_Activity) {
                        ((RewardPage_Activity) activity).changeDailyTargetValues(true);
                    }
                    App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.DAILY_TARGET_RESULT)
                            .setPackage(App_Controller.getContext().getPackageName())
                            .putExtra("id", milestoneId)
                            .putExtra("status", POC_Constants.STATUS_SUCCESS));
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR) || responseModel.getStatus().equals("2")) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                            App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.DAILY_TARGET_RESULT)
                            .setPackage(App_Controller.getContext().getPackageName())
                            .putExtra("id", milestoneId)
                            .putExtra("status", POC_Constants.STATUS_ERROR));
                    if (activity instanceof RewardPage_Activity) {
                        ((RewardPage_Activity) activity).changeDailyTargetValues(false);
                    }
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
