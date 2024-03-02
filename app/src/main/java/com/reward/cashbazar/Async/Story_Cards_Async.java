package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.CardMatchQuizActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Cards_model;
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

public class Story_Cards_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;


    public Story_Cards_Async(final Activity activity, String point) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("UK5FEG", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("SRGSDGS", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("ADFGVDFGVAD", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("KWH56T", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("ZWWOWD", point);
            jObject.put("JI2SGU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("CFVHCBC", Common_Utils.verifyInstallerId(activity));
            jObject.put("DFBDFXD", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("BEAIII", Build.MODEL);
            jObject.put("CGVJGFSSD", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("CFHCVBCV", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//            POC_AppLogger.getInstance().e("Save Cards ORIGINAL11 ==>", jObject.toString());
//            POC_AppLogger.getInstance().e("Save Cards ENCRYPTED ==>", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveCardsData(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Cards_model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Cards_model.class);
            //AppLogger.getInstance().e("Save Count RESPONSE ==>", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                POC_SharePrefs.getInstance().putInt(POC_SharePrefs.LastSpinIndex, -1);
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    //AppLogger.getInstance().e("Save Spin RESPONSE ==>", "" + new Gson().toJson(responseModel));
                    ((CardMatchQuizActivity) activity).changeCountDataValues(responseModel);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    ((CardMatchQuizActivity) activity).changeCountDataValues(responseModel);
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
