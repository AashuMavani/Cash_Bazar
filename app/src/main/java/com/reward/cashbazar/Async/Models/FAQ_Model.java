package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FAQ_Model implements Serializable {
    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("data")
    private List<FAQ_ListItem> FAQList;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("topAds")
    private Up_Ads topAds;

    @SerializedName("adFailUrl")
    private String adFailUrl;

    @SerializedName("tigerInApp")
    private String tigerInApp;


    public String getHomeNote() {
        return homeNote;
    }

    public List<FAQ_ListItem> getFAQList() {
        return FAQList;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public void setFAQList(List<FAQ_ListItem> FAQList) {
        this.FAQList = FAQList;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public String getUserToken() {
        return userToken;
    }
}
