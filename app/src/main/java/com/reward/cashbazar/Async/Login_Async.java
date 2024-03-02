package com.reward.cashbazar.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;
import com.reward.cashbazar.Activity.MainActivity;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Login_Response_Model;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.AES_Cipher;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;
import com.reward.cashbazar.value.POC_Request_Model;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;

    public Login_Async(final Activity activity, POC_Request_Model requestModel) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();

        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();

            jObject.put("JHGKGFG", requestModel.getFirstName().trim());
            jObject.put("VBUIJWS", requestModel.getLastName().trim());
            jObject.put("KUJGHED", requestModel.getEmailId().trim());
            jObject.put("KESNVD", requestModel.getProfileImage().trim());
            jObject.put("MNRGURD", requestModel.getReferralCode().trim());

            jObject.put("Y8NG4X", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("CVUDFS", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("FJEYSH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            if (POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData).length() > 0) {
                jObject.put("VCBNFD", POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData));
            } else {
                jObject.put("VCBNFD", "");
            }

            jObject.put("JKSDGH", "1");// 1 = android
            jObject.put("VTOY7T", Build.MODEL);
            jObject.put("CFGSDS", Build.VERSION.RELEASE);
            jObject.put("VGIXHZ", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("V4KHNK", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("BDN4HH", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("AW8HDW", Common_Utils.verifyInstallerId(activity));
            jObject.put("Y8NG4X", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

//            AppLogger.getInstance().e("Login DATA ORIGINAL==", "" + jObject.toString());
//            AppLogger.getInstance().e("LOGIN DATA ENCRYPTED==", "" + pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);

            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.loginUser(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), pocAesCipher.bytesToHex(pocAesCipher.encrypt(jObject.toString())));
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
//            AppLogger.getInstance().d("TAg","========EFWQQF4R"+"W45EGTW45TG45T");
            e.printStackTrace();
            Common_Utils.dismissProgressLoader();
        }
    }

    private void onPostExecute(Api_Response response) {
        try {
            Common_Utils.dismissProgressLoader();
            Login_Response_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), Login_Response_Model.class);

            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
//            SharePrefs.getInstance().putString(SharePrefs.ReferData, "");
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.User_Details, new Gson().toJson(responseModel.getUserDetails()));
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userId, responseModel.getUserDetails().getUserId());
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserDetails().getUserToken());
                    POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.IS_LOGIN, true);
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getUserDetails().getEarningPoint());
                    Intent in = new Intent(activity, MainActivity.class).putExtra("isFromLogin", true);
                    try {
                        Response_Model responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
                        if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowSurvey()) && responseMain.getIsShowSurvey().matches("1")) {
                            App_Controller app = (App_Controller) activity.getApplication();
                            app.initCPX();
                        }
                        if (!Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAdjump()) && responseMain.getIsShowAdjump().matches("1")) {
                            App_Controller app = (App_Controller)  activity.getApplication();
                            app.initAdjump();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    activity.startActivity(in);
                    activity.finishAffinity();
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
//                String msg = "ORIGINAL: \n\n" + jObject.toString() + "\n\n\nCAPTCHA:\n\n" + captcha + "\n\n\nENCRYPTED: \n\n" + AES_Cipher.encrypt(POC_Constants.getMKEY(),POC_Constants.getMIV(), jObject.toString());
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals("2")) {
                    Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

