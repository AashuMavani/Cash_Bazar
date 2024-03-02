package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Number_Sequencing_Activity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Count_Down_model;
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

public class Store_Count_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;


    public Store_Count_Async(final Activity activity, String point) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();

//        POC_Common_Utils.setToast(activity, userToken);

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("JUXQJ0", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("U9OM2H", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("S7G7NW", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("SFZH4Q", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("NHWRSN", point);
            jObject.put("PV7HUU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("WOZK2J", Common_Utils.verifyInstallerId(activity));
            jObject.put("Z80HDZ", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("O3XHMG", Build.MODEL);
            jObject.put("W1GINQ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("W1GINQ", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

//            AppLogger.getInstance().e("Save Dice ORIGINAL11 ==>", jObject.toString());
//            AppLogger.getInstance().e("Save Dice ENCRYPTED ==>",  pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
//            AppLogger.getInstance().e("Save Count ORIGINAL11 ==>",""+POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken)+"--)"+n+"--)");

            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.saveCountData(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            Count_Down_model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Count_Down_model.class);
//            AppLogger.getInstance().e("Save Count RESPONSE ==>", "" + new Gson().toJson(responseModel));
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
                    ((Number_Sequencing_Activity) activity).changeCountDataValues(responseModel);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    ((Number_Sequencing_Activity) activity).changeCountDataValues(responseModel);
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
