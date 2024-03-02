package com.reward.cashbazar.Async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.MainActivity;
import com.reward.cashbazar.App_Controller;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.Show_Video_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.Activity.RewardPage_Activity;
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

public class SaveQuickTaskAsync {
    private Activity activity;
    private JSONObject jObject;
    private String ids;
    private AES_Cipher cipher;

    public SaveQuickTaskAsync(final Activity activity, String points, String ids) {
        this.activity = activity;
        this.ids = ids;
        cipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("QSXCDREWQ", points);
            jObject.put("EDVASGHTN", ids);
            jObject.put("YHNMJKIOPL", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("SDFSDFS", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("BVNVFVNV", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("FGDFG", POC_SharePrefs.getInstance().getString(POC_SharePrefs.FCMregId));
            jObject.put("YUIYIYUI", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("WERWER", Build.MODEL);
            jObject.put("QWEQDAS", Build.VERSION.RELEASE);
            jObject.put("ASDAWER", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("KHJKHJJ", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("VBNFGHF", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("ASDAWEQ", POC_SharePrefs.verifyInstallerId(activity));
            jObject.put("HKJHKH", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            int n = POC_SharePrefs.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
//            Logger_App.getInstance().e("Save Quick Task ORIGINAL ==>", jObject.toString());
//            Logger_App.getInstance().e("Save Quick Task ENCRYPTED ==>", AESCipher.encrypt(BuildConfig.MKEY, BuildConfig.MIV, jObject.toString()));

            Call<Api_Response> call = apiService.saveQuickTask(POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken), String.valueOf(n), cipher.bytesToHex(cipher.encrypt(jObject.toString())));
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
            Show_Video_Model responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Show_Video_Model.class);
//            AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    Common_Utils.setToast(activity, "Congratulations! Your quick tasks Bucks are credited in your wallet.");
                    if (activity instanceof RewardPage_Activity) {
                        ((RewardPage_Activity) activity).updateQuickTask(true, ids);
                    }
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).updateQuickTask(true, ids);
                    }
                    App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.QUICK_TASK_RESULT)
                            .setPackage(App_Controller.getContext().getPackageName())
                            .putExtra("id", ids)
                            .putExtra("status", POC_Constants.STATUS_SUCCESS));
                } else {
                    if (activity instanceof RewardPage_Activity) {
                        ((RewardPage_Activity) activity).updateQuickTask(false, ids);
                    }
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).updateQuickTask(false, ids);
                    }
                    App_Controller.getContext().sendBroadcast(new Intent(POC_Constants.QUICK_TASK_RESULT)
                            .setPackage(App_Controller.getContext().getPackageName())
                            .putExtra("id", ids)
                            .putExtra("status", POC_Constants.STATUS_ERROR));
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




//    private void onPostExecute(Api_Response response) {
//        try {
//            POC_Common_Utils.dismissProgressLoader();
//            Show_Video_Model responseModel = new Gson().fromJson(new String(cipher.decrypt(response.getEncrypt())), Show_Video_Model.class);
////            Logger_App.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
//            if (responseModel.getStatus().equals(POC_POC_Constants.STATUS_LOGOUT)) {
//                POC_Common_Utils.doLogout(activity);
//            } else {
//                POC_Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
//                if (!POC_Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
//                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
//                }
//                if (!POC_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
//                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
//                }
//                if (responseModel.getStatus().equals(POC_POC_Constants.STATUS_SUCCESS)) {
//                    POC_Common_Utils.setToast(activity, "Congratulations! Your quick tasks points are credited in your wallet.");
//                    if (activity instanceof MainActivity) {
//                        ((MainActivity) activity).updateQuickTask(true, ids);
//                    }
//                    if (activity instanceof RewardPage_Activity) {
//                        ((RewardPage_Activity) activity).updateQuickTask(true, ids);
//                    }
//                } else {
//                    if (activity instanceof MainActivity) {
//                        ((MainActivity) activity).updateQuickTask(false, ids);
//                    }
//                    if (activity instanceof RewardPage_Activity) {
//                        ((RewardPage_Activity) activity).updateQuickTask(false, ids);
//                    }
//                    POC_Common_Utils.Notify(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
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
