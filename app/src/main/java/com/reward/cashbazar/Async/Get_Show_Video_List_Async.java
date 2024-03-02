package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Win_by_Watching_VideosActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Show_Video_Model;
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

public class Get_Show_Video_List_Async {
    private Activity activity;
    private JSONObject jObject;
   private AES_Cipher pocAesCipher;

    public Get_Show_Video_List_Async(final Activity activity) {
        this.activity = activity;
         pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("GY8PV0", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("4K7HGG", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("TGTWML", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("FGJHFG", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("6K240Q", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("RGGVGE", Build.MODEL);
            jObject.put("DFGJFG", Build.VERSION.RELEASE);
            jObject.put("B5KGP1", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("DJTGLY", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("WIBC2I", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("1XDMO1", Common_Utils.verifyInstallerId(activity));
            jObject.put("GCE5TE", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//            AppLogger.getInstance().e("Watch Video List DATA ORIGINAL--)", "" + jObject.toString());
//            AppLogger.getInstance().e("Watch Video List DATA ENCRYPTED--)", "" + pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.getWatchVideoList(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
//            Call<Watch_Video_Model> call = apiService.getWatchVideoList(jObject.toString());
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
            Show_Video_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Show_Video_Model.class);
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof Win_by_Watching_VideosActivity) {
                        ((Win_by_Watching_VideosActivity) activity).setData(responseModel);
                    }
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) { // not login
                    if (activity instanceof Win_by_Watching_VideosActivity) {
                        ((Win_by_Watching_VideosActivity) activity).setData(responseModel);
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
