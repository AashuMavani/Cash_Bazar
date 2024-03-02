package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.WordStoringActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.WordSortingResponseModel;
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

public class SaveWordSortingAsync {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;

    public SaveWordSortingAsync(final Activity activity, String point) {
        this.activity = activity;
        cipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("BHSH67T7", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("FT6G8BAWA", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("A3D6F7H8", point);
            jObject.put("M7V4D5FYSA", Build.MODEL);
            jObject.put("L7G6D4QAS", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("DFGE4T", Common_Utils.verifyInstallerId(activity));
            jObject.put("FGHRTY5", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("FGHFGH54Y", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("GFGFH", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("4354RTE", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//            AppLogger.getInstance().e("Save Word Sorting ORIGINAL11 ==>", jObject.toString());
//            AppLogger.getInstance().e("Save Word Sorting ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveWordSorting(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            WordSortingResponseModel responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), WordSortingResponseModel.class);
//            AppLogger.getInstance().e("Save Word Sorting RESPONSE ==>", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                POC_SharePrefs.getInstance().putInt(POC_SharePrefs.LastSpinIndex, -1);
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS) || responseModel.getStatus().equals("2")) {
                    ((WordStoringActivity) activity).changeCountDataValues(responseModel);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
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
