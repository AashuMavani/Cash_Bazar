package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.ActivityWithdrawTypesList;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Task_OfferList_Response_Model;
import com.reward.cashbazar.Async.Models.Withdraw_CategoryList_Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.Activity.TaskPage_Activity;
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

public class Get_Task_Offer_List_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;
    private String type;

    public Get_Task_Offer_List_Async(final Activity activity, String type, String pageNo) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();

        this.type = type;
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("3CT1AH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("R2GKCP", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("6STNR0", pageNo);
            jObject.put("BNDVF7", type);
            jObject.put("BOAIT7", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("5W6W9D", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("TTP0HH", Build.MODEL);
            jObject.put("CVBVDFH", Build.VERSION.RELEASE);
            jObject.put("QTWL9R", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("0URERT", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("WKHOBU", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("N5D3JT", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

//            AppLogger.getInstance().e("TAG","TAG+++---"+type);
//            AppLogger.getInstance().e(".", jObject.toString());
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);

//            AppLogger.getInstance().e("APPTASK--)",""+ jObject.toString());
            Call<Api_Response> call = apiService.getTaskOfferList(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), jObject.toString());
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
        Task_OfferList_Response_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Task_OfferList_Response_Model.class);
            Withdraw_CategoryList_Response_Model responseModel1 = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Withdraw_CategoryList_Response_Model.class);
//        POC_AppLogger.getInstance().e("RESPONSEsa: " + type, "" + new Gson().toJson(responseModel));
//        POC_AppLogger.getInstance().e("RESPONSE_stts: " + "", responseModel.getStatus());
        if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
            Common_Utils.doLogout(activity);
        } else {
            Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
            if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
            }
            POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
            if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                if (activity instanceof TaskPage_Activity) {
//                    POC_AppLogger.getInstance().e("RESPONSEsdfsa: " + type, "" + new Gson().toJson(responseModel));
//                    ((MainActivity) activity).onTaskListDataChanged(responseModel);
                    ((TaskPage_Activity) activity).setData(responseModel);
                }
                if (activity instanceof ActivityWithdrawTypesList) {
                    ((ActivityWithdrawTypesList) activity).setData(responseModel1);
                }
            } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
            } else if (responseModel.getStatus().equals("2")) {
                if (activity instanceof TaskPage_Activity) {
//                    ((MainActivity) activity).onTaskListDataChanged(responseModel);
                    ((TaskPage_Activity) activity).setData(responseModel);
                }
                if (activity instanceof ActivityWithdrawTypesList) {
                    ((ActivityWithdrawTypesList) activity).setData(responseModel1);
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

