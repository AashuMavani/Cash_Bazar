package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Show_Video_List {
    @SerializedName("ads_type")
    private String adsType;
    @Expose
    private String desc;
    @Expose
    private String title;
    @SerializedName("video_id")
    private String videoId;
    @SerializedName("video_points")
    private String videoPoints;
    @Expose
    private String buttonText;

    @SerializedName("points")
    private String watchedVideoPoints;

    public String getAdsType() {
        return adsType;
    }

    public void setAdsType(String adsType) {
        this.adsType = adsType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoPoints() {
        return videoPoints;
    }

    public void setVideoPoints(String videoPoints) {
        this.videoPoints = videoPoints;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getWatchedVideoPoints() {
        return watchedVideoPoints;
    }

    public void setWatchedVideoPoints(String watchedVideoPoints) {
        this.watchedVideoPoints = watchedVideoPoints;
    }


}

