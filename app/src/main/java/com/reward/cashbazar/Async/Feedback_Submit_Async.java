package com.reward.cashbazar.Async;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
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

public class Feedback_Submit_Async {
    private Activity activity;
    private JSONObject jObject;
    private AES_Cipher pocAesCipher;

    public Feedback_Submit_Async(final Activity activity, String emailId, String message, String mobileNumber, String image) {
        this.activity = activity;
        pocAesCipher = new AES_Cipher();
        try {
            Common_Utils.showProgressLoader(activity);
            jObject = new JSONObject();

            jObject.put("WX7QG7", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.totalOpen));
            jObject.put("BUNG8S", POC_SharePrefs.getInstance().getInt(POC_SharePrefs.todayOpen));
            jObject.put("MXMXCC", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AdID));
            jObject.put("GNCHLQ", POC_SharePrefs.getInstance().getString(POC_SharePrefs.AppVersion));
            jObject.put("TKHTAE", Build.MODEL);
            jObject.put("SCQOYY", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userId));
            jObject.put("7DXOBK", POC_SharePrefs.getInstance().getString(POC_SharePrefs.userToken));
            jObject.put("CZPP1Y", emailId);
            jObject.put("J4TVD6", mobileNumber);
            jObject.put("ARNXWI", message);
            jObject.put("PPH46S", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                jObject.put("O759L7", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
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
            Call<Api_Response> call = apiService.submitFeedback(requestBodyDetails, body);
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
                    Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Success");
                    Common_Utils.NotifySuccess(activity, activity.getString(R.string.app_name), responseModel.getMessage(), true);
                } else if (responseModel.getStatus().equals(POC_Constants.STATUS_ERROR)) {
                    Common_Utils.logFirebaseEvent(activity, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Feedback", "Submit Feedback -> Fail");
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

