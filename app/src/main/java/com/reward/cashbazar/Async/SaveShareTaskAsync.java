package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.TaskDetailsActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.ReferResponseModel;
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

public class SaveShareTaskAsync {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;
    private String type;

    public SaveShareTaskAsync(final Activity activity, String taskId, String type) {
        this.activity = activity;
        cipher = new AES_Cipher();
        this.type = type;
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("L9JRVC4A", taskId);
            jObject.put("KI6H9AH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("FHGFGH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("IOIUOU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("SERSRF", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("3WRWER", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("JKLJL", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("VBNTTY", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("NBMBNM", Build.MODEL);
            jObject.put("SDF343S", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

//            AppLogger.getInstance().e("Get User Profile ORIGINAL ==>", jObject.toString());
//            AppLogger.getInstance().e("Get User Profile ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));

            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.shareTaskOffer(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            ReferResponseModel responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), ReferResponseModel.class);
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof TaskDetailsActivity) {
                        responseModel.setType(type);
                        ((TaskDetailsActivity) activity).saveShareTaskOffer(responseModel);
                    }
                } else {
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
