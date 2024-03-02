package com.reward.cashbazar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.reward.cashbazar.Activity.ActivitySplashScreen;
import com.reward.cashbazar.Async.AddPictureStyleNotificationAsync;
import com.reward.cashbazar.Async.Models.User_History;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends FirebaseMessagingService {
    private JSONObject jsonObject;
    private String image = "";
    private String message = "";
    private String title = "";
    private String mType = "";
    private String isForce = "";
    private boolean isAutoClose = false;
    private String contenttext = "";
    private int notificationid = 123;
    private Notification notification = null;
    boolean isForceClose;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        JSONObject data = new JSONObject(remoteMessage.getData());
        //AppLogger.getInstance().e("Notification===", "===" + remoteMessage.getData());
        if (!data.has("custom")) {
            if (remoteMessage.getData().size() > 0) {
                handleDataMessage(remoteMessage.getData());
            }
        }
    }

    private void handleDataMessage(Map<String, String> intent) {
        String message = intent.get("description");
        String title = intent.get("title");
        String image = intent.get("image");
        String screenNo = intent.get("screenNo");
        String matchId = intent.get("matchId");
        String icon = intent.get("icon");
        String isAutoclear = intent.get("isAutoclear");
        String type = intent.get("type");
        String url = intent.get("url");
        String offerId = intent.get("offerId");
        String taskId = intent.get("taskId");
        String btnName = intent.get("btnName");
        String btnColor = intent.get("btnColor");
        String points = intent.get("points");
        String isForceClick = intent.get("isForceClick");
        String notificationID = intent.get("Notification_Id");

        String isTriggerFirebaseEvent = intent.get("isTriggerFirebaseEvent");
        String isTriggerFirebaseEventEverytime = intent.get("isTriggerFirebaseEventEverytime");
        String eventName = intent.get("eventName");
        if (!Common_Utils.isStringNullOrEmpty(isTriggerFirebaseEvent) && isTriggerFirebaseEvent.equals("1")) {
            if ((!Common_Utils.isStringNullOrEmpty(isTriggerFirebaseEventEverytime) && isTriggerFirebaseEventEverytime.equals("1"))
                    || !POC_SharePrefs.getInstance().getBoolean(eventName)) {
                Common_Utils.logFirebaseEvent(App_Controller.getContext(), "FeatureUsabilityItemId", "FeatureUsabilityEvent", eventName, eventName);
                POC_SharePrefs.getInstance().putBoolean(eventName, true);
            }
        }

        try {
            jsonObject = new JSONObject();
            if (image != null) {
                jsonObject.put("image", image);
            } else {
                jsonObject.put("image", "");
            }
            if (icon != null) {
                jsonObject.put("icon", icon);
            } else {
                jsonObject.put("icon", "");
            }
            if (points != null) {
                jsonObject.put("points", points);
            } else {
                jsonObject.put("points", "");
            }
            if (btnName != null) {
                jsonObject.put("btnName", btnName);
            } else {
                jsonObject.put("btnName", "");
            }
            if (btnColor != null) {
                jsonObject.put("btnColor", btnColor);
            } else {
                jsonObject.put("btnColor", "");
            }
            if (isAutoclear != null) {
                jsonObject.put("isAutoclear", isAutoclear);
            } else {
                jsonObject.put("isAutoclear", "");
            }
            if (type != null) {
                jsonObject.put("type", type);
            } else {
                jsonObject.put("type", "");
            }
            if (taskId != null) {
                jsonObject.put("taskId", taskId);
            } else {
                jsonObject.put("taskId", "");
            }
            if (btnName != null) {
                jsonObject.put("btnName", btnName);
            } else {
                jsonObject.put("btnName", "");
            }

            if (isForceClick != null) {
                jsonObject.put("isForceClick", isForceClick);
            } else {
                jsonObject.put("isForceClick", "");
            }
            if (offerId != null) {
                jsonObject.put("offerId", offerId);
            } else {
                jsonObject.put("offerId", "");
            }
            if (title != null) {
                jsonObject.put("title", title);
            } else {
                jsonObject.put("title", "");
            }
            if (matchId != null) {
                jsonObject.put("matchId", matchId);
            } else {
                jsonObject.put("matchId", "");
            }
            if (url != null) {
                jsonObject.put("url", url);
            } else {
                jsonObject.put("url", "");
            }
            if (screenNo != null) {
                jsonObject.put("screenNo", screenNo);
            } else {
                jsonObject.put("screenNo", "");
            }
            if (message != null) {
                jsonObject.put("description", message);
            } else {
                jsonObject.put("description", "");
            }

            if (notificationID != null) {
                jsonObject.put("Notification_Id", notificationID);
            } else {
                jsonObject.put("Notification_Id", "");
            }
            generateNotification(getBaseContext(), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void generateNotification(Context context, String data) {
        try {
            JSONObject jsonData = new JSONObject(data);
            Intent notificationIntent = new Intent(context, ActivitySplashScreen.class);
            notificationIntent.putExtra("bundle", data);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent intent = null;
            try {
                jsonData.getString("Notification_Id");
                if (jsonData.getString("Notification_Id").trim().length() > 0) {
                    notificationid = Integer.parseInt(jsonData.getString("Notification_Id"));
                } else {
                    Random r = new Random();
                    notificationid = r.nextInt(500);
                }
                intent = PendingIntent.getActivity(context, notificationid, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                image = jsonData.getString("image").trim();
                title = jsonData.getString("title").trim();
                message = jsonData.getString("description").trim();
                mType = jsonData.getString("type").trim();
                isForce = jsonData.getString("isForceClick").trim();
                if (title.trim().length() == 0) {
                    title = context.getString(R.string.app_name);
                }
                if (isForce.matches("1")) {
                    isForceClose = true;
                } else {
                    isForceClose = false;
                }
                User_History userDetails = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.User_Details), User_History.class);
                if (title.contains("{name}")) {
                    title = title.replace("{name}", userDetails == null ? "" : userDetails.getFirstName());
                }
                if (message.contains("{name}")) {
                    message = message.replace("{name}", userDetails == null ? "" : userDetails.getFirstName());
                }

                ////AppLogger.getInstance().e("image--)", "" + image);

                if (title.contains("{wallet}")) {
                    title = title.replace("{wallet}", POC_SharePrefs.getInstance().getEarningPointString());
                }
                if (message.contains("{wallet}")) {
                    message = message.replace("{wallet}", POC_SharePrefs.getInstance().getEarningPointString());
                }

                ////AppLogger.getInstance().e("imageCheck--)", "" + image);

                if (image.trim().equalsIgnoreCase("0") || image.trim().equalsIgnoreCase("")) {
                    ////AppLogger.getInstance().e("message--)", "" + message);
                    if (message.trim().length() > 250) {
                        contenttext = message.substring(0, 250);
                    } else {
                        contenttext = message;
                    }
                    Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    int badgeIconType = R.mipmap.ic_launcher;
                    int stateIconName = R.drawable.ic_stat_name;
                    if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("fire")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_fire);
                        badgeIconType = R.drawable.ic_fire;
                        stateIconName = R.drawable.ic_fire;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("playtime")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_playtime);
                        badgeIconType = R.drawable.ic_playtime;
                        stateIconName = R.drawable.ic_playtime;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("paytm")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_paytm);
                        badgeIconType = R.drawable.ic_paytm;
                        stateIconName = R.drawable.ic_paytm;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("upi")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_upi);
                        badgeIconType = R.drawable.ic_upi;
                        stateIconName = R.drawable.ic_upi;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("code")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_redeem_code);
                        badgeIconType = R.drawable.ic_redeem_code;
                        stateIconName = R.drawable.ic_redeem_code;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("whatsapp")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_whatsapp1);
                        badgeIconType = R.drawable.ic_whatsapp1;
                        stateIconName = R.drawable.ic_whatsapp1;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("success")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_success);
                        badgeIconType = R.drawable.ic_success;
                        stateIconName = R.drawable.ic_success;
                    }else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("free")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_free);
                        badgeIconType = R.drawable.ic_free;
                        stateIconName = R.drawable.ic_free;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("fast_withdraw")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_fast_withdraw);
                        badgeIconType = R.drawable.ic_fast_withdraw;
                        stateIconName = R.drawable.ic_fast_withdraw;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("instant_withdraw")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_instant_withdraw);
                        badgeIconType = R.drawable.ic_instant_withdraw;
                        stateIconName = R.drawable.ic_instant_withdraw;
                    } else if (!Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("loot_now")) {
                        icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_loot_now);
                        badgeIconType = R.drawable.ic_loot_now;
                        stateIconName = R.drawable.ic_loot_now;
                    }


                    NotificationManager notificationmanager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("channel_01", "Playback Notification", NotificationManager.IMPORTANCE_HIGH);
                        channel.setSound(null, null);
                        AudioAttributes att = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build();
                        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                        channel.enableVibration(true);
                        channel.enableLights(true);
                        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                        channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
                        notificationmanager.createNotificationChannel(channel);
                        notification = new Notification.Builder(getApplicationContext())
                                .setSmallIcon(stateIconName)
                                .setContentText(contenttext)
                                .setContentIntent(intent)
                                .setAutoCancel(isForceClose)
                                .setOngoing(isForceClose)
                                .setLargeIcon(icon1)
                                .setBadgeIconType(badgeIconType)
                                .setChannelId("channel_01")
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setStyle(new Notification.BigTextStyle().bigText(message))
                                .setContentTitle(title).build();
                    } else {
                        notification = new Notification.Builder(getApplicationContext())
                                .setSmallIcon(stateIconName)
                                .setContentText(contenttext)
                                .setContentIntent(intent)
                                .setAutoCancel(isForceClose)
                                .setOngoing(isForceClose)
                                .setLargeIcon(icon1)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                                .setLights(Color.RED, 3000, 3000)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setStyle(new Notification.BigTextStyle().bigText(message))
                                .setContentTitle(title).build();
                    }
                    if (notificationmanager != null) {
                        notificationmanager.notify(notificationid, notification);
                    }
                } else {
                    new AddPictureStyleNotificationAsync(context, mType, title, message, image, isForceClose, data).execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}