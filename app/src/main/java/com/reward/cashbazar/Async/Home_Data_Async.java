package com.reward.cashbazar.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.MainActivity;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Response_Model;
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

public class Home_Data_Async {
    private Activity activity;
    private JSONObject jObject;
   private AES_Cipher pocAesCipher;

    public Home_Data_Async(final Activity activity) {
        this.activity = activity;
         pocAesCipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("GHYTJKF", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("870QTW", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("OLWTFT", Build.MODEL);
            jObject.put("MNFGDEQ", Build.VERSION.RELEASE);
            jObject.put("MQWTMK", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("YO1HP0", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("WYQDWW", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("HVPRIH", Common_Utils.verifyInstallerId(activity));
            jObject.put("4Q4EDN", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("GHFWT5", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("XEZHUU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("LONIV9", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
//            AppLogger.getInstance().d("HomeDataObject ORIGINAL--)", "" + jObject.toString());
//            AppLogger.getInstance().d("HomeDataObject ENCRYPTED--)", "" + pocAesCipher.encrypt(jObject.toString()));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.getHomeData(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Api_Response>() {
                @Override
                public void onResponse(Call<Api_Response> call, Response<Api_Response> response) {
                    onPostExecute(response.body(), activity);
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
            Common_Utils.dismissProgressLoader();
            e.printStackTrace();
        }
    }

    private void onPostExecute(Api_Response response, Activity activity) {
        try {
            Common_Utils.dismissProgressLoader();
            Response_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Response_Model.class);

            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.isShowWhatsAppAuth, responseModel.getIsShowWhatsAppAuth());
                    if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                        POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    }
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.fakeEarningPoint, responseModel.getFakeEarningPoint());
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.HomeData, new Gson().toJson(responseModel));
                    ((MainActivity) activity).setHomeData();
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
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

