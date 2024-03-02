package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Activity_Scratch_CouponsGame;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Scratch_Ticket_Model;
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

public class Store_Scratch_Card_Async {
    private Activity activity;
    private JSONObject jObject;
   private AES_Cipher pocAesCipher;


    public Store_Scratch_Card_Async(final Activity activity, String id, String points) {
        this.activity = activity;
         pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("W41O6W", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("ZJKOCA", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("MQGUTP", points);
            jObject.put("1M1J8H", id);
            jObject.put("24V8DL", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("ESEXZG", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("NSUZAD", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("7AGOX4", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("JX0ATG", Build.MODEL);
            jObject.put("H3IDUQ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("DOHGWQ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

//            AppLogger.getInstance().e("Save Scratch_Card ORIGINAL ==>", jObject.toString());
//            AppLogger.getInstance().e("Save Scratch_Card ENCRYPTED ==>",  pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveScratchcard(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Scratch_Ticket_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Scratch_Ticket_Model.class);
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    ((Activity_Scratch_CouponsGame) activity).changeScratchCardDataValues(responseModel);
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

