package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.SpinGameActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Spinner_Data_Model;
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

public class Get_Spin_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;

    public Get_Spin_Async(final Activity activity) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("ADIPC8", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("BTRJTA", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("U69WD3", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("WNYRG", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("FNZ7DN", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("8SDC8D", Build.MODEL);
            jObject.put("RGBHWTJHT", Build.VERSION.RELEASE);
            jObject.put("CQLBVL", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("OSYBPJ", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("HEHF6J", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("4DUYSJ", Common_Utils.verifyInstallerId(activity));
            jObject.put("U69WD3", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            //AppLogger.getInstance().e("Get Spin ORIGINAL ==>", jObject.toString());
            //AppLogger.getInstance().e("Get Spin ENCRYPTED ==>", AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString()));
            Call<Api_Response> call = apiService.getSpinData(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Common_Utils.dismissProgressLoader();
            e.printStackTrace();
        }
    }

    private void onPostExecute(Api_Response response) {
        try {
            Common_Utils.dismissProgressLoader();
            Spinner_Data_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Spinner_Data_Model.class);
//            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    ((SpinGameActivity) activity).setData(responseModel);
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
