package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.ActivityWithdrawTypesList;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Redeem_Points;
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

public class Redeem_Points_Async {
    private Activity activity;
    private JSONObject jObject;
   private AES_Cipher pocAesCipher;


    public Redeem_Points_Async(final Activity activity, String id, String title, String withdrawType, String mobileNumber) {
        this.activity = activity;
         pocAesCipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("BHSLXW", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("PB3DDS", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("B2H3WL", id);
//            jObject.put("UHEQYC", title);
            jObject.put("BX1M2W", withdrawType);
            jObject.put("1Y3W35", mobileNumber);
            jObject.put("62XW2Q", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("T6IV0A", Build.MODEL);
            jObject.put("MDIYMDT", Build.VERSION.RELEASE);
            jObject.put("8JBDI7", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("TMWL6W", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("4NRSH1", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("VFJDKL", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("QU4QXK", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);

//            POC_AppLogger.getInstance().e("REDEEM POINTS ORIGINAL ==>", jObject.toString());
            Log.e("Get WIthdraw--)",""+pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            Log.e("Get WIthdraw--)",""+(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN())+"--)"+n);
//            AppLogger.getInstance().e("Get POINTS ENCRYPTED ==>", pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            Call<Api_Response> call = apiService.redeemPoints(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

//            Call<Api_Response> call = apiService.redeemPoints(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

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
            Log.e("Get WIthdraw Excpetion-)",""+e.getMessage());

            Common_Utils.dismissProgressLoader();
        }
    }

    private void onPostExecute(Api_Response response) {
        try {
            Common_Utils.dismissProgressLoader();
            Redeem_Points responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Redeem_Points.class);
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                ((ActivityWithdrawTypesList) activity).checkWithdraw(responseModel);
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

