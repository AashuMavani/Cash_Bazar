package com.reward.cashbazar.Async;

import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.AES_Cipher;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Store_Package_Install_Data {
        private JSONObject jObject;
   private AES_Cipher pocAesCipher;


    public Store_Package_Install_Data(String packageId) {
        try {
            jObject = new JSONObject();

            Response_Model responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
            String url = responseMain.getPackageInstallTrackingUrl()
                    + "?pid=" + responseMain.getPid()
                    + "&offer_id=" + responseMain.getOffer_id()
                    + "&sub1=" + packageId
                    + "&sub2=" + App_Controller.getContext().getPackageName()
                    + "&sub3=" + POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID);
            jObject.put("6QQDNN", url);
            jObject.put("K1UTO2", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId).length() == 0 ? "0" : POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("79ONSO", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("OO4WLI", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("399MJW", packageId);
            try {
                jObject.put("QGXOGO", Settings.Secure.getString(App_Controller.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                jObject.put("UMD5O0", Settings.Secure.getString(App_Controller.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<JsonObject> call = apiService.savePackageTracking(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostExecute(JsonObject responseModel) {
        try {
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

