package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.Bombsweeper_Activity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Model_MinesweeperData;
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

public class Async_Minesweeper_Get {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;

    public Async_Minesweeper_Get(final Activity activity) {
        this.activity = activity;
        cipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("SDSDFG12", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("ERTER", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("DFGDFG", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("GHJGH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("KJLJKL", Build.MODEL);
            jObject.put("JKLJK", Build.VERSION.RELEASE);
            jObject.put("RTYFGH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("HJKHJK", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("BNGHJ", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("BNMBMN", Common_Utils.verifyInstallerId(activity));
            jObject.put("FGFGH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
//            AppLogger.getInstance().e("Get Minesweeper ORIGINAL ==>", jObject.toString());
//            AppLogger.getInstance().e("Get Minesweeper ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            Call<Api_Response> call = apiService.getMinesweeper(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            Model_MinesweeperData responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Model_MinesweeperData.class);
//            AppLogger.getInstance().e("RESPONSE11", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                ((Bombsweeper_Activity) activity).setData(responseModel);
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
