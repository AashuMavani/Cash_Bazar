package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Scratch_Ticket_Model {

    @SerializedName("backImage")
    private String backImage;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("backgroundImage")
    private String backgroundImage;
    @SerializedName("frontImage")
    private String frontImage;
    @SerializedName("homeNote")
    private String homeNote;
    @SerializedName("lastScratchedDate")
    private String lastScratchedDate;
    @SerializedName("message")
    private String message;
    @SerializedName("scratchCardList")
    private List<Scratch_Ticket_List> scratchCardList;
    @SerializedName("scratchTime")
    private String scratchTime;
    @SerializedName("status")
    private String status;
    @SerializedName("todayDate")
    private String todayDate;
    @SerializedName("topAds")
    private Up_Ads topAds;
    @SerializedName("earningPoint")
    private String earningPoint;
    @SerializedName("helpVideoUrl")
    private String helpVideoUrl;
    @SerializedName("adFailUrl")
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;



    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getBackImage() {
        return backImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public String getLastScratchedDate() {
        return lastScratchedDate;
    }

    public String getMessage() {
        return message;
    }

    public List<Scratch_Ticket_List> getScratchCardList() {
        return scratchCardList;
    }

    public String getScratchTime() {
        return scratchTime;
    }

    public String getStatus() {
        return status;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getUserToken() {
        return userToken;
    }




}

