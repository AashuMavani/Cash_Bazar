package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification_ListMenu implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("entryDate")
    private String entryDate;

    @SerializedName("icon")
    private String icon;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("isActive")
    private String isActive;
    @SerializedName("url")
    private String url;

    @SerializedName("isShowAds")
    private String isShowAds;

    @SerializedName("screenNo")
    private String screenNo;

    @SerializedName("taskId")
    private String taskId;

    public String getImage() {
        return image;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getUrl() {
        return url;
    }

    public String getIsShowAds() {
        return isShowAds;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public String getTaskId() {
        return taskId;
    }
}