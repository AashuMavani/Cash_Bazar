package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.TaskDetailsActivity;
import com.reward.cashbazar.Async.Models.Api_Response;
import com.reward.cashbazar.Async.Models.FAQ_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.network.POC_ApiClient;
import com.reward.cashbazar.network.POC_ApiInterface;
import com.reward.cashbazar.utils.AES_Cipher;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Constants;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Task_Image_Upload_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;

    public Task_Image_Upload_Async(final Activity activity, String taskId, String taskTitle, String image) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();
            jObject.put("LAHOPB", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("WWB91I", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("PQKTWO", taskId);
            jObject.put("QMIFGX", taskTitle);
            jObject.put("KQJABT", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("GF1WQN", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("Z2IAIA", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("603QMJ", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("5YEQH5", Build.MODEL);
            jObject.put("YJA5F4", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("W4WM9A", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            MultipartBody.Part body = null;

            // Send extra params as part
            RequestBody requestBodyDetails =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), jObject.toString());
            try {
                if (image != null) {
                    File file = new File(image);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("image1", file.getName(), requestFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int n = Common_Utils.getRandomNumberBetweenRange(1, 1000000);
            jObject.put("RANDOM", n);
            POC_ApiInterface apiService = POC_ApiClient.getClient().create(POC_ApiInterface.class);
            Call<Api_Response> call = apiService.taskImageUpload(requestBodyDetails, body);
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
            FAQ_Model responseModel = new Gson().fromJson(new String(pocAesCipher.decrypt(response.getEncrypt())), FAQ_Model.class);
            //AppLogger.getInstance().e("RESPONSE", "" + new Gson().toJson(responseModel));
            if (responseModel.getStatus().equals(POC_Constants.STATUS_LOGOUT)) {
                Common_Utils.doLogout(activity);
            } else {
                Ads_Utils.adFailUrl = responseModel.getAdFailUrl();
                if (!Common_Utils.isStringNullOrEmpty(responseModel.getUserToken())) {
                    POC_SharePrefs.getInstance().putString(POC_SharePrefs.userToken, responseModel.getUserToken());
                }
                if (responseModel.getStatus().equals(POC_Constants.STATUS_SUCCESS)) {
                    if (activity instanceof TaskDetailsActivity) {
                        ((TaskDetailsActivity) activity).uploadTaskImageSuccess();
                    }
                    Common_Utils.NotifySuccess(activity, activity.getString(R.string.app_name), responseModel.getMessage(), false);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
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

