package com.reward.cashbazar.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Activity_Milestone;
import com.reward.cashbazar.Activity.MilestoneHistoryActivity;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.MilestonesResponseModel;
import com.reward.cashbazar.R;
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

public class StoreMilestoneAsync {
    private Activity activity;
    private JSONObject jObject;
   private AES_Cipher pocAesCipher;
    private String milestoneId;



    public StoreMilestoneAsync(final Activity activity, String point, String milestoneId) {
        this.activity = activity;
         pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("V63U6G", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("F52NWD", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("SY0DRW", point);
            jObject.put("2W9UWY", milestoneId);
            jObject.put("I88MV9", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("2SGSYH", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("41STT6", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("HFLZ8B", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("TZ0T8U", Build.MODEL);
            jObject.put("PLMF6H", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("PLMF6H", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            ////AppLogger.getInstance().e("Save Spin ORIGINAL ==>", jObject.toString());
            //AppLogger.getInstance().e("Text Typing ENCRYPTED ==>", AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString()));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveMilestone(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            MilestonesResponseModel responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), MilestonesResponseModel.class);
            ////AppLogger.getInstance().e("Save Spin RESPONSE ==>", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof Activity_Milestone) {
                        ((Activity_Milestone) activity).changeMilestoneValues(responseModel);
                    } else if (activity instanceof MilestoneHistoryActivity) {
                        ((MilestoneHistoryActivity) activity).changeMilestoneValues(responseModel);
                    }
                    App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.LIVE_MILESTONE_RESULT)
                            .setPackage(App_Controller.getContext().getPackageName())
                            .putExtra("id", milestoneId)
                            .putExtra("status", POC_Constants.STATUS_SUCCESS));
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR) || responseModel.getStatus().equals("2")) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
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
