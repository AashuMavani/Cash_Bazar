package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;
import com.reward.cashbazar.Activity.MainActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Model_MinesweeperData;
import com.reward.cashbazar.R;
import com.reward.cashbazar.Activity.RewardPage_Activity;
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

public class GetWalletBalanceAsync {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher cipher;

    public GetWalletBalanceAsync(final Activity activity) {
        this.activity = activity;
        cipher = new AES_Cipher();
        try {
            jObject = new JSONObject();
            jObject.put("RGFVBNH", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("ERTERVBV", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("DFGDFGDFGD", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("GHJGHERTE", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("KJLJKLDFG", Build.MODEL);
            jObject.put("JKLJKDFG", Build.VERSION.RELEASE);
            jObject.put("RTYFGHCVB", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("HJKHJKXV", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("BNGHJXV", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("BNMBMNXV", Common_Utils.verifyInstallerId(activity));
            jObject.put("FGFGHXV", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
//            AppLogger.getInstance().e("Get Wallet Balance ORIGINAL ==>", jObject.toString());
//            AppLogger.getInstance().e("Get Wallet Balance ENCRYPTED ==>", cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            Call<Api_Response> call = apiService.getWalletBalance(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
            call.enqueue(new Callback<Api_Response>() {
                @Override
                public void onResponse(Call<Api_Response> call, Response<Api_Response> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<Api_Response> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Common_Utils.Notify(activity, activity.getString(R.string.app_name), POC_Constants.msg_Service_Error, false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostExecute(Api_Response response) {
        try {
            Model_MinesweeperData responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Model_MinesweeperData.class);
//            AppLogger.getInstance().e("RESPONSE11", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).onUpdateWalletBalance();
                }
                if (activity instanceof RewardPage_Activity) {
                    ((RewardPage_Activity) activity).onUpdateWalletBalance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void onPostExecute(Api_Response response) {
//        try {
//            Model_MinesweeperData data = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Model_MinesweeperData.class);
//            POC_AppLogger.getInstance().e("RESPONSE11", "" + new Gson().toJson(data));
//            if (data.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
//                POC_Common_Utils.doLogout(activity);
//            } else if (!POC_Common_Utils.isStringNullOrEmpty(data.getEarningPoint())) {
//                POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, data.getEarningPoint());
//                if (activity instanceof MainActivity) {
//                    ((MainActivity) activity).onUpdateWalletBalance();
//                }
//                if (activity instanceof RewardPage_Activity) {
//                    ((RewardPage_Activity) activity).onUpdateWalletBalance();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }












// private void onPostExecute(Api_Response response) {
//        try {
//            POC_Common_Utils.dismissProgressLoader();
//            Model_MinesweeperData responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Model_MinesweeperData.class);
//            POC_AppLogger.getInstance().e("RESPONSEpub: ", "" + new Gson().toJson(responseModel));
//            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
//                POC_Common_Utils.doLogout(activity);
//            } else {
//                POC_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
//                if (!POC_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
//                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
//                }
//                if (!POC_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
//                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
//                }
//                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
//                    ((MainActivity) activity).setWalletBalance(responseModel);
//                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
//                    POC_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
//                } else if (responseModel.getStatus().equals("2")) {
//                    POC_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
//                }
//                if (!POC_Common_Utils.isStringNullOrEmpty(responseModel.getTigerInApp())) {
//                    FirebaseInAppMessaging.getInstance().triggerEvent(responseModel.getTigerInApp());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
