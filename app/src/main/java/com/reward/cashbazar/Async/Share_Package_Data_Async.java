package com.reward.cashbazar.Async;

import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Share_Package_Data_Async {
    private Context context;

    public Share_Package_Data_Async(Context c, String p_id) {
        this.context = c;
        try {
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            //AppLogger.getInstance().e("SEND_PACKAGE_INSTALL DATA==>", " =========== " + p_id);
            Response_Model responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
            Call<JsonObject> call = apiService.callApi(responseMain.getPackageInstallTrackingUrl(), responseMain.getPid(), responseMain.getOffer_id(), p_id, POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID), App_Controller.getContext().getPackageName(), Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID), Common_Utils.getIpAddress(context));
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
            Common_Utils.dismissProgressLoader();
            e.printStackTrace();
        }
    }

    private void onPostExecute(JsonObject responseModel) {
        try {
            Common_Utils.logFirebaseEvent(context, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Package_Install", "Package Installed");
            //AppLogger.getInstance().e("RESPONSE", "" + responseModel.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
