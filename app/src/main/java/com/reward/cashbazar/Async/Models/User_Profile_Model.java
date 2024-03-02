package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class User_Profile_Model implements Serializable {
    @Expose
    private String adFailUrl;
    @Expose
    private String isOTPManintance;
    @Expose
    private String message;
    @Expose
    private String userToken;
    @Expose
    private String status;
    @Expose
    private User_History userDetails;

    @Expose
    private String homeNote;

    @Expose
    private Up_Ads topAds;
    @SerializedName("tigerInApp")
    private String tigerInApp;


    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getIsOTPManintance() {
        return isOTPManintance;
    }

    public void setIsOTPManintance(String isOTPManintance) {
        this.isOTPManintance = isOTPManintance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User_History getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User_History userDetails) {
        this.userDetails = userDetails;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}

