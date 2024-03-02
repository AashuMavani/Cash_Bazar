package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Typing_GameActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.TypeTextDataModel;
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

public class GetTypeTextDataAsync {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;


    public GetTypeTextDataAsync(final Activity activity) {
        this.activity = activity;
        cipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("FTUO9A", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("WB66KA", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("DU3626", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("USQVQ1", Build.MODEL);
            jObject.put("123AWD", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("DFTG34", Build.VERSION.RELEASE);
            jObject.put("BJBPW4", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("9AH4OE", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("OOOTSF", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("RTB0VZ", Common_Utils.verifyInstallerId(activity));
            jObject.put("OW8Q2C", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            //AppLogger.getInstance().e("Get Spin ORIGINAL ==>", jObject.toString());
//            POC_AppLogger.getInstance().e("Text Typing ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            Call<Api_Response> call = apiService.getTextTypingData(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            TypeTextDataModel responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), TypeTextDataModel.class);
//            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                ((Typing_GameActivity) activity).setData(responseModel);

                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
