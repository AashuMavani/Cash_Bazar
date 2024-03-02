package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.User_Profile_Model;
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

public class Change_Profile_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;

    public Change_Profile_Async(final Activity activity, String fName, String lName, String countryCode, String countryName) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("BB99WT", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("ADT0AK", POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN());
            jObject.put("V2BC3K ", fName);
            jObject.put("HIN9WY", lName);
            jObject.put("KUR96T", countryCode);
            jObject.put("ODSGRH", countryName);

            jObject.put("BHE2WH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("DFHFVBC", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("DFGKMIU", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("EASOJV", Build.MODEL);
            jObject.put("HJKUYTSD", Build.VERSION.RELEASE);
            jObject.put("0BPD7Z", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("LFJ7T2", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("RMTX59", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("DWGEIO", Common_Utils.verifyInstallerId(activity));
            jObject.put("L2234B", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

            //AppLogger.getInstance().e("JSONRegisterProfile", "" + jObject.toString());
            //AppLogger.getInstance().e("JSONRegisterProfile1", "" + AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString()));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.editUserProfile(POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) ? POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken) : POC_Constants.getUSERTOKEN(), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
            User_Profile_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), User_Profile_Model.class);
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Profile", "User Profile Edit -> Success");
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.User_Details, new Gson().toJson(responseModel.getUserDetails()));
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getUserDetails().getEarningPoint());
                    Common_Utils.NotifySuccess(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Profile", "User Profile Edit -> Fail");
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
